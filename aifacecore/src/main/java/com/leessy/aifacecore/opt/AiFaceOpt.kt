package com.leessy.aifacecore.opt

import com.AiChlFace.AiChlFace
import com.AiChlFace.FACE_DETECT_RESULT
import com.AiChlIrFace.AiChlIrFace

/**
 * 多通道封装 防止通道错误
 *
 * @author Created by 刘承. on 2018/10/11
 * business@onfacemind.com
 */
object AiFaceOpt {

    //可见光1
    @Synchronized
    fun DetectFaceEx_1(
        nFmt: Int,
        bSrcImg: ByteArray,
        nWidth: Int,
        nHeight: Int,
        nLeft: Int,
        nTop: Int,
        nRight: Int,
        nBottom: Int,
        nRotate: Int,
        bMirror: Int,
        bRgb24: ByteArray,
        nNewWidth: IntArray,
        nNewHeight: IntArray,
        sFaceResult: FACE_DETECT_RESULT
    ): Int {

        return AiChlFace.DetectFaceEx(
            1, nFmt, bSrcImg, nWidth, nHeight, nLeft, nTop, nRight, nBottom,
            nRotate, bMirror, bRgb24, nNewWidth, nNewHeight, sFaceResult
        )
    }

    //可见光3
    @Synchronized
    fun DetectFaceEx_3(
        nFmt: Int,
        bSrcImg: ByteArray,
        nWidth: Int,
        nHeight: Int,
        nLeft: Int,
        nTop: Int,
        nRight: Int,
        nBottom: Int,
        nRotate: Int,
        bMirror: Int,
        bRgb24: ByteArray,
        nNewWidth: IntArray,
        nNewHeight: IntArray,
        sFaceResult: FACE_DETECT_RESULT
    ): Int {

        return AiChlFace.DetectFaceEx(
            3, nFmt, bSrcImg, nWidth, nHeight, nLeft, nTop, nRight, nBottom,
            nRotate, bMirror, bRgb24, nNewWidth, nNewHeight, sFaceResult
        )
    }

    //红外0
    @Synchronized
    fun DetectFaceExIR_0(
        nFmt: Int,
        bSrcImg: ByteArray,
        nWidth: Int,
        nHeight: Int,
        nLeft: Int,
        nTop: Int,
        nRight: Int,
        nBottom: Int,
        nRotate: Int,
        bMirror: Int,
        bRgb24: ByteArray,
        nNewWidth: IntArray,
        nNewHeight: IntArray,
        sFaceResult: com.AiChlIrFace.FACE_DETECT_RESULT
    ): Int {

        return AiChlIrFace.DetectFaceEx(
            0, nFmt, bSrcImg, nWidth, nHeight, nLeft, nTop, nRight, nBottom,
            nRotate, bMirror, bRgb24, nNewWidth, nNewHeight, sFaceResult
        )
    }

    //红外2
    @Synchronized
    fun DetectFaceExIR_2(
        nFmt: Int,
        bSrcImg: ByteArray,
        nWidth: Int,
        nHeight: Int,
        nLeft: Int,
        nTop: Int,
        nRight: Int,
        nBottom: Int,
        nRotate: Int,
        bMirror: Int,
        bRgb24: ByteArray,
        nNewWidth: IntArray,
        nNewHeight: IntArray,
        sFaceResult: com.AiChlIrFace.FACE_DETECT_RESULT
    ): Int {

        return AiChlIrFace.DetectFaceEx(
            2, nFmt, bSrcImg, nWidth, nHeight, nLeft, nTop, nRight, nBottom,
            nRotate, bMirror, bRgb24, nNewWidth, nNewHeight, sFaceResult
        )
    }


    //可见光1  提取特征
    @Synchronized
    fun FeatureGet_1(
        bRgb24: ByteArray,
        nWidth: Int,
        nHeight: Int,
        sFaceResult: FACE_DETECT_RESULT,
        bFeature: ByteArray
    ): Int {
        return AiChlFace.FeatureGet(1, bRgb24, nWidth, nHeight, sFaceResult, bFeature)
    }

    //可见光3  提取特征
    @Synchronized
    fun FeatureGet_3(
        bRgb24: ByteArray,
        nWidth: Int,
        nHeight: Int,
        sFaceResult: FACE_DETECT_RESULT,
        bFeature: ByteArray
    ): Int {
        return AiChlFace.FeatureGet(3, bRgb24, nWidth, nHeight, sFaceResult, bFeature)
    }

    //红外0  提取特征
    @Synchronized
    fun FeatureGetIR_0(
        bRgb24: ByteArray,
        nWidth: Int,
        nHeight: Int,
        sFaceResult: com.AiChlIrFace.FACE_DETECT_RESULT,
        bFeature: ByteArray
    ): Int {
        return FeatureGet_1(bRgb24, nWidth, nHeight, transformFaceResult(sFaceResult), bFeature)
    }

    //红外1  提取特征
    @Synchronized
    fun FeatureGetIR_2(
        bRgb24: ByteArray,
        nWidth: Int,
        nHeight: Int,
        sFaceResult: com.AiChlIrFace.FACE_DETECT_RESULT,
        bFeature: ByteArray
    ): Int {
        return FeatureGet_3(bRgb24, nWidth, nHeight, transformFaceResult(sFaceResult), bFeature)
    }

    //1：1对比 根据模板类型 切换对应算法 (如果转换了，红外1：1应使用彩色对比方法返回结果，否则模板长度错误会导致崩溃)
    @Synchronized
    fun FeatureCompareIr(nChannelNo: Int, bFeature1: ByteArray, bFeature2: ByteArray): Int {
        return AiChlFace.FeatureCompare(nChannelNo, bFeature1, bFeature2)
    }

    //转换人脸数据
    private fun transformFaceResult(detectResult: com.AiChlIrFace.FACE_DETECT_RESULT): FACE_DETECT_RESULT {
        val result = FACE_DETECT_RESULT()
        result.nFaceLeft = detectResult.nFaceLeft
        result.nFaceTop = detectResult.nFaceTop
        result.nFaceRight = detectResult.nFaceRight
        result.nFaceBottom = detectResult.nFaceBottom
        result.nLeftEyeX = detectResult.nLeftEyeX
        result.nLeftEyeY = detectResult.nLeftEyeY
        result.nRightEyeX = detectResult.nRightEyeX
        result.nRightEyeY = detectResult.nRightEyeY
        result.nMouthX = detectResult.nMouthX
        result.nMouthY = detectResult.nMouthY
        result.nNoseX = detectResult.nNoseX
        result.nNoseY = detectResult.nNoseY
        result.nAngleYaw = detectResult.nAngleYaw
        result.nAnglePitch = detectResult.nAnglePitch
        result.nAngleRoll = detectResult.nAngleRoll
        result.nQuality = detectResult.nQuality
        //        result.nBrightAvg = detectResult.nBrightAvg;// 彩色人脸信息无亮度
        result.FaceData = detectResult.FaceData.clone()
        return result
    }
}
