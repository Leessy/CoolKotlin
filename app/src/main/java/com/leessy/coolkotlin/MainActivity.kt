package com.leessy.coolkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.Loaction.LoactionActivity
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.AiFaceCore.AiFaceType
import com.leessy.aifacecore.AiFaceCore.IAiFaceInitCall
import com.aiface.uvccamera.camera.CamerasMng
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import com.leessy.*
import com.leessy.KotlinExtension.*
import com.leessy.ofm1000test.ofm1000testActivity


class MainActivity : RxAppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        RxView.clicks(functionList1).observeOn(AndroidSchedulers.mainThread())
//                .subscribe { startActivity(Intent(this, ScrollingActivity::class.java)) }
//
//        RxView.clicks(fullscreenactivity).observeOn(AndroidSchedulers.mainThread())
//                .subscribe { startActivity(Intent(this, FullscreenActivity::class.java)) }
//
//        RxView.clicks(basec).observeOn(AndroidSchedulers.mainThread())
//                .subscribe { startActivity(Intent(this, BaseActivity::class.java)) }
//
//        RxView.clicks(ListDemo).observeOn(AndroidSchedulers.mainThread())
//                .subscribe { startActivity(Intent(this, ListDemoActivity::class.java)) }
//
//        RxView.clicks(MySQLBt).observeOn(AndroidSchedulers.mainThread())
//                .subscribe { startActivity(Intent(this, MySQLActivity::class.java)) }
//
//
//        RxView.clicks(coroutine).observeOn(AndroidSchedulers.mainThread())
//                .subscribe { startActivity(Intent(this, CoroutineActivity::class.java)) }
//
//
//        RxView.clicks(xcrash).observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    startActivity(Intent(this, XcrashTestActivity::class.java))
//                }
        RxView.clicks(loaction).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, LoactionActivity::class.java))
            }

        RxView.clicks(AiFaceCoreTest).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isAuto = true
                startActivity(Intent(this, AiFaceCoreTestActivity::class.java))
            }

        RxView.clicks(ofm1000test).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isAuto = true
                startActivity(Intent(this, ofm1000testActivity::class.java))
            }


        //灯光测试---------------------------
        RxView.clicks(w_led).subscribe {
            F602SystemTool.openLed(LED.WHITE_LED_LIGHT)
        }
        RxView.clicks(w_ir).subscribe {
            F602SystemTool.openLed(LED.IR_LED_LIGHT)
        }
        RxView.clicks(led_r).subscribe {
            F602SystemTool.openLed(LED.RED_LED)
        }
        RxView.clicks(led_g).subscribe {
            F602SystemTool.openLed(LED.GRE_LED)
        }
        RxView.clicks(led_b).subscribe {
            F602SystemTool.openLed(LED.BLUE_LED)
        }
        RxView.clicks(colse).subscribe {
            F602SystemTool.closeAll()
        }
        //灯光测试---------------------------

        var window = getWindow()
        //背景亮
        RxView.clicks(bg_u).subscribe {
            var lp = window.getAttributes()
//            lp.screenBrightness = 1F
            window.setAttributes(lp)

        }
        //背景暗
        RxView.clicks(bg_d).subscribe {
            //            var lp = window.getAttributes()
//            lp.screenBrightness = 0F
//            window.setAttributes(lp)
//            goToSleep()
            App.app.restartApp(application)
        }


        CamerasMng.initCameras(application)


        Log.d("----", "厂商     ${android.os.Build.BRAND}")
        Log.d("----", "厂商     ${android.os.Build.MODEL}")

        AiFaceCore.initAiFace(
            application, AiFaceType.MODE_DM2016, object : IAiFaceInitCall {
                override fun call(colorsInit: Boolean, irInit: Boolean) {
                    Log.d("----", "算法初始化    $colorsInit   $irInit")
                    GlobalScope.launch(Dispatchers.Main) {
                        Toast.makeText(application, "算法初始化    $colorsInit   $irInit", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )

        ObjectInduction()
//        PowerManagerUtil.wakeUp(application)
        Log.d("----", ":onCreate ")

        pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn = pm?.isScreenOn//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
    }

    var pm: PowerManager? = null
    /**
     *   关闭屏幕 ，其实是使系统休眠
     *
     */
    public fun goToSleep() {
        PowerManagerUtil.goToSleep(this)
    }

    var isAuto = false


    override fun onResume() {
        super.onResume()
        Log.d("----", ":onResume ")
        if (isAuto) {
            Observable.timer(2, TimeUnit.SECONDS, Schedulers.io()).compose(this.bindToLifecycle())
                .subscribe {
                    startActivity(Intent(this, AiFaceCoreTestActivity::class.java))
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("----", ":onDestroy ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("----", ":onPause ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("----", ":onStop ")
    }

    override fun onStart() {
        super.onStart()
        Log.d("----", ":onStart ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("----", ":onRestart ")
    }


    //人体感应触发
    private fun ObjectInduction() {
        F602SystemTool.ObjectInduction()
            .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("----", "人体感应触发：${it}   屏幕状态${pm?.isScreenOn}")
                var i = it.trim().toInt()
                if (i == 1) {
                    Log.d("----", "人体感应触发  wakeUp ")
                    if (!pm?.isScreenOn!!)
                        PowerManagerUtil.wakeUp(application)
                }
            }, {
                Log.d("----", "人体感应触发erro：${it}")
            })
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent?): Boolean {
        Log.d("----", "keyCode：${keyCode}")
        return super.onKeyMultiple(keyCode, repeatCount, event)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("----", "onKeyDown：${event?.keyCode}")
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("----", "onKeyUp：${event?.keyCode}")
        return super.onKeyUp(keyCode, event)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Log.d("----", "dispatchKeyEvent：${event?.keyCode}")
        return super.dispatchKeyEvent(event)

    }
}
