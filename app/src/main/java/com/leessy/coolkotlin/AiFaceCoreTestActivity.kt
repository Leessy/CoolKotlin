package com.leessy.coolkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AndroidException
import android.util.Log
import com.AiChlFace.AiChlFace
import com.AiChlIrFace.AiChlIrFace
import com.AiFace.AiFace
import com.AiIrFace.AiIrFace
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.AiFaceCore.AiFaceType
import com.leessy.aifacecore.AiFaceCore.Compare.ComparList
import com.leessy.aifacecore.AiFaceCore.IAiFaceInitCall
import com.leessy.aifacecore.opt.DetectFace
import com.leessy.aifacecore.opt.FeatureGet
import com.leessy.aifacecore.opt.ImageColor
import com.leessy.camera.Camera
import com.leessy.camera.CamerasMng
import com.leessy.camera.IFrameCall
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class AiFaceCoreTestActivity : AppCompatActivity() {
    var c: Camera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_face_core_test)

//        AiFaceCore.initAiFace(
//            application, AiFaceType.MODE_DEBUG, object : IAiFaceInitCall {
//                override fun call(colorsInit: Boolean, irInit: Boolean) {
//                    Log.d("----", " colors=$colorsInit   irInit=$irInit")
//                    Log.d("----", "thread  name222 =${Thread.currentThread().name}")
//
//                    Log.d("----", "thread``  ${AiChlFace.FeatureSize()}")
//                    Log.d("----", "thread``  ${AiChlIrFace.FeatureSize()}")
//                    Log.d("----", "thread``  ${AiFace.AiFaceFeatureSize()}")
//                    Log.d("----", "thread``  ${AiIrFace.AiFaceFeatureSize()}")
//                }
//            }
//        )

        CamerasMng.initCameras(application)

        RxView.clicks(button).subscribe {
            CamerasMng.cameraList.forEach {
                if (it.pid == 33073) {
                    c = it
                    var s = c?.setPreviewSize(640, 480)
                }
                it.startPreview(w = 480, h = 640)
            }
        }

        RxView.clicks(button2).subscribe {
            CamerasMng.cameraList.forEach { it.stopPreview() }
        }

        initCamera()
    }

    private fun initCamera() {
        c?.setFrameCall(object : IFrameCall {
            override fun call(bf: ByteBuffer, w: Int, h: Int) {
//                发送到算法库识别
                AiFaceCore.dataEmitter(byteArrayOf(), w, h, 0, ImageColor.COLOR)
            }
        })

        //人脸框数据处理
        AiFaceCore.FollowFaceRect()
            .subscribe {

            }

        //人脸数据处理
        AiFaceCore.Follows(CameraID = 2)
            .DetectFace()
            .FeatureGet()
            .subscribe {

            }

    }
}
