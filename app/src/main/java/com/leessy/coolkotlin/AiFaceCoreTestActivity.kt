package com.leessy.coolkotlin

import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.opt.CompareListColor
import com.leessy.aifacecore.opt.DetectFace
import com.leessy.aifacecore.opt.FeatureGet
import com.leessy.aifacecore.opt.ImageColor
import com.aiface.uvccamera.camera.Camera
import com.aiface.uvccamera.camera.CamerasMng
import com.aiface.uvccamera.camera.IFrameCall
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class AiFaceCoreTestActivity : RxAppCompatActivity() {
    var c: Camera? = null
    var c2: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_face_core_test)
        Log.d("----", "**************************???")

        Observable.timer(5000, TimeUnit.MILLISECONDS, Schedulers.io())
            .compose(this.bindToLifecycle())
            .subscribe({
                finish()
            }, {
            })


        //获取设备列表
        CamerasMng.cameraList.forEach {
            if (it.pid == 37424) {//33073
                c = it
                c?.openCamera()
                c?.setPreviewSize(640, 480, max_fps = 25,frameType = 1)
            } else if (it.pid == 25446) {
//                c2 = it
            }
        }

        initAiFAce()//算法


        //点击开
        RxView.clicks(button).subscribe {
            //获取设备列表
            CamerasMng.cameraList.forEach {
                if (it.pid == 33073) {//8976
                    c = it
                    Log.d("----", "点击开``  ${textureview.isAvailable}")
                    c?.startPreview(textureview.surfaceTexture)
                } else if (it.pid == 25446) {
                    c2 = it
                    c2?.startPreview(textureview.surfaceTexture)
                }
            }
        }

        //点击关
        RxView.clicks(button2).subscribe {
            //            CamerasMng.cameraList.forEach { it.destroyCamera() }
            c?.destroyCamera()
        }
        //点击停止预览
        RxView.clicks(button3).subscribe {
            c?.stopPreview()
        }


        textureview.rotation = -90F
        textureview.setSurfaceTextureListener(object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                Log.d("----", "--   onSurfaceTextureAvailable")
                MatrixView(textureview, width, height)
                c?.startPreview(surface)
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
                Log.d("----", "--   onSurfaceTextureSizeChanged")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//                Log.d("----", "--   onSurfaceTextureUpdated")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("----", "--   onSurfaceTextureDestroyed")
                c?.stopSecede()
//                c?.stopPreview()
//                c?.destroyCamera()
                return true
            }
        })
        textureview2.rotation = -90F
        textureview2.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                Log.d("----", "--   onSurfaceTextureAvailable")
                MatrixView(textureview2, width, height)
                c2?.startPreview(surface)
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
                Log.d("----", "--   onSurfaceTextureSizeChanged")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                //                Log.d("----", "--   onSurfaceTextureUpdated")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("----", "--   onSurfaceTextureDestroyed")
                c2?.stopSecede()
//                c2?.stopPreview()
                //                c2?.destroyCamera()

                return true
            }
        }

        test()//其他测试

    }

    var bbb: ByteArray? = null
    var num1 = 0L
    var num2 = 0L


    private fun initAiFAce() {
        c?.setFrameCall(object : IFrameCall {
            override fun call(bf: ByteBuffer, w: Int, h: Int) {
//                发送到算法库识别
                if (num1++ % 2 != 0L) return
                val bytes = ByteArray(bf.capacity())
                bf.get(bytes, 0, bytes.size)
//                发送到算法库识别
                AiFaceCore.dataEmitter(bytes, ImageColor.COLOR, w, h, bMirror = 1, nRotate = 2)
            }
        })
        c2?.setFrameCall(object : IFrameCall {
            override fun call(bf: ByteBuffer, w: Int, h: Int) {
                if (num2++ % 2 != 0L) return
                val bytes = ByteArray(bf.capacity())
                bf.get(bytes, 0, bytes.size)
//                发送到算法库识别
                AiFaceCore.dataEmitter(bytes, ImageColor.IR, 640, 480, bMirror = 1, nRotate = 2)
            }
        })

        //人脸框数据处理
        AiFaceCore.FollowFaceRect(imageColor = ImageColor.COLOR)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .observeOn(Schedulers.newThread())
            .map {
                it.apply {
                    rect = RectF(
                        it.nFaceLeft.toFloat(),
                        it.nFaceTop.toFloat(),
                        it.nFaceRight.toFloat(),
                        it.nFaceBottom.toFloat()
                    )
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //                Log.d("---- 人脸框", "${it.imageColor}  ${it.nFaceBottom}")
                //画彩色人脸框或者红外人脸框
                when (it.imageColor) {
                    ImageColor.COLOR ->
                        faceview.setFaces(it.rect, 480, 640)
                    ImageColor.IR ->
                        faceview2.setFaces(it.rect, 480, 640)
                }

            }, {

            })


        //彩色人脸数据处理
        AiFaceCore.Follows(ImageColor.COLOR)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .DetectFace()
            .observeOn(Schedulers.newThread())
            .sample(200, TimeUnit.MILLISECONDS)
            .FeatureGet()
//            .CompareListColor()
//            .DetectFace_Feature()
            .subscribe({
                Log.d("----", "-*----------彩色 人脸个数  ${it.faceNum}   Thread=${Thread.currentThread().name}")
                Log.d("----", "-*----------彩色 检测时间     ${it.testTime_face}")
                Log.d("----", "-*----------彩色 特征时间     ${it.testTime_feature}")
            }, {
                Log.d("----", "-*----------彩色 异常了     ${it}")
            })

        //红外人脸数据处理
        AiFaceCore.Follows(ImageColor.IR)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .DetectFace()

//            .observeOn(Schedulers.newThread())
//            .sample(200, TimeUnit.MILLISECONDS)
//            .FeatureGet()
//            .DetectFace_Feature()

            .subscribe({
                Log.d("----", "-*----------红外 人脸个数  ${it.faceNum}   Thread=${Thread.currentThread().name}")
                Log.d("----", "-*----------红外 检测时间     ${it.testTime_face}")
                Log.d("----", "-*----------红外 特征时间     ${it.testTime_feature}")
            }, {
                Log.d("----", "-*----------彩色 异常了     ${it}")
            })
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
