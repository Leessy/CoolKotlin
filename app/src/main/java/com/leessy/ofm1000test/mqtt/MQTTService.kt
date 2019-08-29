package com.leessy.ofm1000test.mqtt

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttException
import java.util.concurrent.TimeUnit


class MQTTService : Service() {
    private var conOpt: MqttConnectOptions? = null

    private var host = "tcp://192.168.1.5:1883"
    private var userName = "test"
    private var passWord = "test"
    private var clientId = "20190422135600010001"//客户端标识
    private var myTopic = "/dev/20190422135600010001"      //要订阅的主题


    private var IGetMessageCallBack: IGetMessageCallBack? = null

    internal var red: Disposable? = null

    // MQTT监听并且接受消息
    private val mqttCallback = object : MqttCallbackExtended {
        override fun messageArrived(topic: String?, message: MqttMessage?) {
            Log.i(TAG, "messageArrived${Thread.currentThread().name}")
            Log.i(TAG, "messageArrived_isDuplicate${message?.isDuplicate}")
            Log.i(TAG, "messageArrived$topic  messageArrived:$message")
        }

        override fun connectionLost(cause: Throwable?) {
            Log.i(TAG, "connectionLost")
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {

            Log.i(TAG, "deliveryComplete${token?.isComplete}")
        }

        override fun connectComplete(reconnect: Boolean, serverURI: String?) {
            Log.i(TAG, "connectComplete  连接成功  ")
            subscribeAllTopics()
        }
    }

    /**
     * 判断网络是否连接
     */
    private val isConnectIsNormal: Boolean
        get() {
            val connectivityManager = this.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetworkInfo
            if (info != null && info.isAvailable) {
                val name = info.typeName
                Log.i(TAG, "MQTT当前网络名称：$name")
                return true
            } else {
                Log.i(TAG, "MQTT 没有可用网络")
                return false
            }
        }

    override fun onCreate() {
        super.onCreate()
        Log.e(javaClass.name, "onCreate")

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(javaClass.name, "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    fun initParameter(strings: ArrayList<String>) {
        if (strings != null && strings.size == 6) {
            host = "tcp://${strings[0]}:${strings[1]}"
            userName = strings[2]
            passWord = strings[3]
            myTopic = strings[4]
            clientId = strings[5]
            Log.i(
                TAG, "MQTT Server 参数 host：$host  " +
                        "userName$userName  passWord$passWord  " +
                        "myTopic$myTopic  clientId$clientId"
            )
        }
        //TODO 可以根据传入参数校验是否变动， 有变动的情况close server 再重新连接
        init()
    }

    private fun init() {
        if (client != null) return
        // 服务器地址（协议+地址+端口号）
        val uri = host
//        client = MqttAndroidClient(this, uri, clientId)
        client = MqttClient(uri, clientId, null)
        // 设置MQTT监听并且接受消息
        client!!.setCallback(mqttCallback)

        conOpt = MqttConnectOptions()
        //开启自动重连
        conOpt!!.isAutomaticReconnect = true
        // 清除缓存
//        conOpt!!.isCleanSession = false
        // 设置超时时间，单位：秒
        conOpt!!.connectionTimeout = 10
        // 心跳包发送间隔，单位：秒
        conOpt!!.keepAliveInterval = 20
        // 用户名
        conOpt!!.userName = userName
        // 密码
        conOpt!!.password = passWord.toCharArray()//将字符串转换为字符串数组

        // last will message
        var doConnect = true
        val message = "offline"//"{\"terminal_uid\":\"$clientId\"}"
        val topic = "EngineClient_Will"
        Log.e(javaClass.name, "遗嘱 主题:$topic  message:$message")
        val qos = 0
//        如果 publish消息的retain标记位被设置为1，则称该消息为“保留消息”
//        Broker对保留消息的处理Broker会存储每个Topic的最后一条保留消息及其Qos，当订阅该Topic的客户端上线后，Broker需要将该消息投递给它。
//        publish消息时，如果retain值是true，则服务器会一直记忆，哪怕是服务器重启。
        val retained = false
        try {
            conOpt!!.setWill("EngineClient_Will", message.toByteArray(), qos, retained)//设置遗嘱 主题  msg
        } catch (e: Exception) {
            Log.i(TAG, "Exception Occured", e)
            doConnect = false
            reConnect()
        }
        if (doConnect) {
            doClientConnection()
        }

    }

    fun subscribeAllTopics() {
        Schedulers.io().scheduleDirect {
            try {
//                client!!.subscribe(myTopic, 2)
                client!!.subscribe(myTopic, 2) { topic, message ->
                    message?.let {
                        val str1 = String(it.payload)
                        if (IGetMessageCallBack != null && str1.isNotEmpty()) {
                            IGetMessageCallBack!!.setMessage(str1, it.id)
                        }
                    }
                }
            } catch (ex: MqttException) {
                Log.d(TAG, "subscribeToTopic: Exception whilst subscribing")
                ex.printStackTrace()
            }
        }

    }

    override fun onDestroy() {
        stopSelf()
        try {
            client!!.disconnect()
            client!!.close()
        } catch (e: MqttException) {
            e.printStackTrace()
        }

        super.onDestroy()
    }

    /**
     * 连接MQTT服务器
     */
    private fun doClientConnection() {
        Schedulers.io().scheduleDirect {
            if (!client!!.isConnected && isConnectIsNormal) {
                try {
                    client!!.connect(conOpt)
                } catch (e: MqttException) {
                    e.printStackTrace()
                }
            }
        }

    }

    // 连接失败，重连
    private fun reConnect() {
        if (red != null) {
            red!!.dispose()//防止多次计时启动
        }
        red = Observable.timer(8, TimeUnit.SECONDS)
            .subscribe { aLong -> doClientConnection() }
    }


    override fun onBind(intent: Intent): IBinder? {
        Log.e(javaClass.name, "onBind")
        return CustomBinder()
    }

    fun setIGetMessageCallBack(IGetMessageCallBack: IGetMessageCallBack) {
        this.IGetMessageCallBack = IGetMessageCallBack
    }

    inner class CustomBinder : Binder() {
        val service: MQTTService
            get() = this@MQTTService
    }

    companion object {
        val TAG = MQTTService::class.java.simpleName

        private var client: MqttClient? = null
//        private var client: MqttAndroidClient? = null

//        //发布主题消息
//        fun publish(msg: String) {
//            val topic = myTopic
//            val qos = 0
//            val retained = false
//            try {
//                if (client != null) {
//                    client!!.publish(topic, msg.toByteArray(), qos, retained)
//                }
//            } catch (e: MqttException) {
//                e.printStackTrace()
//            }

    }
//}
}