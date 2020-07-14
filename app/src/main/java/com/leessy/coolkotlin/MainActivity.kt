package com.leessy.coolkotlin

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.AiChlFace.AiChlFace
import com.aiface.uvccamera.camera.CamerasMng
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ShellUtils
import com.jakewharton.rxbinding2.view.RxView
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
import com.leessy.util.SystemRotation
import com.leessy.webviewjs.WebViewJsActivity
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

        sendBroadcast(Intent("android.intent.action.SHOW_NAVIGATION_BAR"))

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

        CamerasMng.initCameras(application)
//        initAiFace()
    }

    private fun testRar() {
        Schedulers.io().scheduleDirect {
            var l = System.currentTimeMillis()
            RarUtil.unzipFileByKeyword(
                File("/storage/emulated/0/aifacelibs.zip"),
                File("/storage/emulated/0/aifacelibs"), null
            )
//            RarUtil.unrar("/storage/emulated/0/aifacelibs.zip","/storage/emulated/0/aifacelibs")
            Log.d("----", "解压文件耗时    ${System.currentTimeMillis() - l}")
        }

    }

    private fun initAiFace() {
        var l = System.currentTimeMillis()
        //这个方法目标目录其他文件都删了。。。。。。
//        FileUtils.copyDir("/storage/emulated/0/AiFaceLib/",getCacheDir().parent+"/lib/")
        Log.d("----", "算法初始化 拷贝文件  nativeLibraryDir  ${getCacheDir().parent}")
        var file = File("/storage/emulated/0/AiFaceLib/")
        for (listFile in file.listFiles()) {
            var des = File(getCacheDir().parent + "/lib/" + listFile.name)
//            if (des.exists()&&des.isFile){
//                Log.d("----", "算法初始化 拷贝文件  文件存在  ${des.name}")
//                continue
//            }
            Log.d(
                "----",
                "算法初始化 拷贝文件  listFile  ${listFile.absolutePath}    des${des.absolutePath}"
            )
            var s = FileUtils.copyFile(listFile, des)
            Log.d("----", "算法初始化 拷贝文件  listFile 结果 $s")
        }
        Log.d("----", "算法初始化 拷贝文件耗时    ${System.currentTimeMillis() - l}")


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

    fun functiontest(view: View) {
        startActivity(Intent(this, FunctionListActivity::class.java))
    }

    fun cameratest(view: View) {
        startActivity(Intent(this, CameraTestListActivity::class.java))
    }

    fun aifacetest(view: View) {
        startActivity(Intent(this, AifaceListTestActivity::class.java))
    }

    fun interfacetest(view: View) {
        startActivity(Intent(this, ApiTestActivity::class.java))
    }

    fun hardwaretest(view: View) {
        startActivity(Intent(this, HardwareTestActivity::class.java))
    }

    fun SdkFuncation(view: View) {
        startActivity(Intent(this, OpenSdkTestActivity::class.java))
    }


}
