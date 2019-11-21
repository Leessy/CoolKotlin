package com.leessy.ofm1000test

import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.blankj.utilcode.util.FileUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.leessy.KotlinExtension.onClick
import com.leessy.KotlinExtension.subscribeOnIo
import com.leessy.coolkotlin.BaseActivity
import com.leessy.coolkotlin.R
import com.leessy.ofm1000test.data.request.FinishData
import com.leessy.ofm1000test.data.request.InitData
import com.leessy.ofm1000test.data.request.Record
import com.leessy.ofm1000test.data.request.RequestDatas
import com.leessy.ofm1000test.data.respone.ResponeBean
import com.leessy.ofm1000test.mqtt.MqttBody
import com.leessy.ofm1000test.mqtt.MqttServer
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_ofm1000server.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.*
import android.content.Context.WIFI_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.net.wifi.WifiManager
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import com.trello.rxlifecycle2.RxLifecycle.bindUntilEvent
import android.content.Context
import com.thonnn.cubeengine.engineapi.EngineApiConfig


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

        getSN.onClick {
            getserverSN()
        }
        searchSN.onClick {
            searchserverSN()
        }
        Update.onClick {
            appUpdate()
        }

        initMqtt.onClick {
            initMqtt()
        }

        init.onClick {
            RetrofitHelper.apiService()
                .Init(
                    RequestBody.create(
                        MEDIA_TYPE_urlencoded,
                        "json=${gson.toJson(
                            RequestDatas(
                                InitData(
                                    Constans.Dev_type,
                                    Constans.name
                                )
                            )
                        )}"
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
            Observable.zip(RetrofitHelper.apiService().UploadFile(
                RequestBody.create(
                    MEDIA_TYPE_FILE,
                    file
                )
            ),
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
                    RetrofitHelper.apiService()
                        .Insert(RequestBody.create(MEDIA_TYPE_urlencoded, json))
                }
                .subscribe({
                    Log.d(TAG, "上传记录成功 :${gson.toJson(it)}")
                }, {
                    Log.d(TAG, "上传记录异常 :$it")
                })


        }

    }

    //检查软件更新
    private fun appUpdate() {
        RetrofitHelper.apiService()
            .CheckUpdate("F602A")
            .subscribeOnIo()
            .subscribe({
                Log.d(TAG, "appUpdate :${gson.toJson(it)}")
                Log.d(TAG, "download MD5:${it.body!!.md5}")

                RetrofitHelper.apiService()
                    .download(it.body!!.path!!)
                    .subscribeOnIo()
                    .subscribe({
                        var s = writeResponseBodyToDisk(
                            Environment.getExternalStorageDirectory().absolutePath + "/apkfile.apk",
                            it
                        )
                        Log.d(TAG, "download s:${s}")

                        Log.d(
                            TAG,
                            "download 下载后的 MD5:${FileUtils.getFileMD5ToString(Environment.getExternalStorageDirectory().absolutePath + "/apkfile.apk")}"
                        )

                    }, {
                        Log.d(TAG, "download e:${it}")
                    })
            }, {
                Log.d(TAG, "appUpdate :${it}")
            })

    }


    //查询sn
    private fun searchserverSN() {
//        deviceInfo	{"mac": "40-B0-76-80-9B-D4", "mac2": "50-B0-76-80-9B-B4"}
        var map = hashMapOf<String, String>()
        map.put("systemid2", "7989879")
        RetrofitHelper.apiService()
            .FindSn(gson.toJson(map))
            .subscribeOnIo()
            .subscribe({
                Log.d(TAG, "searchserverSN :${gson.toJson(it)}")
            }, {
                Log.d(TAG, "searchserverSN :${it}")
            })
    }


    //请求SN 上传测试记录
    private fun getserverSN() {
        // 取得WifiManager对象
        var mWifiManager =
            getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        mWifiManager.setWifiEnabled(true)
        var map = hashMapOf<String, String>()
        map.put("IMEI", DevUtil.getIMEI(this) + "")
        map.put("IMSI", DevUtil.getIMSI(this) + "")
        map.put("ICCID", DevUtil.getICCID(this) + "")
        map.put("MSISDN", DevUtil.getMSISDN(this) + "")
        map.put("WIFIMAC", DevUtil.getWIFIMac(this) + "")
        map.put("ETH0MAC", DevUtil.getETH0Mac(this) + "")
        map.put("SerialNumber", android.os.Build.SERIAL + "")
        map.put("UniquePsuedoID", DevUtil.getUniquePsuedoID() + "")

        val macAddress = android.provider
            .Settings.Secure.getString(this.getContentResolver(), "bluetooth_address")
        map.put("BluetoothMac", macAddress + "")

        Log.d(TAG, "getserverSN :${gson.toJson(map)}")

        RetrofitHelper.apiService()
            .TestReport(false, "F602A", gson.toJson(map))
            .subscribeOnIo()
            .subscribe({
                Log.d(TAG, "getserverSN :${gson.toJson(it)}")
            }, {
                Log.d(TAG, "getserverSN :${it}")
            })
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
                .Finish(RequestBody.create(MEDIA_TYPE_urlencoded, "json=$jsonhttp"))//？？？这样得类型处理
                .subscribe {
                    Log.d(TAG, "FinishData :${gson.toJson(it)}")
                }
        }
    }

    private val MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8")
    private val MEDIA_TYPE_FILE = MediaType.parse("file/*; charset=UTF-8")
    private val MEDIA_TYPE_urlencoded =
        MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")

    /**
     *保存文件
     * 454681607F28C9299EC50165CE4BEF57
     * 454681607f28c9299ec50165ce4bef57
     */
    private fun writeResponseBodyToDisk(path: String, body: ResponseBody): Boolean {
        Log.d(TAG, "download :开始下载")

        try {
            val futureStudioIconFile = File(path)
//            if (!futureStudioIconFile.exists()) {
//                futureStudioIconFile.createNewFile()
//            }
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(40960)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                var oldProgress = 0
                while (true) {
                    val read = inputStream!!.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream!!.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    val progress =
                        ((fileSizeDownloaded.toDouble() / fileSize.toDouble()) * 100).toInt()
                    if (oldProgress != progress) {
//                        mHandler.post { mCallBack.inProgress(progress) }
                        Log.d(TAG, "download down_progress :${progress}")
                    }

                    Log.d(
                        TAG,
                        "download down_progress :${fileSizeDownloaded}  总量：${fileSize} " +
                                " ${(fileSizeDownloaded.toFloat() / fileSize) * 100}%"
                    )

                    Thread.sleep(50)
                    oldProgress = progress
                }
                outputStream!!.flush()
                return true
            } catch (e: IOException) {
                return false
            } finally {
                if (inputStream != null) {
                    inputStream!!.close()
                }
                if (outputStream != null) {
                    outputStream!!.close()
                }
            }
        } catch (e: IOException) {
            return false
        }

    }
}
