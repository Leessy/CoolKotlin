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
    /**********************************************************分割线*********************************************************************/

//    http://127.0.0.1:7040/Api/Finish.do
//{ "namespace": "testGroup", "name": "user", "sn": "1", "apiVersion": "0.0.2", "body": { "cmid": 13, "result": "执行成功" } }
//body中必须携带cmid为任务id，在获取任务时给出的cmid，result为执行结果，可附带备注信息

    //mqtt任务完成
    @POST("/Api/Finish.do")
    fun Finish(@Body requestBody: RequestBody): Observable<ResponeBean<Any>>


    //上传文件
    @POST("/Api/Upload.do")
    fun UploadFile(@Body requestBody: RequestBody): Observable<ResponeBean<String>>

    //上传记录
    @POST("/Api/Insert.do")
    fun Insert(@Body requestBody: RequestBody): Observable<ResponeBean<String>>

    //上传记录
    @POST("/Api/Init.do")
    fun Init(@Body requestBody: RequestBody): Observable<ResponeBean<Any>>

    /**********************************************************分割线*********************************************************************/
//
//    /**
//     * 文件上传
//     */
//    @POST("/data_engine/Api/Upload2.do")
//    fun Upload2(@Body requestBody: RequestBody): Observable<ResponeBean<String>>
//
//    /**
//     * 心跳
//     */
//    @GET("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15016704684")
//    fun test(): Observable<ResponseBody>


    /**
     * 心跳
     */
//    @POST("http://221.214.13.10:6026/FaceSerCloud/api/jnga/extranet/face/v1/compare-uncard")
//    fun ttttt(@HEAD s: String, @HEAD head2: String, @Body requestBody: RequestBody): Observable<ResponseBody>


}