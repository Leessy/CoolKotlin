package com.leessy.ofm1000test.mqtt

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType

/**
 * @author Created by 刘承. on 2019/6/12
 * business@onfacemind.com
 */
class MqttServer {
    var context: Context? = null

    var connetion: MyServiceConnection? = null
    var gson = Gson()
//    private var AIFACEREDUCED by Preference(Constant.VERIFY_THRESHOLD_KEY, Constant.AiFaceReduced)

//    fun getMqttObservable(): Observable<MqttBody> {
//        return mqttDataToBen(subject)
//    }

    //初始化
    fun initServer(
        context: Application,
        ip: String,
        port: String,
        user: String,
        password: String,
        topic: String,
        clientId: String
    ) {
        this.context = context
        if (ip.isEmpty() || port.isEmpty() || user.isEmpty() || password.isEmpty() || topic.isEmpty() || clientId.isEmpty()) {
            throw Exception("MQTT 参数不能为空！")
        }
        connetion = MyServiceConnection().apply {
            setIGetMessageCallBack(object : IGetMessageCallBack {
                override fun setMessage(s: String, messageId: Int) {
                    Log.d(TAG, "msg=$s   id=$messageId")
                    subject.onNext(MqttMsg(s, messageId))
                }
            })
            setConnectStatus {
                connetion?.mqttService?.initParameter(arrayListOf<String>(ip, port, user, password, topic, clientId))
            }
        }
        context.bindService(
            Intent(context, MQTTService::class.java), connetion, Context.BIND_AUTO_CREATE
        )
        initFaceDataParse()
    }

    //人脸数据解析(人脸注册  +  人脸更新)
    private fun initFaceDataParse() {
        subject.subscribe {
            Log.d(TAG, "$it ")
        }
    }


//    //mqtt数据解密  解析json
//    private fun mqttDataToBen(o: Observable<MqttMsg>): Observable<MqttBody> {
//        return o
//            .map {
//                var ds = AESUtils.decrypt(it.data, REGISTER_TOKEN)
//                var data: MqttBody? = null
//                try {
//                    data = gson.fromJson(ds, MqttBody::class.java)
//                } catch (e: Exception) {
//                    print("mqtt 数据解析错误 $e")
//                }
//                if (data == null) {
//                    data = MqttBody()
//                }
//                data.apply { msgId = it.id }
//            }
//    }


    companion object {
        private val TAG = javaClass.name
        val instance: MqttServer  by lazy { MqttServer() }
        private val MEDIA_TYPE = MediaType.parse("text/plain; charset=UTF-8")
        val subject = PublishSubject.create<MqttMsg>()
    }

    class MqttMsg(var data: String, var id: Int)

}
