package com.leessy.ofm1000test

import com.leessy.ofm1000test.data.respone.ResponeBean
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 *
 * @author Created by 刘承. on 2019/7/31
 *
 * --深圳市尚美欣辰科技有限公司.
 */
interface ApiService {
    /**
     * 记录上传
     */
    @POST("data_engine/Api/Insert.do")
    fun Insert(@Body requestBody: RequestBody): Observable<ResponeBean<Int>>
//    fun Insert(@Body requestBody: RequestBody): Observable<ResponseBody>

    /**
     * 文件上传
     */
    @POST("/data_engine/Api/Upload2.do")
    fun Upload2(@Body requestBody: RequestBody): Observable<ResponeBean<String>>

    /**
     * 心跳
     */
    @GET("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15016704684")
    fun test(): Observable<ResponseBody>


    /**
     * 心跳
     */
//    @POST("http://221.214.13.10:6026/FaceSerCloud/api/jnga/extranet/face/v1/compare-uncard")
//    fun ttttt(@HEAD s: String, @HEAD head2: String, @Body requestBody: RequestBody): Observable<ResponseBody>


}