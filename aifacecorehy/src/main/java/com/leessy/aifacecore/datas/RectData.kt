package com.leessy.aifacecore.datas

import android.graphics.RectF
import com.leessy.aifacecore.opt.AiFaceFilter
import com.leessy.aifacecore.opt.ImageColor

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 * 人脸框数据
 */
class RectData(var imageColor: ImageColor, var cameraId: Int = 0, var width: Int=0, var height: Int=0) {
    //人脸
    //相机id，或者数据分类值

    var nFaceLeft: Int = 0
    var nFaceTop: Int = 0
    var nFaceRight: Int = 0
    var nFaceBottom: Int = 0 // 人脸坐标
    var nLeftEyeX: Int = 0
    var nLeftEyeY: Int = 0
    var nRightEyeX: Int = 0
    var nRightEyeY: Int = 0 // 左右眼坐标
    var nMouthX: Int = 0
    var nMouthY: Int = 0
    var nNoseX: Int = 0
    var nNoseY: Int = 0 // 嘴巴鼻子坐标
    var nAngleYaw: Int = 0
    var nAnglePitch: Int = 0
    var nAngleRoll: Int = 0 // 人脸偏转角度（左右，上下，水平方向）
    var nQuality: Int = 0 // 人脸质量
    var rect: RectF? = null

//    var width: Int = 0//转换之后的宽高
//    var height: Int = 0//转换之后的宽高
    var faceFilterRet = AiFaceFilter.DEFAULT  // 人脸位置过滤计算结果
}

/**
 * 没有人脸的数据
 */
fun RectData.isNoneFace(): Boolean {
    return nFaceBottom == 0 && nFaceRight == 0
}