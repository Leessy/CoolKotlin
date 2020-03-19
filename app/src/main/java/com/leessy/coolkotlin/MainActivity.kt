package com.leessy.coolkotlin

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.PowerManager
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Toast
import com.AiChlFace.AiChlFace
import com.aiface.jidacard.MainCardActivity
import com.aiface.uvccamera.camera.CamerasMng
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ShellUtils
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.ActionBroadCast
import com.leessy.CardTest.YxCardTestActivity
import com.leessy.F501ATest.F501ATestActivity
import com.leessy.F602SystemTool
import com.leessy.KotlinExtension.onClick
import com.leessy.LED
import com.leessy.Loaction.LoactionActivity
import com.leessy.PowerManagerUtil
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.AiFaceCore.AiFaceType
import com.leessy.aifacecore.AiFaceCore.IAiFaceInitCall
import com.leessy.mediarecord.MediaRecordActivity
import com.leessy.ofm1000test.ofm1000ServerTest
import com.leessy.util.RarUtil
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileReader
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : RxAppCompatActivity(), CoroutineScope by MainScope() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var p = Runtime.getRuntime().exec("su")
//        var s = "chmod 777 /dev/video0\n"
//        var pr = p.getOutputStream()
//        pr.write(s.toByteArray())
//        pr.flush()
//        pr.write("exit\n".toByteArray())
//        pr.flush()
//        pr.close()

        sendBroadcast(Intent("android.intent.action.SHOW_NAVIGATION_BAR"))
//        sendBroadcast(Intent("android.intent.action.HIDE_NAVIGATION_BAR"))

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
//                startActivity(Intent(this, AiFaceCoreTestActivity::class.java))
                startaifacecore()
            }

        RxView.clicks(ofm1000test).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, ofm1000ServerTest::class.java))
            }
        RxView.clicks(F501Test).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, F501ATestActivity::class.java))
            }
        RxView.clicks(presentationCamera).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, PresentationCameraActivity::class.java))
            }
        records.onClick {
            startActivity(Intent(this, MediaRecordActivity::class.java))
        }


        ocr.onClick {
            //            startActivity(Intent(this, YxCardTestActivity::class.java))
//            startActivity(Intent(this, MainCardActivity::class.java))
//            startActivity(Intent(this, OcrActivity::class.java))
//            var came = Camera.open(0)
//            var came1 = Camera.open(1)
//            var came2 = Camera.open(2)

//            Log.d("****camera", " " + came)
//            Log.d("****camera", " " + came1)
//            Log.d("****camera", " " + came2)

//            initAiFace()
            testRar()

//            val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//            Log.d("****camera", "size :${cameraManager.cameraIdList.size} ")
//
//            cameraManager.cameraIdList.forEachIndexed { index, s ->
//                Log.d("****camera", "ID===${s} ")
//            }
//
//            Log.d(
//                "****camera",
//                " " + getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)
//            )

//
//            startActivity(
//                Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
//                    putExtra("android.intent.extras.CAMERA_FACING_FRONT", 2)//前置摄像头
////                    putExtra("android.intent.extras.CAMERA_FACING", 2)//前置摄像头
//                })
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
            window.apply {
                attributes = attributes.apply {
                    screenBrightness = 0.9F
                }
            }
            launch {
                F602SystemTool.openLed(LED.GRE_LED)
                delay(200)
                F602SystemTool.closeAll()
                delay(200)
                F602SystemTool.openLed(LED.GRE_LED)
                delay(1000)
                F602SystemTool.openLed(LED.BLUE_LED)
            }
        }
        //背景暗
        RxView.clicks(bg_d).subscribe {
            //            window.apply {
//                attributes = attributes.apply {
//                    screenBrightness = 0.1F
//                }
//            }
//            goToSleep()

//            F602SystemTool.restUsb()

        }


        CamerasMng.initCameras(application)


//        ObjectInduction()
//        PowerManagerUtil.wakeUp(application)
        Log.d("----", ":onCreate ")

        pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn = pm?.isScreenOn//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。

