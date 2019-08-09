package com.leessy.ofm1000test.data.request


/**
 *
 * @author Created by 刘承. on 2019/7/31
 *
 * --深圳市尚美欣辰科技有限公司.
 *
 *
 *
 */

public class Requests {
    var namespace: String? = null
    var name: String? = null
    var sn: String? = null
    var apiVersion: String? = null
    var body: dataarray? = null

}

class dataarray(var value: ArrayList<uploadbody>? = null)

class uploadbody {
    var name: String? = null
    var nation: String? = null
    var id_card: String? = null
    var address: String? = null
    var expire_date_start: String? = null
    var expire_date_end: String? = null
    var birth_day: String? = null
    var finger: String? = null
    var record_time: String? = null
    var remark: String? = null
    var last_update_time: String? = null
    var sn: String? = null
    var score: Int? = null
    var provesuccessful: Int? = null
    var longitude: String? = null
    var latitude: String? = null
    var devicelocation: String? = null
    var recordtype: Int? = null
    var versioncode: String? = null
    var icCard: String? = null
    var facepic: String? = null
    var template: String? = null
    var capPic: String? = null
}

//class recordData {
//    "id_card": "422232200010214521",
//    "device_sn": "SNVSJDG0000001",
//    "cap_pic": "",
//    "face_pic": "",
//    "score": "72",
//    "remark": "测试数据1",
//    "create_time": "2019-07-25 10:37:22"
//}
