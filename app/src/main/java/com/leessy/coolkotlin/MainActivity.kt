package com.leessy.coolkotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.AiFaceCore.AiFaceType
import com.leessy.aifacecore.AiFaceCore.IAiFaceInitCall
import com.leessy.camera.CamerasMng
import com.leessy.xCrash.XcrashTestActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MainActivity : RxAppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxView.clicks(functionList1).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, ScrollingActivity::class.java)) }

        RxView.clicks(fullscreenactivity).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, FullscreenActivity::class.java)) }

        RxView.clicks(basec).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, BaseActivity::class.java)) }

        RxView.clicks(ListDemo).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, ListDemoActivity::class.java)) }

        RxView.clicks(MySQLBt).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, MySQLActivity::class.java)) }


        RxView.clicks(coroutine).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, CoroutineActivity::class.java)) }


        RxView.clicks(xcrash).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, XcrashTestActivity::class.java))
            }

        RxView.clicks(AiFaceCoreTest).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isAuto = true
                startActivity(Intent(this, AiFaceCoreTestActivity::class.java))
            }



        CamerasMng.initCameras(application)

        AiFaceCore.initAiFace(
            application, AiFaceType.MODE_DEBUG, object : IAiFaceInitCall {
                override fun call(colorsInit: Boolean, irInit: Boolean) {
                    Log.d("----", "算法初始化    $colorsInit   $irInit")
                    GlobalScope.launch(Dispatchers.Main) {
                        Toast.makeText(application, "算法初始化    $colorsInit   $irInit", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }

    var isAuto = false

    override fun onResume() {
        super.onResume()
        if (isAuto) {
            Observable.timer(2, TimeUnit.SECONDS).compose(this.bindToLifecycle())
                .subscribe {
                    startActivity(Intent(this, AiFaceCoreTestActivity::class.java))
                }
        }
    }
}
