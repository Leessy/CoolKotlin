package com.leessy.coolkotlin

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.AiChlFace.AiChlFace
import com.aiface.uvccamera.camera.Camera
import com.aiface.uvccamera.camera.CamerasMng
import com.aiface.uvccamera.camera.IFrameCall
import com.leessy.KotlinExtension.onClick
import com.leessy.MyBitmapFactory
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.datas.isLivings
import com.leessy.aifacecore.datas.isNoneFace
import com.leessy.aifacecore.opt.*
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class AiFaceCoreTestActivity : RxAppCompatActivity() {
    var c: Camera? = null
    var c2: Camera? = null

    var cameraColorW = 1280
    var cameraColorH = 720
    //    var cameraColorW = 1920
//    var cameraColorH = 1080
    var cameraIrW = 640
    var cameraIrH = 480
//    var cameraIrW = 1920
//    var cameraIrH = 1080

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_face_core_test)
        Log.d("----", "**************************???")
        val w = intent.getIntExtra("w", 0)
        val h = intent.getIntExtra("h", 0)
        if (w != 0 && h != 0) {
            cameraColorW = w
            cameraIrW = w
            cameraColorH = h
            cameraIrH = h
        }
        Observable.timer(4 * 1000, TimeUnit.MILLISECONDS)
            .compose(this.bindToLifecycle())
            .subscribe({
                finish()
            }, {
            })

        //获取设备列表
        CamerasMng.cameraList.forEach {
            Log.d("CamerasMng", "CamerasMng pid ${it.pid}")
            if (it.pid == 3) {//33073
                c = it
                c?.openCamera()
                c?.setPreviewSize(cameraColorW, cameraColorH, max_fps = 25)
            } else if (it.pid == 1) {//22594   25446
                c2 = it
                c2?.openCamera()
                c2?.setPreviewSize(cameraIrW, cameraIrH, max_fps = 25)
            }
        }

        initAiFAce()//算法

//        val d = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(d)
//        d.widthPixels
//        Log.d("----", "get w ${d.widthPixels} ")
//        Log.d("----", "get h ${d.heightPixels} ")
//
//        Log.d("----", "get 111 ${windowManager.defaultDisplay.width} ")
//        Log.d("----", "get 111 ${windowManager.defaultDisplay.height} ")
//
//        val p = frame1.layoutParams as ViewGroup.MarginLayoutParams
////            val p = FrameLayout.LayoutParams(m)
//        p.width = 1812
//        p.height = 1024
//        frame1.layoutParams = p
//        textureview.onClick {
//        }
//        textureview.rotation = -90F
        textureview.setSurfaceTextureListener(object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surfaceTexture: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureAvailable$width  $height")
                MatrixView(textureview, width, height)
                c?.startPreview(surfaceTexture)
                c?.startPreview(previewTexture = surfaceTexture,
                    surface = null,
                    w = 640,
                    h = 480,
                    max_fps = 25,
                    frameType = Camera.FRAME_FORMAT_MJPEG,
                    previewcall = object : IFrameCall {
                        override fun call(bf: ByteBuffer, w: Int, h: Int) {
                        }
                    })
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureSizeChanged$width  $height")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//                Log.d("----", "--   onSurfaceTextureUpdated")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("----", "--   onSurfaceTextureDestroyed")
//                c?.stopSecede()
                c?.stopPreview()
                c?.destroyCamera()
                return true
            }
        })
