package com.leessy.myfacetest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.FaceDetector
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.AiChlFace.AiChlFace
import com.AiChlFace.FACE_DETECT_RESULT
import com.AiChlIrFace.AiChlIrFace
import com.AiFace.AiFace
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class MainActivity : RxAppCompatActivity() {
    val tag = "MainActivity"
    val subject0 = PublishSubject.create<Int>()
    val subject1 = PublishSubject.create<Int>()
    val subject2 = PublishSubject.create<Int>()
    val subject3 = PublishSubject.create<Int>()
    val getsubject0 = PublishSubject.create<Int>()
    val getsubject1 = PublishSubject.create<Int>()
    val getsubject2 = PublishSubject.create<Int>()
    val getsubject3 = PublishSubject.create<Int>()


    var bytes: ByteArray? = null
    var w = 0
    var h = 0
    var facesize = 0
    var sss: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val s = AiFace.InitDebug(this)
//        val s2 = AiChlIrFace.InitDebug(this, 4)

//        val s = AiChlFace.InitDebug(this, 4)
//        val s2 = AiChlIrFace.InitDebug(this, 4)
        val s = AiChlFace.InitDm2016License(this, 4)
        val s2 = AiChlIrFace.InitDm2016License(this, 4)
        text.text = "初始化=$s"
//        text.append("\n使用内存根目录图片aaaa.jpg")
        facesize = AiChlFace.FeatureSize()
        startAiface()

        RxView.clicks(StartTest).take(1)
            .subscribe {
                faceFun(subject0)
            }

        RxView.clicks(start1).take(1)
            .subscribe {
                faceFun(subject1)
            }

        RxView.clicks(start2).take(1)
            .subscribe {
                faceFun(subject2)
            }

        RxView.clicks(start3).take(1)
            .subscribe {
                faceFun(subject3)
            }
        RxView.clicks(test1)
            .subscribe {
                test1()
//                getFeatue(getsubject0)
//                getFeatue(getsubject1)
//                getFeatue(getsubject2)
//                getFeatue(getsubject3)
            }
        RxView.clicks(test2)
            .subscribe {
                test2()
//                getFeatue(getsubject0)
//                getFeatue(getsubject1)
//                getFeatue(getsubject2)
//                getFeatue(getsubject3)
            }



        Observable.interval(2, TimeUnit.SECONDS, Schedulers.io())
            .compose(this.bindToLifecycle())
            .map { cpuInfo.getCPURateDesc() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                textView.text = "\nCPU总消耗: $it"
            }
//
//        if(s==0){
//            faceFun(subject0)
//            faceFun(subject1)
//            faceFun(subject2)
//            faceFun(subject3)
//        }


        //系统人脸检测测试/////////////////
//        Observable.interval(10, TimeUnit.MILLISECONDS)
//            .observeOn(Schedulers.computation())
//            .compose(this.bindToLifecycle())
//            .subscribe {
//                if (bmp != null) {
//                    var start = System.currentTimeMillis()
//                    val mFace = arrayOfNulls<FaceDetector.Face>(1)
//                    var f = FaceDetector(bmp!!.getWidth(), bmp!!.getHeight(), 1)
//                        .findFaces(bmp!!, mFace)
//                    Log.d(
//                        tag,
//                        "1111系统api检测人脸 $f 个,  时间${System.currentTimeMillis() - start}  thread=${Thread.currentThread().name}"
//                    )
//                }
//
//            }

        //单通道算法测试///////////////////
//        Observable.interval(1200, TimeUnit.MILLISECONDS)
//            .observeOn(Schedulers.computation())
//            .compose(this.bindToLifecycle())
//            .subscribe {
//                if (bytes != null) {
//                    //                单通道版本
//                    var start = System.currentTimeMillis()
//                    val result1 = com.AiFace.FACE_DETECT_RESULT()
//                    val w1 = IntArray(1)//转换后宽
//                    val h1 = IntArray(1)//转换后高
//                    val ret = AiFace.AiFaceDetectFaceEx(
//                        2, bytes, w, h, 0, 0, 0, 0,
//                        0, 0, rgb24, w1, h1, result1
//                    )
//                    Log.d(
//                        tag,
//                        "单线程  检测人脸 $ret 个,  时间${System.currentTimeMillis() - start}  thread=${Thread.currentThread().name}"
//                    )
//                }
//
//            }

        //系统人脸检测测试
//        Observable.interval(1200, TimeUnit.MILLISECONDS)
//            .observeOn(Schedulers.computation())
//            .compose(this.bindToLifecycle())
//            .subscribe {
//                if (bmp != null) {
//                    var start = System.currentTimeMillis()
//                    for (i in 1..10)
//                        bitmapToBgr24(bmp)
//                    Log.d(
//                        tag,
//                        "功能 消耗测试,  时间${System.currentTimeMillis() - start}  thread=${Thread.currentThread().name}"
//                    )
//                }
//
//            }

    }

    fun bitmapToBgr24(image: Bitmap?): ByteArray? {
        var image = image
        if (image == null) {
            return null
        } else {
            if (image.config != Bitmap.Config.ARGB_8888) {
                image = image.copy(Bitmap.Config.ARGB_8888, true)
            }

            val bytes = image!!.byteCount
            val buffer = ByteBuffer.allocate(bytes)
            image.copyPixelsToBuffer(buffer)
            val rgbaData = buffer.array()
            val pixelCount = rgbaData.size / 4
            val bgrData = ByteArray(pixelCount * 3)
            var rgbaIndex = 0
            var bgrIndex = 0

            for (i in 0 until pixelCount) {
                bgrData[bgrIndex] = rgbaData[rgbaIndex + 2]
                bgrData[bgrIndex + 1] = rgbaData[rgbaIndex + 1]
                bgrData[bgrIndex + 2] = rgbaData[rgbaIndex]
                bgrIndex += 3
                rgbaIndex += 4
            }

            return bgrData
        }
    }

    var test = 0
    var bmp: Bitmap? = null
    //开始测试
    private fun startAiface() {
        var inputStream: InputStream? = null

//        this.resources.assets
        try {
            inputStream = this.resources.assets.open("aaaa.jpg")
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
//        Bitmap bitmap = BitmapFactory . decodeStream (inputStream);
//
//        val file = try {
//            File(Environment.getExternalStorageDirectory(), "aaaa.jpg")
//        } catch (e: Exception) {
//            Log.d(tag, e.toString())
//            return
//        }
//        if (!file.isFile) return
        bmp = BitmapFactory.decodeStream(inputStream) ?: return
//        val bmp: Bitmap? = BitmapFactory.decodeFile(file.absolutePath) ?: return
        w = bmp!!.width
        h = bmp!!.height
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
                when (it % 4) {
                    0L -> getsubject0.onNext(l)
                    1L -> getsubject1.onNext(l)
                    2L -> getsubject2.onNext(l)
                    3L -> getsubject3.onNext(l)
                }
            }
    }


    var currentPoints = arrayListOf<Long>(0L, 0L, 0L, 0L) //0L//当前分数
    var num = arrayListOf<Long>(0L, 0L, 0L, 0L)//总次数
    var totalpoints = arrayListOf<Long>(0L, 0L, 0L, 0L)//总分

    var totalgetFacepoints = arrayListOf<Long>(0L, 0L, 0L, 0L)//获取模板总分数
    var currentgetface = arrayListOf<Long>(0L, 0L, 0L, 0L)//获取模板当前分数
    fun faceFun(subject: PublishSubject<Int>): Disposable {
        return subject
            .observeOn(Schedulers.io())
//            .sample(200, TimeUnit.MILLISECONDS)
            .map {
                Log.d(tag, "开始 $it     thread=${Thread.currentThread().name}")
                val startTime = System.currentTimeMillis()
                val result = com.AiChlFace.FACE_DETECT_RESULT()
                val rgb24 = ByteArray(h * w * 3)
                val w1 = IntArray(1)//转换后宽
                val h1 = IntArray(1)//转换后高

                //多通道版本
                val ret = AiChlFace.DetectFaceEx(
                    it,
                    2, bytes, w, h, 0, 0, 0, 0,
                    0, 0, rgb24, w1, h1, result
                )
                if (ret > 0) {
                    num[it]++
                    val startTime2 = System.currentTimeMillis()
                    currentPoints[it] = startTime2 - startTime
                    Log.d(tag, "--- 检测人脸 $it  时间  ${currentPoints[it]}   thread=${Thread.currentThread().name}")

                    totalpoints[it] = totalpoints[it] + currentPoints[it]

//                    if (test++ == 0) {
                        val id_temp = ByteArray(facesize)
                        var r = AiChlFace.FeatureGet(it, rgb24, w1[0], h1[0], result, id_temp)
                        currentgetface[it] = System.currentTimeMillis() - startTime2
                        Log.d(tag, "--- 提取特征码 $it  时间  ${currentgetface[it]}")
//                    }
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

    fun getFeatue(subject: PublishSubject<Int>) {
        subject.observeOn(Schedulers.computation())
            .filter { result.nFaceBottom != 0 }
            .map {
                var start = System.currentTimeMillis()
                val id_temp = ByteArray(facesize)
                var r = AiChlFace.FeatureGet(it, rgb24, w, h, result, id_temp)
                Log.d(
                    tag,
                    "--- 提取特征码**** $it  时间  ${System.currentTimeMillis() - start}    thread=${Thread.currentThread().name}"
                )
            }.subscribe()
    }

    val result = FACE_DETECT_RESULT()
    val rgb24 = ByteArray(640 * 480 * 3)

    fun test2() {
        Thread(Runnable {
            val startTime = System.currentTimeMillis()
            val w1 = IntArray(1)//转换后宽
            val h1 = IntArray(1)//转换后高
            var s = AiChlFace.DetectFaceEx(
                1,
                2, bytes, w, h, 0, 0, 0, 0,
                0, 0, rgb24, w1, h1, FACE_DETECT_RESULT()
            )
            runOnUiThread(Runnable {
                Toast.makeText(
                    this@MainActivity,
                    "子线程检测一次成功 $s   时间${System.currentTimeMillis() - startTime}\"",
                    Toast.LENGTH_LONG
                ).show()
            })
        }).start()
    }

    private fun test1() {
        val startTime = System.currentTimeMillis()

        Log.d(tag, "开始  thread=${Thread.currentThread().name}")
//        val result = com.AiChlFace.FACE_DETECT_RESULT()
//        val rgb24 = ByteArray(h * w * 3)
        val w1 = IntArray(1)//转换后宽
        val h1 = IntArray(1)//转换后高

        val ret = AiChlFace.DetectFaceEx(
            1,
            2, bytes, w, h, 0, 0, 0, 0,
            0, 0, rgb24, w1, h1, result
        )
        runOnUiThread(Runnable {
            Toast.makeText(
                this@MainActivity,
                "主线程检测一次成功 $ret  时间${System.currentTimeMillis() - startTime}",
                Toast.LENGTH_LONG
            ).show()
        })
//
//        AiChlIrFace.DetectFaceEx(
//            1,
//            2, bytes, w, h, 0, 0, 0, 0,
//            0, 0, rgb24, w1, h1, com.AiChlIrFace.FACE_DETECT_RESULT()
//        )
//        Thread(Runnable {
//            //            while (true){
//
//            AiChlIrFace.DetectFaceEx(
//                1,
//                2, bytes, w, h, 0, 0, 0, 0,
//                0, 0, rgb24, w1, h1, com.AiChlIrFace.FACE_DETECT_RESULT()
//            )
////            }
//        }).start()


//        if (ret > 10) {
////            Thread(Runnable {
////                val startTime = System.currentTimeMillis()
////                Log.d(tag, "--- 开始检测人脸")
////                AiChlFace.DetectFaceEx(
////                    1,
////                    2, bytes, w, h, 0, 0, 0, 0,
////                    0, 0, rgb24, w1, h1, FACE_DETECT_RESULT()
////                )
////                Log.d(
////                    tag, "--- 开始检测人脸2  ${System.currentTimeMillis() - startTime}"
////                )
////
////            }).start()
//
//            Thread(Runnable {
//                val startTime = System.currentTimeMillis()
//                Log.d(tag, "--- 开始检测人脸----****")
//                val result1 = com.AiChlIrFace.FACE_DETECT_RESULT()
//                AiChlIrFace.DetectFaceEx(
//                    1,
//                    2, bytes, w, h, 0, 0, 0, 0,
//                    0, 0, rgb24, w1, h1, result1
//                )
//                Log.d(
//                    tag, "--- 开始检测人脸------****2  ${System.currentTimeMillis() - startTime}"
//                )
//
//
//                Thread(Runnable {
//                    Log.d(tag, "--- 开始提特征")
//                    val id_temp = ByteArray(facesize)
//                    var r = AiChlIrFace.FeatureGet(0, rgb24, w1[0], h1[0], result1, id_temp)
//                    Log.d(tag, "--- 开始提特征2  ${System.currentTimeMillis() - startTime}    ")
//
//                }).start()
////                Thread(Runnable {
////                    Log.d(tag, "--- 开始提特征")
////                    val id_temp = ByteArray(facesize)
////                    var r = AiChlIrFace.FeatureGet(1, rgb24, w1[0], h1[0], result1, id_temp)
////                    Log.d(tag, "--- 开始提特征2  ${System.currentTimeMillis() - startTime}")
////
////                }).start()
//                Thread(Runnable {
//                    Log.d(tag, "--- 开始提特征")
//                    val id_temp = ByteArray(facesize)
//                    var r = AiChlIrFace.FeatureGet(2, rgb24, w1[0], h1[0], result1, id_temp)
//                    Log.d(tag, "--- 开始提特征2  ${System.currentTimeMillis() - startTime}")
//
//                }).start()
//                Thread(Runnable {
//                    Log.d(tag, "--- 开始提特征")
//                    val id_temp = ByteArray(facesize)
//                    var r = AiChlIrFace.FeatureGet(3, rgb24, w1[0], h1[0], result1, id_temp)
//                    Log.d(tag, "--- 开始提特征2  ${System.currentTimeMillis() - startTime}")
//
//                }).start()
//
//                Thread(Runnable {
//                    val startTime = System.currentTimeMillis()
//                    Log.d(tag, "--- 开始检测人脸----****")
//                    AiChlIrFace.DetectFaceEx(
//                        1,
//                        2, bytes, w, h, 0, 0, 0, 0,
//                        0, 0, rgb24, w1, h1, com.AiChlIrFace.FACE_DETECT_RESULT()
//                    )
//                    Log.d(
//                        tag, "--- 开始检测人脸------****2  ${System.currentTimeMillis() - startTime}"
//                    )
//                }).start()
//
//
//            }).start()
//
//
//        }

    }


}
