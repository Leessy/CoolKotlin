package com.leessy.ofm1000test

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leessy.KotlinExtension.onClick
import com.leessy.KotlinExtension.subscribeOnIo
import com.leessy.coolkotlin.BaseActivity
import com.leessy.coolkotlin.R
import com.leessy.ofm1000test.data.request.FinishData
import com.leessy.ofm1000test.data.request.InitData
import com.leessy.ofm1000test.data.request.RequestDatas
import com.leessy.ofm1000test.data.request.Record
import com.leessy.ofm1000test.data.respone.ResponeBean
import com.leessy.ofm1000test.mqtt.MqttBody
import com.leessy.ofm1000test.mqtt.MqttServer
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_ofm1000server.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File


class ofm1000ServerTest : BaseActivity() {
    private val TAG = javaClass.name
    val gson = Gson()

    val devSN = Constans.sn
    val typeName = Constans.Dev_type
    val host = "http://101.7040.ddns.thonnn.com:99/"//http服务器

    var isreply = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ofm1000server)



        initMqtt.onClick {
            initMqtt()
        }

        init.onClick {
            RetrofitHelper.apiService()
                .Init(
                    RequestBody.create(
                        MEDIA_TYPE_urlencoded,
                        "json=${gson.toJson(RequestDatas(InitData(Constans.Dev_type, Constans.name)))}"
                    )
                )
                .subscribeOnIo()
                .subscribe({
                    Log.d(TAG, "Initdata  :${gson.toJson(it)}")

                }, {
                    Log.d(TAG, "Initdata er :${it}")
                })
        }

        repart.onClick {
            isreply = !isreply
            Log.d(TAG, "当前 isreply :${isreply}")
        }

        upload.onClick {
            //先把记录要上传得图片上传,成功后再把图片地址放到记录上传
            val file = File("/storage/emulated/0/F602A/RecordImg/1358500317695.jpg")
            Observable.zip(RetrofitHelper.apiService().UploadFile(RequestBody.create(MEDIA_TYPE_FILE, file)),
                RetrofitHelper.apiService().UploadFile(RequestBody.create(MEDIA_TYPE_FILE, file)),
                BiFunction<ResponeBean<String>, ResponeBean<String>, ArrayList<String>> { t1, t2 ->
                    ArrayList<String>().apply {
                        add(if (t1.errcode == 0) t1.body.toString() else "")
                        add(if (t2.errcode == 0) t2.body.toString() else "")
                    }
                })
                .map {
                    val json = "json=${RequestDatas(Record(image1 = it[0], image2 = it[1]).apply {
                        name = "NAME"
                        nation = "1"
                        id_card = "112"
                    })}"
                    RetrofitHelper.apiService().Insert(RequestBody.create(MEDIA_TYPE_urlencoded, json))
                }
                .subscribe({
                    Log.d(TAG, "上传记录成功 :${gson.toJson(it)}")
                }, {
                    Log.d(TAG, "上传记录异常 :$it")
                })


        }

    }

    private fun initMqtt() {
        MqttServer.instance.initServer(
            application,
            "47.112.124.234",
            "1883",
            "user",
            "123456",
            "cmdReceiver_$devSN",
            "EngineClient_${typeName}_$devSN"
        )
        MqttServer.subject.subscribe {
            Log.d(TAG, "MQTT 源数据 :${gson.toJson(it)}")
            val type = object : TypeToken<MqttBody<MqttBody.Body>>() {}.type
            var mqttbody = gson.fromJson<MqttBody<MqttBody.Body>>(it.data, type)
            Log.d(TAG, "MQTT 解析后数据 JSON :${gson.toJson(mqttbody)}")


            //应答HTTP应答处理
            if (!isreply) return@subscribe
            var jsonhttp = gson.toJson(RequestDatas(FinishData(mqttbody.cmid, "完成cmd")))
            Log.d(TAG, "http 应答数据 json:${jsonhttp}")
            RetrofitHelper.apiService()
                .Finish(RequestBody.create(MEDIA_TYPE_urlencoded, "json=$jsonhttp"))//？？？这样得类型处理 蛋疼
                .subscribe {
                    Log.d(TAG, "FinishData :${gson.toJson(it)}")
                }
        }
    }

    private val MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8")
    private val MEDIA_TYPE_FILE = MediaType.parse("file/*; charset=UTF-8")
    private val MEDIA_TYPE_urlencoded = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
}
