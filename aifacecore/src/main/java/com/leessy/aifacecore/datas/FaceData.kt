package com.leessy.aifacecore.datas

import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.opt.AiFaceChannelNo
import com.leessy.aifacecore.opt.ImageColor

/**
 *
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 */
class FaceData {
    var YUV: ByteArray? = null//yuv
    var RGB24: ByteArray? = null//rgb24
    var Feature: ByteArray? = null//特征
    var FeatureGet: Int? = -1//提取特征返回值
    var detectResult: Any? = null
    var width: Int = 0//转换之后的宽高
    var height: Int = 0//转换之后的宽高
    var faceNum: Int = 0//人脸个数

    var imageColor = ImageColor.COLOR//图片类型
    var CameraID = 0 //相机id,或者源数据分类1

    lateinit var nChannelNo: AiFaceChannelNo //nChannelNo 执行通道 默认为-1 不指定通道，其他为指定通道运行

    var CompareRet: Long = 0//对比值
    var CompareDataID: Long = 0//对比 数据库id

    var Livings: Int = 0//活体结果
}


/**
 * 判断是否活体检测通过
 */
fun FaceData.isLivings(): Boolean {
    return Livings == 1
}

/**
 * 检查数据是否可以参与识别
 */
fun FaceData.isReadyCompare(): Boolean {
    return FeatureGet == 0 && Feature != null
            && Feature!!.size ==
            if (imageColor == ImageColor.COLOR) AiFaceCore.AiChlFaceSize
            else AiFaceCore.AiChlIrFaceSize
}

/**
 * 检查数据是否可以提取特征码
 */
fun FaceData.isReadyFeature(): Boolean {
    return detectResult != null
            && RGB24 != null
            && faceNum > 0
}