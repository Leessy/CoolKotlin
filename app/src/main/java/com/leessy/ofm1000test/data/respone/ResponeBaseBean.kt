package com.leessy.ofm1000test.data.respone


/**
 *base
 */
open class ResponeBaseBean {
//    var errcode: Int = -1   //结果码
//    var errmsg: String = ""  //响应消息

    var errcode: Int = 0
    var errmsg: String? = null


    var code: Int = 0//app升级数据兼容旧版 使用此字段
}
