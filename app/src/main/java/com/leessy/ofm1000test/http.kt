package com.leessy.ofm1000test

import android.util.Log
import com.leessy.KotlinExtension.observeOnMain
import com.leessy.KotlinExtension.subscribeOnIo
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 *
 * @author Created by 刘承. on 2019/8/1
 *
 * --深圳市尚美欣辰科技有限公司.
 */
object http {
    private val TAG = javaClass.name + "********"
    private val MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8")
    private val MEDIA_TYPE_FILE = MediaType.parse("image/png")

    //上传记录
    fun upload() {
        RetrofitHelper.apiService().Insert(
            RequestBody.create(
                MEDIA_TYPE_JSON, "{\n" +
                        "    \"namespace\": \"f9527\",\n" +
                        "    \"name\": \"heartbeat\",\n" +
                        "    \"sn\": \"SNVSJDG0000001\",\n" +
                        "    \"apiVersion\": \"0.0.2\",\n" +
                        "    \"body\": {\n" +
                        "        \"value\": [\n" +
                        "            {\n" +
                        "                \"time\": \"2017-08-09 12:00:00\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }\n" +
                        "}"
            )
        )
            .subscribeOnIo()
            .subscribe({
                Log.d(TAG, "1 ${it}")
            }, {
                Log.d(TAG, "2 $it")
            })
    }


}