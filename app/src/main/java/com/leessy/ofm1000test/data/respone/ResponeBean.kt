package com.leessy.ofm1000test.data.respone

import com.thonnn.cubeengine.engineapi.asset.CommandMsgBean

/**
 *
 * @Author:  陈博
 * @create time:  2019/6/11  9:48
 */
class ResponeBean<T> : ResponeBaseBean() {
    var info: Any? = null
    var body: T? = null
    var call: CommandMsgBean? = null

    override fun toString(): String {
        return "ResponeBean(info=$info, body=$body, call=$call)"
    }

}


//人脸信息
data class faceData(
    val curPage: Int,
    val offset: Int
)

