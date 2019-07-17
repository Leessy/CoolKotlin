package com.leessy

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.concurrent.TimeUnit

object F602SystemTool {
    //502机器
    private val GPIO0_PATH = "/sys/bus/platform/devices/gpio_port/gpioport/irled"//红外补光
    private val ledred = "/sys/bus/platform/devices/gpio_port/gpioport/ledred"//红色灯
    private val ledgre = "/sys/bus/platform/devices/gpio_port/gpioport/ledgre"//绿
    private val dsled3 = "/sys/bus/platform/devices/gpio_port/gpioport/dsled3"//白光补光灯2（dsled3）
    private val dsled2 = "/sys/bus/platform/devices/gpio_port/gpioport/dsled2"//白光补光灯1（dsled2）

    //602机器
    private val irled602 = "/sys/devices/platform/gpioport/gpioport/ir_led"//红外 602
    private val dsled602 = "/sys/devices/platform/gpioport/gpioport/ds_led_flash"// 白色补光 602
    private val redled602 = "/sys/devices/platform/gpioport/gpioport/led_blue"//红led
    private val greled602 = "/sys/devices/platform/gpioport/gpioport/led_gre"//绿灯
    private val blueled602 = "/sys/devices/platform/gpioport/gpioport/led_red"//蓝灯

    private val induction: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }
    var readDis: Disposable? = null//读文件控制器

    //写文件
    private fun writeSysFile(sys_path: String, value: String) {
        try {
            var bufWriter: BufferedWriter? = null
            bufWriter = BufferedWriter(FileWriter(sys_path))
            bufWriter.write(value)  // 写操作
            bufWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    //开始  结束
    fun startRead(b: Boolean) {
        if (b) {
            readDis?.let { it.dispose() }
            readDis = Observable.interval(100, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe({
                    val o = FileReader("/sys/devices/platform/gpioport/gpioport/mandet")
                    induction.onNext(o.readText())
                    o.close()
                }, {
                    induction.onNext("0")//读文件异常
                })
        } else {
            readDis?.dispose()
        }
    }

    //开放感应接口
    fun ObjectInduction(): Observable<String> {
        return induction.distinctUntilChanged { s, s2 ->
            s == s2
        }.doOnSubscribe {
            startRead(true)
        }.doOnDispose {
            startRead(false)
        }.doFinally {
        }
    }

    /**
     * 开灯
     */
    fun openLed(vararg type: LED) {
        type.forEach {
            writeSysFile(
                when (it) {
                    LED.IR_LED_LIGHT -> irled602
                    LED.WHITE_LED_LIGHT -> dsled602
                    LED.BLUE_LED -> {
                        closeLed(LED.RED_LED, LED.GRE_LED)
                        blueled602
                    }
                    LED.GRE_LED -> {
                        closeLed(LED.RED_LED, LED.BLUE_LED)
                        greled602
                    }
                    LED.RED_LED -> {
                        closeLed(LED.BLUE_LED, LED.GRE_LED)
                        redled602
                    }
                }, "1"
            )
        }
    }

    //关灯
    fun closeLed(vararg type: LED) {
        type.forEach {
            writeSysFile(
                when (it) {
                    LED.IR_LED_LIGHT -> irled602
                    LED.WHITE_LED_LIGHT -> dsled602
                    LED.RED_LED -> redled602
                    LED.GRE_LED -> greled602
                    LED.BLUE_LED -> blueled602
                }, "0"
            )
        }
    }


    //全部关闭
    fun closeAll() {
        closeLed(
            LED.IR_LED_LIGHT,
            LED.WHITE_LED_LIGHT,
            LED.BLUE_LED,
            LED.RED_LED,
            LED.GRE_LED
        )
    }

}

enum class LED {
    IR_LED_LIGHT,
    WHITE_LED_LIGHT,
    RED_LED,
    GRE_LED,
    BLUE_LED,
}
