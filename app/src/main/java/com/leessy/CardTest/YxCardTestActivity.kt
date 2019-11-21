package com.leessy.CardTest

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.hdos.usbdevice.publicSecurityIDCardLib
import com.leessy.KotlinExtension.onClick
import com.leessy.coolkotlin.R
import kotlinx.android.synthetic.main.activity_yx_card_test.*
import java.util.*

class YxCardTestActivity : AppCompatActivity() {
    private var iDCardDevice: publicSecurityIDCardLib? = null

    var cardSDk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yx_card_test)
        init()
        read.onClick { if (cardSDk) readcard() else ToastUtils.showLong("初始化失败") }
    }


    //初始化
    private fun init() {
        var ret = 0
        try {
            iDCardDevice = publicSecurityIDCardLib(this)
            if (iDCardDevice != null) {
                ret = iDCardDevice?.getSAMStatus()!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cardSDk = ret == 0x90
    }

    //读卡方法
    fun readcard() {
        val decodeInfo = arrayOfNulls<String>(13)
        val BmpFile = ByteArray(38556)
        Arrays.fill(BmpFile, 0x00.toByte())
        try {
            val ret = iDCardDevice?.PICC_Reader_ForeignerIDCard(
                decodeInfo,
                BmpFile,
                getPackageName()
            )
            if (ret == 0 || ret == 1) {//读卡成功，0中国身份证; 1外国居住证
                val colors = iDCardDevice?.convertByteToColor(BmpFile)
                val bm = Bitmap.createBitmap(colors, 102, 126, Bitmap.Config.ARGB_8888)
                val bm1 = Bitmap.createScaledBitmap(bm, 102 * 2, 126 * 2, false) //这里你可以自定义它的大小
                var c = ScanCardItem(decodeInfo, bm1, ret)
                Log.d("读卡结果 ", c.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class ScanCardItem(decodeInfo: Array<String?>, bitmap: Bitmap, ret: Int) {
        var retCode = -1//读卡状态
        var type = 1//类型  0表示刷证  1表示无证
        var name: String? = null
        var id_num: String? = ""//身份证号码
        var gender: String? = ""//性别
        var nation_str: String? = ""//民族
        var birth_date: String? = ""//出生日期
        var address: String? = ""//地址
        var finger: ByteArray? = null//指纹特征码
        var picBitmap: Bitmap? = null//身份证头像
        var certOrg: String? = ""//签发机关
        var effDate: String? = ""//有效期开始
        var expDate: String? = ""//有效期结束

        init {
            if (decodeInfo != null) {
                if (ret == 0 || ret == 1) {
                    retCode = 1
                }
                name = decodeInfo[0]
                id_num = decodeInfo[5]
                gender = decodeInfo[1]
                nation_str = decodeInfo[2]
                birth_date = decodeInfo[3]
                address = decodeInfo[4]
                picBitmap = bitmap
                certOrg = decodeInfo[6]
                effDate = decodeInfo[7]
                expDate = decodeInfo[8]
            }
        }

        override fun toString(): String {
            return "ScanCardItem(retCode=$retCode, type=$type, name=$name, id_num=$id_num, gender=$gender, nation_str=$nation_str, birth_date=$birth_date, address=$address, finger=${finger?.contentToString()}, picBitmap=$picBitmap, certOrg=$certOrg, effDate=$effDate, expDate=$expDate)"
        }
    }
}
