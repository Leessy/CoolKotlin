package com.leessy.ofm1000test.data.respone

import com.thonnn.cubeengine.engineapi.asset.CommandMsgBean

/**
 *
 * @author Created by 刘承. on 2019/9/11
 *
 * --深圳市尚美欣辰科技有限公司.
 */
class ResponeBean<T> : ResponeBaseBean() {
    var info: Any? = null
    var body: T? = null
    var call: CommandMsgBean? = null

    override fun toString(): String {
        return "ResponeBean(info=$info, body=$body, call=$call)"
    }

}

class AppUpdate {
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
    var md5: String? = null//下载的apk文件md5
    var lowestVersionCode: String? = null//强制升级的最小Code
    var clearData: Boolean = false//升级前是否清除数据
}


//查询sn
data class FindSN(
    var trid: Int? = 0,
    var sn: String? = null,
    var type: String? = null,
    var active: String? = null,
    var testJson: String? = null,
    var valid: String? = null,
    var createTime: String? = null
)
//{
//    "trid": 1,
//    "sn": "F702190908000001",
//    "type": "F702",
//    "active": true,
//    "testJson": "{"mac": "40-B0-76-80-9B-D4"}",
//    "valid": true,
//    "createTime": "Sep 8, 2019 5:26:12 PM"
//},

//人脸信息
data class faceData(
    val curPage: Int,
    val offset: Int
)

