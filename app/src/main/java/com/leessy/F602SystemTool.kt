package com.leessy

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.concurrent.TimeUnit

object F602SystemTool {
    //502机器
//    private val GPIO0_PATH = "/sys/bus/platform/devices/gpio_port/gpioport/irled"//红外补光
//    private val ledred = "/sys/bus/platform/devices/gpio_port/gpioport/ledred"//红色灯
//    private val ledgre = "/sys/bus/platform/devices/gpio_port/gpioport/ledgre"//绿
//    private val dsled3 = "/sys/bus/platform/devices/gpio_port/gpioport/dsled3"//白光补光灯2（dsled3）
//    private val dsled2 = "/sys/bus/platform/devices/gpio_port/gpioport/dsled2"//白光补光灯1（dsled2）

    //602机器
    private val irled602 = "/sys/devices/platform/gpioport/gpioport/ir_led"//红外 602
    private val dsled602 = "/sys/devices/platform/gpioport/gpioport/ds_led_flash"// 白色补光 602

    private val redled602 = "/sys/devices/platform/gpioport/gpioport/led_blue"//红led
    private val greled602 = "/sys/devices/platform/gpioport/gpioport/led_gre"//绿灯
    private val blueled602 = "/sys/devices/platform/gpioport/gpioport/led_red"//蓝灯
//    private val DISMANTLE_KEY = "/sys/devices/platform/gpioport/gpioport/forbid"     //防拆
//    private val INDUCTION_KEY = "/sys/devices/platform/gpioport/gpioport/mandet"     //感应

    //防拆
    val DISMANTLE_KEY = "/sys/bus/platform/devices/gpioport/gpioport/forbid"

    //人体感应
    val INDUCTION_KEY = "/sys/bus/platform/devices/gpioport/gpioport/mandet"
    val gpio1 = "/sys/bus/platform/devices/gpioport/gpioport/gpio1"
    val gpio2 = "/sys/bus/platform/devices/gpioport/gpioport/gpio2"
    val gpio3 = "/sys/bus/platform/devices/gpioport/gpioport/gpio3"
    val gpio4 = "/sys/bus/platform/devices/gpioport/gpioport/gpio4"

    val hubrst = "/sys/bus/platform/devices/gpioport/gpioport/hubrst"


    //感应主题
    private val induction: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }

    private var readDisInduction: Disposable? = null//读感应文件控制器
    private var readDisDismantle: Disposable? = null//读防拆文件控制器


    //防拆主题
    private val dismantle: PublishSubject<String> by lazy {
        PublishSubject.create<String>()
    }

    //写文件
    fun writeSysFile(sys_path: String, value: String) {
        try {
            var bufWriter: BufferedWriter? = null
            bufWriter = BufferedWriter(FileWriter(sys_path))
            bufWriter.write(value)  // 写操作
            bufWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun usbPowerOff() {
        GlobalScope.launch {
            writeSysFile(
                hubrst,
                "0"
            )
        }
    }

    fun usbPowerOn() {
        GlobalScope.launch {
            writeSysFile(
                hubrst,
                "1"
            )
        }
    }

    fun restUsb() {
        GlobalScope.launch {
            writeSysFile(
                hubrst,
                "0"
            )
            delay(800)
            writeSysFile(
                hubrst,
                "1"
            )
        }
    }


    //开始 结束 读取感应数据
    private fun startInductionRead(b: Boolean) {
        if (b) {
            readDisInduction?.let { it.dispose() }
            readDisInduction = Observable.interval(100, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe({
                    val o = FileReader(INDUCTION_KEY)
                    induction.onNext(o.readText())
                    o.close()
                }, {
                    induction.onNext("0")//读文件异常
                })
        } else {
            readDisInduction?.dispose()
        }
    }

    //开始 结束 读取防拆数据读取
    private fun startDismantleRead(b: Boolean) {
        if (b) {
            readDisDismantle?.let { it.dispose() }
            readDisDismantle = Observable.interval(1000, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe({
                    val o = FileReader(DISMANTLE_KEY)
                    dismantle.onNext(o.readText())
                    o.close()
                }, {
                    dismantle.onNext("0")//读文件异常
                })
        } else {
            readDisDismantle?.dispose()
        }
    }

    /**
     * 活体感应监听器
     */
    fun induction(): Observable<String> {
        return induction.distinctUntilChanged { s, s2 -> s == s2 }
//            .skip(1)
            .doOnSubscribe {
                startInductionRead(true)
            }.doOnDispose {
                startInductionRead(false)
            }.doFinally {
            }
    }

    /**
     * 防拆开关状态监听器
     */
    fun dismantle(): Observable<String> {
        return dismantle
            .doOnSubscribe {
                startDismantleRead(true)
            }.doOnDispose {
                startDismantleRead(false)
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
                        closeLed(
                            LED.RED_LED,
                            LED.GRE_LED
                        )
                        blueled602
                    }
                    LED.GRE_LED -> {
                        closeLed(
                            LED.RED_LED,
                            LED.BLUE_LED
                        )
                        greled602
                    }
                    LED.RED_LED -> {
                        closeLed(
                            LED.BLUE_LED,
                            LED.GRE_LED
                        )
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
