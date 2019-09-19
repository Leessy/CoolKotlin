package com.leessy.coolkotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.TextureView
import com.AiChlFace.AiChlFace
import com.aiface.uvccamera.camera.Camera
import com.aiface.uvccamera.camera.CamerasMng
import com.aiface.uvccamera.camera.IFrameCall
import com.blankj.utilcode.util.TimeUtils
import com.leessy.F602SystemTool
import com.leessy.KotlinExtension.check_mkdirs
import com.leessy.LED
import com.leessy.MyBitmapFactory
import com.leessy.util.cpuInfo
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xcrash.XCrash
import java.io.*
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class FunctionTestActivity : RxAppCompatActivity() {
    private val TAG = javaClass.name
    var c: Camera? = null
    var c2: Camera? = null
    val cameraColorW = 640
    val cameraColorH = 480
    val cameraIrW = 640
    val cameraIrH = 480
    var filename: String = ""
    lateinit var fr: FileWriter
    var fileImgDir = ""//图片保存路劲

    var savaimage = false
    var savaimage1 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_test)
        //开灯
        F602SystemTool.openLed(LED.WHITE_LED_LIGHT, LED.IR_LED_LIGHT)
        initFiles()
        GlobalScope.launch(Dispatchers.Main) {
            startpreview()
        }
        startTest()
        cpuInfos()
        savaIme()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    //定时保存图片 3分钟
    private fun savaIme() {
        Observable.intervalRange(0, Long.MAX_VALUE, 0, 180L, TimeUnit.SECONDS, Schedulers.io())
            .compose(this.bindToLifecycle())
            .map {
                //获取cpu信息保存并且保存一张图片
                savaimage = true
                savaimage1 = true
            }.subscribe({
            }, {
                savaIme()
            })
    }


    //获取cpu信息并记录
    private fun cpuInfos() {
        Observable.intervalRange(0, Long.MAX_VALUE, 0, 5L, TimeUnit.SECONDS, Schedulers.io())
            .compose(this.bindToLifecycle())
            .map {
                //获取cpu信息保存并且保存一张图片
                val cpu = arrayListOf<String>()
                for (i in 0..5) {
                    val s = FileReader("/sys/devices/system/cpu/cpu$i/cpufreq/scaling_cur_freq")
                    cpu.add("CPU频率核心$i：${s.readLines()}")
                    s.close()
                }
                val socthermal = FileReader("/sys/class/thermal/thermal_zone0/temp")
                val gputhermal = FileReader("/sys/class/thermal/thermal_zone1/temp")//gpu-thermal
                val battery = FileReader("/sys/class/thermal/thermal_zone2/temp")//gpu-battery

                val thermal =
                    "SOC:${socthermal.readLines()}  GPU:${gputhermal.readLines()}  battery:${battery.readLines()}"
                socthermal.close()
                gputhermal.close()
                battery.close()
                "总占用${cpuInfo.getCPURateDesc()}  --${cpu}\n$thermal"
            }.subscribe({
                Log.d(TAG, "读取cpu信息:${it}")
                writerf(arrayListOf("${TimeUtils.getNowString()}\n", it, "\n\n"))
            }, {
                Log.d(TAG, "读取cpu 频率= ee:$it")
                cpuInfos()
            })
    }

    var bytes: ByteArray? = null
    var w = 0
    var h = 0
    var facesize = 0

    val subject0 = PublishSubject.create<Int>()
    val subject1 = PublishSubject.create<Int>()


    //    var currentPoints = arrayListOf<Long>(0L, 0L, 0L, 0L) //0L//当前分数
    val totalnum = arrayListOf<Long>(0L, 0L, 0L, 0L)//总次数
    val totalpoints = arrayListOf<Long>(0L, 0L, 0L, 0L)//总时间
    val totalgetFacepoints = arrayListOf<Long>(0L, 0L, 0L, 0L)//获取模板总时间
