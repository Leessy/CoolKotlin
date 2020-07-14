package com.leessy.CardTest

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import cn.mineki.Utils.ToastUtil
import com.aiface.jidacard.card.IDCardUtil
import com.leessy.coolkotlin.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_jidacard.*
import vpos.apipackage.Picc
import vpos.apipackage.Sys
import java.util.*
import java.util.concurrent.TimeUnit

class JidaCardTestActivity : AppCompatActivity() {
    private val TAG = "JidaCardTestActivity"
    var cardSDk = false

    var readCardSuccess = 0
    var readCardFail = 0
    var readBankCardSuccess = 0
    var readBankCardFail = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jidacard)
        initSdk(application)
    }

    //初始化读卡模块参数
    fun initSdk(context: Context) {
        IDCardUtil.initSdk(context, "/dev/ttyS4")
        cardSDk = IDCardUtil.openSdk()
        if (cardSDk) {
            val data = IDCardUtil.getVersion()
            version.text = "固件版本：V${data[0] + 48}.${data[1] + 48}"
            readstatus.text = "初始化成功"
        } else {
            readstatus.text = "初始化失败"
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        IDCardUtil.closeSdk()
        d?.let { it.dispose() }
    }

    fun startread(view: View) {
        if (!cardSDk) {
            ToastUtil.showToast(this, "读卡模块未初始化")
            return
        }
        readType.text = "正在读身份证..."
        start()
    }

    fun startreadbankcard(view: View) {
        if (!cardSDk) {
            ToastUtil.showToast(this, "读卡模块未初始化")
            return
        }
        readType.text = "正在读银行卡..."
        bankcard()
    }

    private fun bankcard() {
        d?.let { it.dispose() }
        d = Observable.intervalRange(0, Long.MAX_VALUE, 500, 300, TimeUnit.MILLISECONDS)
            .map { IDCardUtil.searchBankCard() }//TODO 这里是进行寻卡操作，寻到卡后面才进行身份证读卡操作
            .filter { it }
            .map {
                IDCardUtil.readBankCardNotSearch()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.retCode != -1) {
                    readstatus.text = "读卡成功"
                    readBankCardSuccess++
                    text.text = "证件类型：银行卡 \n\n"
                    text.append(it.toString())

                } else {
                    readBankCardFail++
                    readstatus.text = "读卡失败"
                }
                readcount.text = "读卡总数：$readBankCardSuccess   失败总数$readBankCardFail"
            }, {
                readstatus.text = "读卡异常：$it"
                bankcard()
            })
    }

    var d: Disposable? = null
    fun start() {
        d?.let { it.dispose() }
        d = Observable.intervalRange(0, Long.MAX_VALUE, 500, 300, TimeUnit.MILLISECONDS)
            .map { IDCardUtil.searchIDCard() }//TODO 这里是进行寻卡操作，寻到卡后面才进行身份证读卡操作
            .filter { it }
            .map {
                IDCardUtil.readCard()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.retCode != -1) {//-1为读卡失败，0身份证，1外国人居住证，2港澳台居住证
                    readCardSuccess++
                    readstatus.text = "读卡成功"
                    Log.d(TAG, "指纹=${Arrays.toString(it.finger)}")
                    text.text = "UID=${it.uuid}\n\n"
                    text.append(
                        "证件类型：${when (it.retCode) {
                            0 -> "身份证"
                            1 -> "外国人居住证"
                            2 -> "港澳台居民居住证"
                            else -> ""
                        }}\n\n"
                    )
                    text.append(it.toString())
                    image.setImageBitmap(it.picBitmap)
                    Log.d(TAG, "宽=${it.picBitmap.width} ")
                    Log.d(TAG, "高=${it.picBitmap.height} ")
                    readcount.text = "读卡总数：$readCardSuccess   失败总数$readCardFail"
                } else {
                    readCardFail++
                    readstatus.text = "读卡失败"
                }
            }, {
                readstatus.text = "读卡异常：$it"
                start()
            })
    }

}
