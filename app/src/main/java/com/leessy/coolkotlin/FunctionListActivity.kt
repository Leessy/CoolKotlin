package com.leessy.coolkotlin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.PowerManager
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.blankj.utilcode.util.ShellUtils
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.F602SystemTool
import com.leessy.LED
import com.leessy.Loaction.LoactionActivity
import com.leessy.PowerManagerUtil
import com.leessy.util.SystemRotation
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_function_list.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 * 功能测试列表页面
 */
class FunctionListActivity : RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_list)
        ledTest()
    }

    //F602 灯光测试
    private fun ledTest() {
        RxView.clicks(w_led).subscribe {
            F602SystemTool.openLed(LED.IR_LED_LIGHT)
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
    }

    fun showbar(view: View) {
        sendBroadcast(Intent("android.intent.action.SHOW_NAVIGATION_BAR"))
    }

    fun hidebar(view: View) {
        sendBroadcast(Intent("android.intent.action.HIDE_NAVIGATION_BAR"))
    }

    fun location(view: View) {
        startActivity(Intent(this, LoactionActivity::class.java))
    }

    fun ischarging(view: View) {
        //是否充电状态
        var batteryBroadcast =
            registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        // 0 means we are discharging, anything else means charging
        //0表示我们在放电，其它都表示在充电
        var ret = batteryBroadcast.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        Log.d("****", "isCharging = " + ret)
        var isCharging = batteryBroadcast.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) != 0
        BatteryManager.BATTERY_PLUGGED_USB//usb充电
        BatteryManager.BATTERY_PLUGGED_AC//电源适配器充电
        Toast.makeText(this, "充电状态 :" + ret + "   (0：没有充电，1：电源充电，2：usb接口充电)", Toast.LENGTH_SHORT)
            .show()
    }

    fun Backlightup(view: View) {
        window.apply {
            attributes = attributes.apply {
                screenBrightness = 0.9F
            }
        }
    }

    fun Backlightlow(view: View) {
        window.apply {
            attributes = attributes.apply {
                screenBrightness = 0.1F
            }
        }
    }


    //进入锁屏状态  --没唤醒按键  默认5秒后唤醒
    fun gotosleep(view: View) {
        PowerManagerUtil.goToSleep(this)
        Observable.timer(5, TimeUnit.SECONDS)
            .subscribe {
                var pm = getSystemService(Context.POWER_SERVICE) as PowerManager
                //如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
                if (!pm?.isScreenOn!!)
                    PowerManagerUtil.wakeUp(application)
            }
    }

    fun UsbPowerOff(view: View) = F602SystemTool.usbPowerOff()
    fun UsbPowerOn(view: View) = F602SystemTool.usbPowerOn()
    fun UsbPowerreset(view: View) = F602SystemTool.restUsb()
    fun SystemRotation270(view: View) =
        SystemRotation.setRotation(SystemRotation.ROTATION_270, this)

    fun SystemRotation180(view: View) =
        SystemRotation.setRotation(SystemRotation.ROTATION_180, this)

    fun SystemRotation90(view: View) = SystemRotation.setRotation(SystemRotation.ROTATION_90, this)
    fun SystemRotation0(view: View) = SystemRotation.setRotation(SystemRotation.ROTATION_0, this)
    fun getSystemRotation(view: View) {
        val angle = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).getDefaultDisplay()
            .getRotation()
        Toast.makeText(this, "当前方向 :" + angle, Toast.LENGTH_SHORT).show()
    }

    var inductionDisposable: Disposable? = null
    fun objectInduction(view: View) {
        inductionDisposable?.let { it.dispose() }
        inductionDisposable = F602SystemTool.induction()
            .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var i = it.trim().toInt()
                objectInductionText.text = "人体感应：$i"
            }, {
                Log.d("----", "人体感应触发erro：${it}")
            })
    }


    fun aifaceICtest(view: View) {
        var f = File("dev/test_chip")
        if (!f.exists()) {
            //没有权限写入文件
//            f.createNewFile()
//            Log.d("----", "测试文件不存在 写入文件  test_chip")
////            val cmd = ShellUtils.execCmd("chmod -R 777 /dev/test_chip", true)
////            Log.d("----", "修改dev目录权限 ret=$cmd")
//            //读写assets目录下的文件
//            val inputs: InputStream = resources.assets.open("test_chip")
//            var fo = FileOutputStream(f)
//            fo.write(inputs.readBytes())
//            inputs.close()
//            fo.flush()
//            fo.close()
            Toast.makeText(this, "找不到测试文件 :test_chip", Toast.LENGTH_SHORT).show()
            return
        }
        val s = ShellUtils.execCmd("dev/test_chip", false)
        Log.d("----", "textdm2016：${s.toString()}")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("结果")
        builder.setMessage(s.successMsg + "--" + s.errorMsg)
        builder.create().show()
    }


    var dismantleDisposable: Disposable? = null
    fun dismantle(view: View) {
        dismantleDisposable?.let { it.dispose() }
        dismantleDisposable = F602SystemTool.dismantle()
            .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                var i = it.trim().toInt()
                dismantleText.text = "防拆开关：${if (i == 0) "松开" else "按下"}"
            }, {
                Log.d("----", "防拆开关 err：${it}")
            })
    }

    fun IO3ON(view: View) = F602SystemTool.writeSysFile(F602SystemTool.gpio3, "1")
    fun IO3OFF(view: View) = F602SystemTool.writeSysFile(F602SystemTool.gpio3, "0")
    fun IO2ON(view: View) = F602SystemTool.writeSysFile(F602SystemTool.gpio2, "1")
    fun IO2OFF(view: View) = F602SystemTool.writeSysFile(F602SystemTool.gpio2, "0")

}