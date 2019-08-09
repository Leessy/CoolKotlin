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