//        textureview2.rotation = -90F
        textureview2.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureAvailable")
                MatrixView(textureview2, width, height)
                c2?.startPreview(surface)
                c2?.startPreview()
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

        test()//其他测试

    }

    var bbb: ByteArray? = null
    var num1 = 0L
    var num2 = 0L

    var fc = object : IFaceFitersCall {
        override fun call(type: AiFaceFilter) {
            Log.d("----", "彩色 人脸过滤 未通过类型 $type")
        }
    }

    private fun initAiFAce() {
        c?.setFrameCall(object : IFrameCall {
            override fun call(bf: ByteBuffer, w: Int, h: Int) {
//                Log.d("----", "彩色 相机数据ByteBuffer ${bf.capacity()}  $w   $h")

                //                发送到算法库识别
                if (num1++ % 3 == 0L) {
                    val bytes = ByteArray(bf.capacity())
                    bf.get(bytes, 0, bytes.size)
                    //发送到算法库识别
                    AiFaceCore.dataEmitter(
                        bytes,
                        ImageColor.COLOR,
                        cameraColorW,
                        cameraColorH,
                        CameraID = 0,
                        stream = 2,
                        bMirror = 1,
                        nRotate = 0
                    )

                    if (num1 == 4L) {
                        try {
                            val p = Environment.getExternalStoragePublicDirectory("DCIM").toString()
                            var fileName = System.currentTimeMillis().toString() + ".jpg"
                            val targetFile = File(p, fileName)
                            val outputStream = FileOutputStream(targetFile)

                            var bitmap =
                                YUV420SP_Util.JUV420SPDataToBitmap(
                                    bytes,
                                    cameraColorW,
                                    cameraColorH
                                )
//                    var bitmap =
//                        MyBitmapFactory.createMyBitmap(it.RGB24, cameraColorH, cameraColorW)
                            bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

                            outputStream.flush()
                            //确保图片保存后不会出现0kb
                            outputStream.fd.sync()
                            outputStream.close()
                            Log.d("---", "保存图片完成 ");
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

            }
        })
        c2?.setFrameCall(object : IFrameCall {
            override fun call(bf: ByteBuffer, w: Int, h: Int) {
//                Log.d("----", "黑白 相机数据ByteBuffer ${bf.capacity()}  $w   $h")

                if (num2++ % 3 == 0L) {
                    val bytes = ByteArray(bf.capacity())
                    bf.get(bytes, 0, bytes.size)
//                发送到算法库识别
                    AiFaceCore.dataEmitter(
                        bytes,
                        ImageColor.IR,
                        cameraIrW,
                        cameraIrH,
                        bMirror = 1,
                        nRotate = 2
                    )
                    if (num2 == 4L) {
                        try {
                            val p = Environment.getExternalStoragePublicDirectory("DCIM").toString()
                            var fileName = System.nanoTime().toString() + ".jpg"
                            val targetFile = File(p, fileName)
                            val outputStream = FileOutputStream(targetFile)

                            var bitmap =
                                YUV420SP_Util.JUV420SPDataToBitmap(
                                    bytes,
                                    cameraColorW,
                                    cameraColorH
                                )
//                    var bitmap =
//                        MyBitmapFactory.createMyBitmap(it.RGB24, cameraColorH, cameraColorW)
                            bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

                            outputStream.flush()
                            //确保图片保存后不会出现0kb
                            outputStream.fd.sync()
                            outputStream.close()
                            Log.d("---", "保存图片完成 ");
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

            }
        })

        //人脸框数据处理
        AiFaceCore.FollowFaceRect(imageColor = ImageColor.COLOR)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .observeOn(Schedulers.newThread())
            .map {
                it.apply {
                    //                    if (!isNoneFace()) {
//                        FaceFilterCalculate(16, 250, 500, 30, 0, 85)
//                    }
                    rect = RectF(
                        it.nFaceLeft.toFloat(),
                        it.nFaceTop.toFloat(),
                        it.nFaceRight.toFloat(),
                        it.nFaceBottom.toFloat()
                    )
                }
            }
            .FaceFilterCalculate(16, 250, 500, 30, 0, 85)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("---- 人脸框  过滤值=", "${it.faceFilterRet} ")
                //画彩色人脸框或者红外人脸框
                when (it.imageColor) {
                    ImageColor.COLOR ->
//                        faceview.setFaces(it.rect, 480, 640)
                        faceview.setFaces(it.rect, it.width, it.height)
//                        faceview.setFaces(it.rect, cameraColorH, cameraColorW)
                    ImageColor.IR ->
//                        faceview2.setFaces(it.rect, 480, 640)
                        faceview2.setFaces(it.rect, cameraIrH, cameraIrW)
                }

            }, {

            })

        //红外人脸数据处理
        AiFaceCore.Follows(ImageColor.IR, CameraID = 0)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .sample(200, TimeUnit.MILLISECONDS)
//            .DetectFace()
            .DetectFaceAndFilter()
            .LivingsSinglePass()
            .FeatureGet()

//            .filter { it.isLivings() }
//            .Livings()
//            .observeOn(Schedulers.io())
//            .sample(200, TimeUnit.MILLISECONDS)
//            .FeatureGet()
//            .DetectFace_Feature()
            .subscribe({
                Log.d(
                    "----",
                    "-*----------红外 人脸个数  ${it.faceNum}   Thread=${Thread.currentThread().name}"
                )
                Log.d("----", "-*----------红外 检测时间     ${it.testTime_face}")
                Log.d("----", "-*----------红外 特征时间     ${it.testTime_feature}")
                Log.d("----", "-*----------红外 活体结果     ${it.Livings}")

            }, {
                Log.d("----", "-*----------红外 异常了     ${it}")
            })

        startColorAiface()
    }

    var ss: Disposable? = null
    fun startColorAiface() {
        //彩色人脸数据处理
        ss?.dispose()
        ss = AiFaceCore.Follows(ImageColor.COLOR)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
//            .sample(300, TimeUnit.MILLISECONDS)
            .DetectFaceAndFilter()
//            .filter {
//                Log.d("----", "彩色 过滤faceAngleFilter(10)  ${it.faceAngleFilter(10)}")
//                Log.d("----", "彩色 过滤faceWidthFilter(150)  ${it.faceWidthFilterMin(150)}")
//                Log.d("----", "彩色 过滤faceWidthFilter(150)  ${it.faceWidthFilterMax(350)}")
//                Log.d("----", "彩色 过滤faceQualityFilter(85)  ${it.faceQualityFilter(85)}")
//                Log.d("----", "彩色 过滤ffaceEdgeFilter(50,60)  ${it.faceEdgeFilter(50,60)}")
//                it.faceAngleFilter(10)
//                        && it.faceWidthFilterMin(200)
//                        && it.faceQualityFilter(85)
//            }
//            .faceFilter(15, 150, 350, 50, 60,85, fc)
//            .faceOffsetFilter(0.08F)
            .observeOn(Schedulers.io())
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .sample(200, TimeUnit.MILLISECONDS)
            .LivingsSinglePass()
            .filter { it.isLivings() }
            .FeatureGet()
//            .CompareListColor()
//            .DetectFace_Feature()
            .subscribe({
                Log.d(
                    "----",
                    "-*----------彩色 人脸个数  ${it.faceNum}   Thread=${Thread.currentThread().name}"
                )
                Log.d("----", "-*----------彩色 检测时间     ${it.testTime_face}")
                Log.d("----", "-*----------彩色 特征时间     ${it.testTime_feature}")
                Log.d("----", "-*----------彩色 活体结果     ${it.Livings}")
                it.faceResult.nAnglePitch
//                try {
//                    var fileName = System.currentTimeMillis().toString() + ".jpg"
//                    val targetFile = File("/storage/emulated/0/DCIM", fileName)
//                    val outputStream = FileOutputStream(targetFile)
//
//                    var bitmap = MyBitmapFactory.createMyBitmap(it.RGB24, cameraColorH, cameraColorW)
////                    bitmap = BitmapFactory.decodeByteArray(it.RGB24, 0, it.RGB24!!.size)
//                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
////                    outputStream.write(it.RGB24)
//                    outputStream.flush()
//                    //确保图片保存后不会出现0kb
//                    outputStream.fd.sync()
//                    outputStream.close()
//                } catch (e: FileNotFoundException) {
//                    e.printStackTrace()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }

            }, {
                Log.d("----", "-*----------彩色 异常了     ${it}")
            })

//        Observable.timer(10, TimeUnit.SECONDS).subscribe {
//            startColorAiface()
//        }

    }

    //视图转换  镜像
    private fun MatrixView(textureView: TextureView, w: Int, h: Int) {
        val matrix = Matrix()
        textureView.getTransform(matrix)
        //                matrix.postRotate(90, i / 2, i1 / 2);//绕某个点旋转90度，这里选择的原点是图片的中心点
        //                matrix.setSinCos(1, 0, i / 2, i1 / 2);//把图像旋转90度，那么90度对应的sin和cos分别是1和0。
        matrix.setScale(-1f, 1f)
        matrix.postTranslate(w.toFloat(), 0f)
        textureView.setTransform(matrix)
    }

    private fun test() {
        /////////////////////test//////////////////////
//        val file = try {
//            File(Environment.getExternalStorageDirectory(), "aaaa.jpg")
//        } catch (e: Exception) {
//            return
//        }
//        if (!file.isFile) return
//        val bmp: Bitmap? = BitmapFactory.decodeFile(file.absolutePath) ?: return
//        bbb = (YUV420SP_Util.BitmapToYUV420SP(bmp) ?: return)
//
//        Observable.interval(10, TimeUnit.MILLISECONDS)
//                .observeOn(Schedulers.io())
//                .subscribe {
//                    if (bbb != null) {
//                        val test0 = System.currentTimeMillis()
//                        val RGB24 = ByteArray(640 * 480 * 3)
//                        val ww = IntArray(1)
//                        val hh = IntArray(1)
//                        val detectResult = FACE_DETECT_RESULT()
//                        val n = AiChlFace.DetectFaceEx(
//                                2, 2, bbb, 480, 640, 0, 0, 0, 0,
//                                0, 0, RGB24, ww, hh, detectResult
//                        )
//                        Log.d(
//                                "-----",
//                                "人脸  $n    时间= ${System.currentTimeMillis() - test0}   thread ${Thread.currentThread().name}"
//                        )
//                    }
//                }


//        Observable.interval(10, TimeUnit.MILLISECONDS)
//                .observeOn(Schedulers.computation())
//                .subscribe {
//                    val test0 = System.currentTimeMillis()
//                    val b = IntArray(640 * 480 * 3)
//                    b.indices.forEach {
//                        b[it] = it
//                    }
//                    b.forEach {
//                        val s = (it * 2.0F).toInt()
//                        val s1 = (it * 3.1415926412 * 2.0F).toInt()
//                        val s3 = (it * 3.1415926412 * 2.1110001110F).toInt()
//                    }
//                    Log.d(
//                            "----", "***  时间= ${System.currentTimeMillis() - test0}   thread ${Thread.currentThread().name}"
//                    )
//                }
    }
}
