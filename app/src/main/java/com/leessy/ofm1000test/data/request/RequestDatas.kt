package com.leessy.ofm1000test.data.request

import com.leessy.ofm1000test.Constans

/**
 * @author Created by 刘承. on 2019/7/31
 *
 * --深圳市尚美欣辰科技有限公司.
 */

class RequestDatas<T>(var body: T?) {
    val namespace = Constans.Namespace
    var name: String? = Constans.name
    val sn = Constans.sn
    val apiVersion = Constans.apiVersion
}

//mqtt结束任务响应
data class FinishData(var cmid: Int, var result: String? = null)

//init
data class InitData(var type: String?, var deviceName: String? = null)

data class dataarray(var value: ArrayList<uploadbody>? = null)

data class Record(
    var name: String? = null,
    var nation: String? = null,
    var id_card: String? = null,
    var image1: String? = null,
    var image2: String? = null

)

 class AppData {
     /*******原数据*********/
     var id: Int = 0
    var apkName: String? = null
    var path: String? = null//apk下载路径
    var versionName: String? = null
    var remark: String? = null
    var createTime: String? = null
    var versionCode: Int = 0//apk 版本号
    var typeId: String? = null
    var filekey: String? = null//下载下来的文件名称

     /*******新增数据*********/
     var apkfileMD5: String? = null//下载的apk文件md5
     var forceCode: String? = null//强制升级的最小Code
     var clearData: Boolean = false//升级前是否清除数据
 }


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

