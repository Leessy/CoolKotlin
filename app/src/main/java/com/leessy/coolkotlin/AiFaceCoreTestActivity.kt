package com.leessy.coolkotlin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.TextureView
import com.AiChlFace.AiChlFace
import com.AiChlFace.FACE_DETECT_RESULT
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.opt.DetectFace_Feature
import com.leessy.aifacecore.opt.ImageColor
import com.leessy.camera.Camera
import com.leessy.camera.CamerasMng
import com.leessy.camera.IFrameCall
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class AiFaceCoreTestActivity : RxAppCompatActivity() {
    var c: Camera? = null
    var c2: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_face_core_test)
//
//        Observable.timer(4, TimeUnit.SECONDS)
//            .compose(this.bindToLifecycle())
//            .subscribe { finish() }

        //获取设备列表
        CamerasMng.cameraList.forEach {
            if (it.pid == 33073) {
                c = it
            } else if (it.pid == 25446) {
                c2 = it
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


        textureview.setSurfaceTextureListener(object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                Log.d("----", "--   onSurfaceTextureAvailable")
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
                c?.stopPreview()
                return true
            }
        })
        textureview2.setSurfaceTextureListener(object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                Log.d("----", "--   onSurfaceTextureAvailable")
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
                c2?.stopPreview()
                return true
            }
        })

        test()//其他测试

    }

    override fun onStop() {
        super.onStop()
        c?.destroyCamera()
        c2?.destroyCamera()
    }

    var bbb: ByteArray? = null
    var num1 = 0L
    var num2 = 0L


    private fun initAiFAce() {
        c?.setFrameCall(object : IFrameCall {
            override fun call(bf: ByteBuffer, w: Int, h: Int) {
//                发送到算法库识别
                if (num1++ % 6 != 0L) return
                val bytes = ByteArray(bf.capacity())
                bf.get(bytes, 0, bytes.size)
//                发送到算法库识别
                AiFaceCore.dataEmitter(bytes, w, h, 0, ImageColor.COLOR, nRotate = 2)
            }
        })
        c2?.setFrameCall(object : IFrameCall {
            override fun call(bf: ByteBuffer, w: Int, h: Int) {
                if (num2++ % 6 != 0L) return
                val bytes = ByteArray(bf.capacity())
                bf.get(bytes, 0, bytes.size)
//                发送到算法库识别
//                AiFaceCore.dataEmitter(bytes, w, h, 0, ImageColor.IR, nRotate = 2)
            }
        })

        //人脸框数据处理
        AiFaceCore.FollowFaceRect()
            .subscribe {

            }

        //彩色人脸数据处理
        AiFaceCore.Follows(ImageColor.COLOR)
            .compose(this.bindToLifecycle())
//            .DetectFace()
//            .FeatureGet()
            .DetectFace_Feature()
            .subscribe {
                Log.d(
                    "----",
                    "\n-----------------------------------------------" +
                            "\n 彩色 人脸个数${it.faceNum} " +
                            "\n 检测时间 ${it.testTime_face} " +
                            "\n 特征时间=${it.testTime_feature} " +
                            "\n Thread= ${Thread.currentThread().name}"
                )
            }

        //红外人脸数据处理
        AiFaceCore.Follows(ImageColor.IR)
            .compose(this.bindToLifecycle())
//            .DetectFace()
//            .FeatureGet()
            .DetectFace_Feature()
            .subscribe {
                Log.d(
                    "----",
                    "\n-----------------------------------------------" +
                            "\n 红外 人脸个数${it.faceNum} " +
                            "\n 检测时间 ${it.testTime_face} " +
                            "\n 特征时间=${it.testTime_feature} " +
                            "\n Thread= ${Thread.currentThread().name}"
                )
            }
    }

    private fun test() {
        /////////////////////test//////////////////////
        val file = try {
            File(Environment.getExternalStorageDirectory(), "aaaa.jpg")
        } catch (e: Exception) {
            return
        }
        if (!file.isFile) return
        val bmp: Bitmap? = BitmapFactory.decodeFile(file.absolutePath) ?: return
        bbb = (YUV420SP_Util.BitmapToYUV420SP(bmp) ?: return)

        Observable.interval(10, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe {
                if (bbb != null) {
                    val test0 = System.currentTimeMillis()
                    val RGB24 = ByteArray(640 * 480 * 3)
                    val ww = IntArray(1)
                    val hh = IntArray(1)
                    val detectResult = FACE_DETECT_RESULT()
                    val n = AiChlFace.DetectFaceEx(
                        2, 2, bbb, 480, 640, 0, 0, 0, 0,
                        0, 0, RGB24, ww, hh, detectResult
                    )
                    Log.d(
                        "-----",
                        "人脸  $n    时间= ${System.currentTimeMillis() - test0}   thread ${Thread.currentThread().name}"
                    )
                }
            }


//        Observable.interval(10, TimeUnit.MILLISECONDS)
//            .observeOn(Schedulers.computation())
//            .subscribe {
//                val test0 = System.currentTimeMillis()
//                val b = ByteArray(640 * 480 * 3)
//                for (i in 0 until 10) {
//                    b.indices.forEach {
//                        b[it] = 10
//                    }
//                }
//                Log.d(
//                    "----", "***  时间= ${System.currentTimeMillis() - test0}   thread ${Thread.currentThread().name}"
//                )
//            }
    }
}
