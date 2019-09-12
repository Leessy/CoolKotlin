package com.leessy.aifacecore.datas

import com.AiChlFace.FACE_DETECT_RESULT

class FaceResult<T>(f: T) {
    var face: T? = f

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
    var nBrightAvg: Int = 0 // 人脸区域平均亮度

    fun getColor(): FACE_DETECT_RESULT {
        return face as FACE_DETECT_RESULT
    }

    fun getIr(): com.AiChlIrFace.FACE_DETECT_RESULT {
        return face as com.AiChlIrFace.FACE_DETECT_RESULT
    }

    init {
        if (f is FACE_DETECT_RESULT) {
            nFaceLeft = f.nFaceLeft
            nFaceTop = f.nFaceTop
            nFaceRight = f.nFaceRight
            nFaceBottom = f.nFaceBottom
            nLeftEyeX = f.nLeftEyeX
            nLeftEyeY = f.nLeftEyeY
            nRightEyeX = f.nRightEyeX
            nRightEyeY = f.nRightEyeY
            nMouthX = f.nMouthX
            nMouthY = f.nMouthY
            nNoseX = f.nNoseX
            nNoseY = f.nNoseY
            nAngleYaw = f.nAngleYaw
            nAnglePitch = f.nAnglePitch
            nAngleRoll = f.nAngleRoll
            nQuality = f.nQuality
        } else if (f is com.AiChlIrFace.FACE_DETECT_RESULT) {
            nFaceLeft = f.nFaceLeft
            nFaceTop = f.nFaceTop
            nFaceRight = f.nFaceRight
            nFaceBottom = f.nFaceBottom
            nLeftEyeX = f.nLeftEyeX
            nLeftEyeY = f.nLeftEyeY
            nRightEyeX = f.nRightEyeX
            nRightEyeY = f.nRightEyeY
            nMouthX = f.nMouthX
            nMouthY = f.nMouthY
            nNoseX = f.nNoseX
            nNoseY = f.nNoseY
            nAngleYaw = f.nAngleYaw
            nAnglePitch = f.nAnglePitch
            nAngleRoll = f.nAngleRoll
            nQuality = f.nQuality
            nBrightAvg = f.nBrightAvg
        }
    }
}