//    var currentgetface = arrayListOf<Long>(0L, 0L, 0L, 0L)//获取模板当前分数


    //开始跑算法测试
    private fun startTest() {
        facesize = AiChlFace.FeatureSize()
        var inputStream: InputStream? = null
        try {
            inputStream = this.resources.assets.open("aaaa.jpg")
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
        var bmp = BitmapFactory.decodeStream(inputStream) ?: return
//        val bmp: Bitmap? = BitmapFactory.decodeFile(file.absolutePath) ?: return
        w = bmp!!.width
        h = bmp!!.height
        bytes = (YUV420SP_Util.BitmapToYUV420SP(bmp) ?: return)
        Log.d(TAG, "yuv数据 " + bytes.toString())

        Observable.interval(20, TimeUnit.MILLISECONDS)
            .compose(this.bindToLifecycle())
            .subscribe {
                val l = (it % 4).toInt()
                when (it % 4) {
                    0L -> subject0.onNext(l)
                    1L -> subject1.onNext(l)
                }
            }
        faceFun(subject0)
        faceFun(subject1)
    }


    fun faceFun(subject: PublishSubject<Int>): Disposable {
        return subject
            .observeOn(Schedulers.computation())
            .compose(this.bindToLifecycle())
            .map {
                //                Log.d(TAG, "开始识别 $it     thread=${Thread.currentThread().name}")
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
//                totalnum[it]++//总次数加1
                val startTime2 = System.currentTimeMillis()
                totalpoints[it] += startTime2 - startTime//总分数
//                Log.d(TAG, "人脸识别 $it     时间=${totalpoints[it]}")
                if (ret > 0) {
                    val id_temp = ByteArray(facesize)
                    var r = AiChlFace.FeatureGet(it, rgb24, w1[0], h1[0], result, id_temp)
                    totalgetFacepoints[it] += System.currentTimeMillis() - startTime2
                }
                it
            }
            .buffer(60, TimeUnit.SECONDS)
            .subscribe({
                if (it.size > 0) {
                    val totals = it.size
                    val t = it[0]
                    writerf(
                        arrayListOf(
                            "${TimeUtils.getNowString()}\n",
                            "----线程${t}    人脸识别次数：${totals} 平均时间：${(totalpoints[t] / totals).toInt()}",
                            "   提取特征码次数：${totals} 平均时间：${(totalgetFacepoints[t] / totals).toInt()}"
                            , "\n\n"
                        )
                    )
                    totalnum[t] = 0
                    totalgetFacepoints[t] = 0
                    totalpoints[t] = 0
                }

            }, {
                Log.d(TAG, "异常 $it    thread=${Thread.currentThread().name}")
                XCrash.testJavaCrash(true)
            })
    }


    //写文件
    @Synchronized
    fun writerf(vararg list: ArrayList<String>) {
        //每次启动创建一个测试文件
        fr = FileWriter(filename, true)
        list.forEach {
            it.forEach {
                fr.write(it)
            }
        }
        fr.flush()
        fr.close()
    }

    //创建相关文件信息
    private fun initFiles() {
        val path = Environment.getExternalStorageDirectory()
        fileImgDir = path.absolutePath + "/TestImg/"
        File(fileImgDir).check_mkdirs()

        filename = path.absolutePath + "/测试记录文件--${TimeUtils.getNowString()}.txt"
        val file = File(filename)
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    var num1 = 0L
    var num0 = 0L
    val call1 = object : IFrameCall {
        override fun call(bf: ByteBuffer, w: Int, h: Int) {
//                发送到算法库识别
            if (num0++ % 3 != 0L) return
            if (savaimage) {
                savaimage = false
                val bytes = ByteArray(bf.capacity())
                bf.get(bytes, 0, bytes.size)
                savaImg(bytes, "${TimeUtils.getNowString()}Color.jpg")
            }
        }
    }
    val call2 = object : IFrameCall {
        override fun call(bf: ByteBuffer, w: Int, h: Int) {
//                发送到算法库识别
            if (num1++ % 3 != 0L) return
            if (savaimage1) {
                savaimage1 = false
                val bytes = ByteArray(bf.capacity())
                bf.get(bytes, 0, bytes.size)
                savaImg(bytes, "${TimeUtils.getNowString()}Ir.jpg")
            }

        }
    }

    fun savaImg(byteArray: ByteArray, name: String) {
        GlobalScope.launch {
            try {
                var b = MyBitmapFactory.YUV420SPDataToBitmap(byteArray, 640, 480)
                var fileName = fileImgDir + name
                val targetFile = File(fileName)
                val outputStream = FileOutputStream(targetFile)
//                    bitmap = BitmapFactory.decodeByteArray(it.RGB24, 0, it.RGB24!!.size)
                b?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                    outputStream.write(it.RGB24)
                outputStream.flush()
                //确保图片保存后不会出现0kb
                outputStream.fd.sync()
                outputStream.close()
            } catch (e: Exception) {
            }

        }
    }

    //开始预览
    private suspend fun startpreview() {
        //获取设备列表
        if (CamerasMng.cameraList.size != 2) {
            delay(3000)
        }
        if (CamerasMng.cameraList.size != 2) {
            delay(3000)
        }
        CamerasMng.cameraList.forEach {
            Log.d("CamerasMng", "CamerasMng ${it.pid}")
            if (it.pid == 33073) {//33073
                c = it
                c?.setPreviewSize(cameraColorW, cameraColorH)
            } else if (it.pid == 25446) {
                c2 = it
                c2?.setPreviewSize(cameraIrW, cameraIrH)
            }
        }

        textureview.rotation = -90F
        textureview.setSurfaceTextureListener(object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureAvailable")
                MatrixView(textureview, width, height)
                c?.startPreview(surface, previewcall = call1)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureSizeChanged")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//                Log.d("----", "--   onSurfaceTextureUpdated")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("----", "--   onSurfaceTextureDestroyed")
//                c?.stopSecede()
                c?.stopPreview()
//                c?.destroyCamera()
                return true
            }
        })
        textureview2.rotation = -90F
        textureview2.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureAvailable")
                MatrixView(textureview2, width, height)
                c2?.startPreview(surface, previewcall = call2)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureSizeChanged")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                //                Log.d("----", "--   onSurfaceTextureUpdated")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("----", "--   onSurfaceTextureDestroyed")
//                c2?.stopSecede()
                c2?.stopPreview()
                //                c2?.destroyCamera()

                return true
            }
        }
    }


    //视图转换  镜像
    private fun MatrixView(textureView: TextureView, w: Int, h: Int) {
        val matrix = Matrix()
        textureView.getTransform(matrix)
        matrix.setScale(-1f, 1f)
        matrix.postTranslate(w.toFloat(), 0f)
        textureView.setTransform(matrix)
    }
}
