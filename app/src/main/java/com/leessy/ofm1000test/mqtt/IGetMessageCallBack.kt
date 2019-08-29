package com.leessy.ofm1000test.mqtt
interface IGetMessageCallBack {
    fun setMessage(s: String, messageId: Int)
}