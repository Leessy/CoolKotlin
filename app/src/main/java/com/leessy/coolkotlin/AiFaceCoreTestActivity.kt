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
import com.leessy.camera.CamerasMng
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AiFaceCoreTestActivity : AppCompatActivity() {

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
            CamerasMng.cameraList.forEach { it.startPreview() }
        }

        RxView.clicks(button2).subscribe {
            CamerasMng.cameraList.forEach { it.stopPreview() }
        }

    }
}