//        initAiFace()
        textdm2016()
    }

    private fun testRar() {
        Schedulers.io().scheduleDirect {
            var l=System.currentTimeMillis()
            RarUtil.unzipFileByKeyword(File("/storage/emulated/0/aifacelibs.zip"),
                File("/storage/emulated/0/aifacelibs"),null)
//            RarUtil.unrar("/storage/emulated/0/aifacelibs.zip","/storage/emulated/0/aifacelibs")
            Log.d("----", "解压文件耗时    ${System.currentTimeMillis()-l}")
        }

    }

    private fun initAiFace() {
        var l=System.currentTimeMillis()
        //这个方法目标目录其他文件都删了。。。。。。
//        FileUtils.copyDir("/storage/emulated/0/AiFaceLib/",getCacheDir().parent+"/lib/")
        Log.d("----", "算法初始化 拷贝文件  nativeLibraryDir  ${getCacheDir().parent}")
        var file =File("/storage/emulated/0/AiFaceLib/")
        for (listFile in file.listFiles()) {
            var des= File( getCacheDir().parent+"/lib/"+listFile.name)
//            if (des.exists()&&des.isFile){
//                Log.d("----", "算法初始化 拷贝文件  文件存在  ${des.name}")
//                continue
//            }
            Log.d("----", "算法初始化 拷贝文件  listFile  ${listFile.absolutePath}    des${des.absolutePath}")
            var s= FileUtils.copyFile(listFile,des)
            Log.d("----", "算法初始化 拷贝文件  listFile 结果 $s")
        }
        Log.d("----", "算法初始化 拷贝文件耗时    ${System.currentTimeMillis()-l}")


          Log.d("----", "厂商     ${android.os.Build.BRAND}")
       Log.d("----", "厂商     ${android.os.Build.MODEL}")
       Log.d("----", "算法版本     ${AiChlFace.Ver()}")

       Log.d("----****", "cpunum=     ${AiChlFace.GetCpuNum()}")
    //        AiChlFace.SetFuncCpuNum(0, 2)
       //0-全部功能，1-人脸检测，2-特征提取，3-活体检测
       AiChlFace.SetFuncCpuNum(1, 1)
       AiChlFace.SetFuncCpuNum(2, 1)
       AiChlFace.SetFuncCpuNum(3, 1)

        //配置特征码版本（）
        AiFaceCore.isV10 = true
        AiFaceCore.initAiFace(
            application, AiFaceType.MODE_DM2016, object : IAiFaceInitCall {
                override fun call(colorsInit: Boolean, irInit: Boolean) {
                    Log.d("----", "算法初始化    $colorsInit   $irInit")
                    Log.d("----", "算法版本size     ${AiChlFace.FeatureSize()}")

                    GlobalScope.launch(Dispatchers.Main) {
                        //                        startActivity(Intent(this@MainCardActivity, FunctionTestActivity::class.java))
                        Toast.makeText(
                            application,
                            "算法初始化    $colorsInit   $irInit",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    var pm: PowerManager? = null
    /**
     *   关闭屏幕 ，其实是使系统休眠
     *
     */
    public fun goToSleep() {
        PowerManagerUtil.goToSleep(this)
    }

    //    var isAuto = true
    var isAuto = false


    fun startaifacecore() {
        val s = edittext.text.toString().trim()
        startActivity(Intent(this, AiFaceCoreTestActivity::class.java).apply {
            val wh = s.split("*")
            if (wh.size == 2) {
                try {
                    val w = wh[0].toInt()
                    val h = wh[1].toInt()
                    putExtra("w", w)
                    putExtra("h", h)
                } catch (e: Exception) {
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
//        GlobalScope.launch(Dispatchers.Main) {
//            delay(8000)
//            startActivity(Intent(this@MainCardActivity, FunctionTestActivity::class.java))
//        }
        Log.d("----", ":onResume ")
        if (isAuto) {
            Observable.timer(2, TimeUnit.SECONDS, Schedulers.io())
                .compose(this.bindToLifecycle())
                .subscribe {
                    //                    startActivity(Intent(this, AiFaceCoreTestActivity::class.java))
                    startaifacecore()
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
        Schedulers.io().scheduleDirect {
            while (true) {
                val o = FileReader(F602SystemTool.INDUCTION_KEY)
                Log.d("----", "读人体感应：${o.readText()}")
                o.close()
                Thread.sleep(200)
            }
        }
//        F602SystemTool.induction()
//            .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                Log.d("----", "人体感应触发：${it}   屏幕状态${pm?.isScreenOn}")
//                var i = it.trim().toInt()
//                if (i == 1) {
//                    Log.d("----", "人体感应触发  wakeUp ")
//                    if (!pm?.isScreenOn!!)
//                        PowerManagerUtil.wakeUp(application)
//                }
//            }, {
//                Log.d("----", "人体感应触发erro：${it}")
//            })
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("----", "onTouchEvent：${event?.pointerCount}")
        Log.d("----", "onTouchEvent：${event?.getSize(0)}")
        Log.d(
            "----",
            "onTouchEvent：${if (event?.pointerCount!! > 1) event?.getSize(1) else ""}"
        )
        return super.onTouchEvent(event)
    }

    fun textdm2016() {
//        var f = File("dev/test_chip1")
//        f.mkdirs()
//        f.outputStream().apply {
//            write("aaaaaaa".toByteArray())
//            flush()
//            close()
//        }

        var s = ShellUtils.execCmd("dev/test_chip", false)
        Log.d("----", "textdm2016：${s.toString()}")
    }

    @SuppressLint("NewApi")
    fun test(pi: PendingIntent, time: Long) {
        //AlarmReceiver.class为广播接受者
//        var intent = Intent(this, ActionBroadCast::class.java)
//        intent.putExtra("msg", "我是定时关机闹钟")
//        var pi = PendingIntent.getBroadcast(this, 0, intent, 0);

//        var intent = Intent(this, ofm1000ServerTest::class.java)
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        var pi = PendingIntent.getActivity(this, 0, intent, 0);

        var alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        //得到日历实例，主要是为了下面的获取时间
        var mCalendar = Calendar.getInstance()
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
//        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为13点
//        mCalendar.set(Calendar.HOUR_OF_DAY, 10);
        //设置在几分提醒  设置的为25分
//        mCalendar.set(Calendar.MINUTE, 55)
        //下面这两个看字面意思也知道
//        mCalendar.set(Calendar.SECOND, 0);
//        mCalendar.set(Calendar.MILLISECOND, 0);

        // ②设置AlarmManager在Calendar对应的时间启动Activity
//        alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);

//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi)// (1000 * 60 * 60 * 24)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            mCalendar.getTimeInMillis(),
            time,
            pi
        )// (1000 * 60 * 60 * 24)

        // 提示闹钟设置完毕:
        Toast.makeText(this, "闹钟设置完毕~" + mCalendar.getTimeInMillis(), Toast.LENGTH_SHORT).show()


        var a = alarmManager.nextAlarmClock

        if (a != null) {
            Log.d("----", "下一个alarmManager：${a.showIntent}")

            Log.d("----", "下一个alarmManager system：${System.currentTimeMillis()}")
            Log.d("----", "下一个alarmManager：${a.triggerTime}")
        }
    }

    fun cancle(pi: PendingIntent) {
        var alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pi)
    }


}
