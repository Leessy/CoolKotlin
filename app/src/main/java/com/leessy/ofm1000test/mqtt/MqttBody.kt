package com.leessy.ofm1000test.mqtt

/**
 * @author Created by 刘承. on 2019/6/11
 * business@onfacemind.com
 */
class MqttBody<T> {
//    <cmd desc="下发人脸模板" name="addFace" target="7018">
//    <param desc="图片ID">picId</param>
//    </cmd>
//    <cmd desc="下发人脸模板2" name="addFace2">
//    <param desc="图片ID">picId</param>
//    </cmd>

    internal var cmd: Int = 0//addFace addFace2
    var cmid: Int = 0
    var sn: String? = null
    var msgUuid: String? = null
    var createTime: String? = null
    var sendTime: String? = null
    var isSend: Boolean = false
    var isFinish: Boolean = false
    var msg: T? = null

    class Body {
        var cmd: String? = null
        var sn: String? = null
        var picId: String? = null
    }

//    {
//        "cmid":115,
//        "mid":0,
//        "sn":"123456",
//        "msgUuid":"1451797353314df9a881dd2db2c38b66",
//        "createTime":"Aug 28, 2019 9:44:59 AM",
//        "sendTime":"Aug 28, 2019 10:27:57 AM",
//        "isSend":true,
//        "isFinish":false,
//        "msg":{
//        "cmd":"addFace",
//        "sn":"123456",
//        "picId":"asdfsdag"
//    }
//    }
}
