package com.leessy.ofm1000test.data.respone

/**
 *
 * @Author:  陈博
 * @create time:  2019/6/11  9:48
 */
class ResponeBean<T> : ResponeBaseBean() {
    var body: T? = null   //响应内容


    override fun toString(): String {
        return "ResponeBean(errcode=$errcode,errmsg=$errmsg, body=$body)"
    }

}


//人脸信息
data class faceData(
        val curPage: Int,
        val offset: Int
)

