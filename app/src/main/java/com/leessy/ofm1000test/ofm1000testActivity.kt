package com.leessy.ofm1000test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.leessy.KotlinExtension.observeOnMain
import com.leessy.KotlinExtension.onClick
import com.leessy.coolkotlin.R
import com.leessy.ofm1000test.data.request.Requests
import com.leessy.ofm1000test.data.request.dataarray
import com.leessy.ofm1000test.data.request.uploadbody
import com.thonnn.cubeengine.engineapi.EngineApiConfig
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ofm1000test.*
import okhttp3.*
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit


class ofm1000testActivity : AppCompatActivity() {
    private val TAG = javaClass.name
    private val MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8")
    private val MEDIA_TYPE_FILE = MediaType.parse("image/png")

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ofm1000test)
        bitmapToBgr24()
        upload.onClick { ttttt2() }
    }


    fun init() {
        // 以下代码段在 F702 上测试通过，所有配置一旦执行过 init() 均不可再修改。具体可参考 javadoc
        try {
            EngineApiConfig.setNamespace("F702");   // 工程对接的应用名称，与[描述文件](#%E6%8F%8F%E8%BF%B0%E6%96%87%E4%BB%B6%E8%AF%B4%E6%98%8E)中 engine 节点的 namespace 字段对应
            EngineApiConfig.setDeviceName("asdf"); // 设备名称，此处的Config对象来自于F702设备安卓程序，具体请根据对应工程配置
            EngineApiConfig.setDeviceType("F702");   // 设备型号，此处的Config对象来自于F702设备安卓程序，具体请根据对应工程配置
            EngineApiConfig.setDeviceSn("123456");  // 设备序列号，设备出厂时使用[上传测试报告获取序列号](#%E4%B8%8A%E4%BC%A0%E6%B5%8B%E8%AF%95%E6%8A%A5%E5%91%8A%E8%8E%B7%E5%8F%96%E5%BA%8F%E5%88%97%E5%8F%B7)接口获取，此字段值在设备终生应保持不变。
            EngineApiConfig.setApiHostUri("http://192.168.18.101:7040/");   // 引擎服务器主机连接
//            EngineApiConfig.setUseMqtt(true)    // 启用 MQTT 通道获取指令，以下 MQTT 配置和 Context 配置仅针对此项为 true 生效
//            EngineApiConfig.setContext(getApplicationContext());    // 应用Context，用于使用SharedPreferences保存工程的一些记录信息
//            EngineApiConfig.setMqttUrl("tcp://47.112.124.234:1883");    // MQTT 服务器主机连接
//            EngineApiConfig.setMqttUsername("user");    // MQTT 连接用户名
//            EngineApiConfig.setMqttPasswd("123456");    // MQTT 连接密码
//            EngineApiConfig.setMqttTopicHead("cmdReceiver");    // MQTT 订阅主题头，需要与引擎服务器配置相同
//            EngineApiConfig.setMqttClientId("EngineClient_" + Config.NAME + "_" + Config.deviceSn); // MQTT 连接客户端 Id
//            EngineApiConfig.setMqttWillTopic("EngineClient_Will");  // 遗嘱主题
//            EngineApiConfig.setMqttWillMsg("offline");  // 遗嘱信息
//            EngineApiConfig.setMqttWillRetain(false);   // 遗嘱是否是 retain 的
//            EngineApiConfig.setMqttReceiver(new IMqttReceiver () {
//                // 设置 MQTT 监听接收器，根据回调内容，执行对应方法
//                @Override
//                public String callback(CommandMsgBean commandMsgBean) {
//                    SoutUtil.info(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + commandMsgBean.getMsgJson());
//                    if (commandMsgBean.getMsg().get("cmd").equals("addFace")) {
//                        // TODO 完成指令对应的任务
//                    }
//                    return "任务已完成"; // 当返回值不为 null 时，Api 会自动调用任务完成方法，上传任务完成请求
//                }
//            });
            EngineApiConfig.init(); // 执行初始化
        } catch (e: Exception) {
        }

    }


    fun ttttt2() {
        val builder = OkHttpClient().newBuilder()
        var http = builder.run {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            retryOnConnectionFailure(true) // 错误重连
        }.build()

        var body = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(testid().apply {
            id = "d342b417b9214c978430c5bc9ffad323"
        }))

        val form = FormBody.Builder() // 添加参数的键值对
            .add("id", "d342b417b9214c978430c5bc9ffad323").build()

        val r = Request.Builder()
            .url("http://221.214.13.10:6026/FaceSerCloud/api/jnga/extranet/face/v1/compare-no-card-query")
            .addHeader("API-Key", "OoYPS8UELSmvtehMxFu7CkFnh_JggZyw")
            .addHeader("API-Secret", "SUza4hTRRe1qagnXrwptuE8AEZmVbLYV")
            .post(form)
            .build()
        http.newCall(r).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "111111 ${e}")

            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "111111222 ${response}")
                Log.d(TAG, "111111222*** ${response.body()?.string()}")
            }
        })
    }

    fun ttt() {
        val builder = OkHttpClient().newBuilder()
        var http = builder.run {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
            retryOnConnectionFailure(true) // 错误重连
        }.build()

        var b = Base64Utils.bitmapToBase64(bmp!!)
        b = b?.replace("[\\s*\t\n\r]".toRegex(), "")

        LogUtils.d(b)

        var body = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(datass().apply {
            photo = b!!
        }))
        val r = Request.Builder()
            .url("http://221.214.13.10:6026/FaceSerCloud/api/jnga/extranet/face/v1/compare-uncard")
            .addHeader("API-Key", "OoYPS8UELSmvtehMxFu7CkFnh_JggZyw")
            .addHeader("API-Secret", "SUza4hTRRe1qagnXrwptuE8AEZmVbLYV")
            .post(body)
            .build()

        http.newCall(r).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "111111 ${e}")

            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "111111222 ${response}")
                Log.d(TAG, "111111222*** ${response.body()?.string()}")
            }
        })
    }


    var bmp: Bitmap? = null
    fun bitmapToBgr24() {
        var inputStream: InputStream? = null
        try {
            inputStream = this.resources.assets.open("aaaa.jpg")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        bmp = BitmapFactory.decodeStream(inputStream)
    }


    //上传记录
    private fun uploadrecord() {
        RetrofitHelper.apiService().Insert(
            RequestBody.create(
                MEDIA_TYPE_JSON, gson.toJson(Requests().apply {
                    namespace = "f9527"
                    name = "f9527"
                    sn = "SNVSJDG0000001"
                    apiVersion = "0.0.2"
                    body = dataarray().apply {
                        value = ArrayList(1)
                        value!!.add(uploadbody().apply {
                            name = "asdgfa"
                            sn = "132123"
                        })
                    }
                })
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOnMain()
            .subscribe({
                Log.d(TAG, "1 ${it}")
            }, {
                Log.d(TAG, "2 $it")
            })

    }
}

class testid {
    var id = ""
}

