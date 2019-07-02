package com.leessy.myfacetest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.AiChlFace.AiChlFace
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : RxAppCompatActivity() {
    val tag = "MainActivity"
    val subject0 = PublishSubject.create<Int>()
    val subject1 = PublishSubject.create<Int>()
    val subject2 = PublishSubject.create<Int>()
    val subject3 = PublishSubject.create<Int>()


    var bytes: ByteArray? = null
    var w = 0
    var h = 0
    var facesize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val s = AiChlFace.InitDebug(this, 4)
        text.text = "初始化=$s"
        text.append("\n使用内存根目录图片aaaa.jpg")
        facesize = AiChlFace.FeatureSize()
        startAiface()
//
//        RxView.clicks(StartTest).take(1)
//            .subscribe {
//                faceFun(subject0)
//            }
//
//        RxView.clicks(start1).take(1)
//            .subscribe {
//                faceFun(subject1)
//            }
//
//        RxView.clicks(start2).take(1)
//            .subscribe {
//                faceFun(subject2)
//            }
//
//        RxView.clicks(start3).take(1)
//            .subscribe {
//                faceFun(subject3)
//            }

        Observable.interval(2, TimeUnit.SECONDS)
            .compose(this.bindToLifecycle())
            .map { cpuInfo.getCPURateDesc() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                textView.text = "\nCPU总消耗: $it"
            }

        if(s==0){
            faceFun(subject0)
            faceFun(subject1)
            faceFun(subject2)
            faceFun(subject3)
        }
    }


    //开始测试
    private fun startAiface() {

        val file = try {
            File(Environment.getExternalStorageDirectory(), "aaaa.jpg")
        } catch (e: Exception) {
            Log.d(tag, e.toString())
            return
        }
        if (!file.isFile) return
        val bmp: Bitmap? = BitmapFactory.decodeFile(file.absolutePath) ?: return
        w = bmp!!.width
        h = bmp.height
        bytes = (YUV420SP_Util.BitmapToYUV420SP(bmp) ?: return)
        Log.d(tag, "yuv数据 " + bytes.toString())

        Observable.interval(20, TimeUnit.MILLISECONDS)
            .compose(this.bindToLifecycle())
            .subscribe {
                val l = (it % 4).toInt()
                when (it % 4) {
                    0L -> subject0.onNext(l)
                    1L -> subject1.onNext(l)
                    2L -> subject2.onNext(l)
                    3L -> subject3.onNext(l)
                }
            }
    }


    var currentPoints = arrayListOf<Long>(0L, 0L, 0L, 0L) //0L//当前分数
    var num = arrayListOf<Long>(0L, 0L, 0L, 0L)//总次数
    var totalpoints = arrayListOf<Long>(0L, 0L, 0L, 0L)//总分

    var totalgetFacepoints = arrayListOf<Long>(0L, 0L, 0L, 0L)//获取模板总分数
    var currentgetface = arrayListOf<Long>(0L, 0L, 0L, 0L)//获取模板当前分数

    fun faceFun(subject: PublishSubject<Int>) {
        subject
            .observeOn(Schedulers.computation())
//            .sample(200, TimeUnit.MILLISECONDS)
            .map {
                Log.d(tag, "开始 $it     thread=${Thread.currentThread().name}")
                val startTime = System.currentTimeMillis()
                val result = com.AiChlFace.FACE_DETECT_RESULT()
                val rgb24 = ByteArray(h * w * 3)
                val w1 = IntArray(1)//转换后宽
                val h1 = IntArray(1)//转换后高

                val ret = AiChlFace.DetectFaceEx(
                    it,
                    2, bytes, w, h, 0, 0, 0, 0,
                    0, 0, rgb24, w1, h1, result
                )
                if (ret > 0) {
                    num[it]++
                    val startTime2 = System.currentTimeMillis()
                    currentPoints[it] = startTime2 - startTime
                    totalpoints[it] = totalpoints[it] + currentPoints[it]

                    val id_temp = ByteArray(facesize)
                    var r = AiChlFace.FeatureGet(it, rgb24, w1[0], h1[0], result, id_temp)
                    currentgetface[it] = System.currentTimeMillis() - startTime2
                    Log.d(tag, "提取特征码 $it  时间  ${currentgetface[it]}")
                    totalgetFacepoints[it] += currentgetface[it]
                }
                Log.d(tag, "结束 $it      thread=${Thread.currentThread().name}")
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //                Log.d(tag, "检测人脸时间 ${currentPoints[it]}    thread=${Thread.currentThread().name}")
                it.run {
                    when (this) {
                        0 -> text
                        1 -> text1
                        2 -> text2
                        3 -> text3
                        else -> text
                    }
                }.text = "通道：$it     总计:${num[it]}" +
                        "\n     人脸时间：${currentPoints[it]}   平均时间:${(totalpoints[it] / num[it]).toInt()}" +
                        "\n     特征时间：${currentgetface[it]}  平均时间:${(totalgetFacepoints[it] / num[it]).toInt()}"
            }, {
                Log.d(tag, "异常 $it      thread=${Thread.currentThread().name}")
            })

    }

}
