package com.leessy.aifacecore.opt

import com.huiyuenet.faceCheck.FaceFunction
import com.huiyuenet.faceCheck.THFI_Param
import com.leessy.aifacecore.datas.FaceData
import com.leessy.aifacecore.datas.haveFaceData
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 */


/**
 * 提取特征码
 *
 * 没有人脸的数据，不做处理
 */
fun Observable<FaceData>.FeatureGet(): Observable<FaceData> {
    return map {
        if (it.haveFaceData()) {
            if (it.imageColor == ImageColor.COLOR) {
                it.feature = FaceFunction.faceFeatures(
                    it.BGR24,
                    it.width,
                    it.height,
                    THFI_Param.FACE_VALID_ANGLE,
                    arrayOf(it.detectResult)
                )
                if (it.feature != null) {
                    it.featureGetStatu = 0
                }
            }
        }
        it
    }
}

/**
 * 提取特征码  并过滤失败数据
 */
fun Observable<FaceData>.FeatureGetandFilter(cd: Observable<FaceData>): Observable<FaceData> {
    return FeatureGet()?.filter { it.featureGetStatu == 0 }
}

/**
 * 提取特征码
 *
 * 没有人脸的数据，不做处理
 */
//fun Observable<FaceData>.FaceQualityGet(): Observable<FaceData> {
//    return map {
//        if (it.haveFaceData()) {
//            val start = System.currentTimeMillis()//开始时间
//            when (it.nChannelNo) {
//                AiFaceChannelNo.IRNo0 -> {
//                    var fq = FACE_QUALITY()
//                    it.faceQualityStatus = AiChlIrFace.FaceQuality(
//                        0,
//                        it.RGB24!!,
//                        it.width,
//                        it.height,
//                        (it.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT?)!!,
//                        fq
//                    )
//                    if (it.faceQualityStatus == 0) {
//                        it.nMask = fq.nMask
//                        it.nHat = fq.nHat
//                        it.nGlasses = fq.nGlasses
//                        it.nBrightLevel = fq.nBrightLevel
//                        it.nBlur = fq.nBlur
//                    }
//                }
//                AiFaceChannelNo.IRNo2 -> {
//                    val fq = FACE_QUALITY()
//                    it.faceQualityStatus = AiChlIrFace.FaceQuality(
//                        2,
//                        it.RGB24!!,
//                        it.width,
//                        it.height,
//                        (it.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT?)!!,
//                        fq
//                    )
//                    if (it.faceQualityStatus == 0) {
//                        it.nMask = fq.nMask
//                        it.nHat = fq.nHat
//                        it.nGlasses = fq.nGlasses
//                        it.nBrightLevel = fq.nBrightLevel
//                        it.nBlur = fq.nBlur
//                    }
//                }
//                AiFaceChannelNo.COLORNo1 -> {
//                    val fq = com.AiChlFace.FACE_QUALITY()
//                    it.faceQualityStatus = AiChlFace.FaceQuality(
//                        1,
//                        it.RGB24!!,
//                        it.width,
//                        it.height,
//                        (it.detectResult as com.AiChlFace.FACE_DETECT_RESULT?)!!,
//                        fq
//                    )
//                    if (it.faceQualityStatus == 0) {
//                        it.nMask = fq.nMask
//                        it.nHat = fq.nHat
//                        it.nGlasses = fq.nGlasses
//                        it.nBrightLevel = fq.nBrightLevel
//                        it.nBlur = fq.nBlur
//                    }
//                }
//                AiFaceChannelNo.COLORNo3 -> {
//                    val fq = com.AiChlFace.FACE_QUALITY()
//                    it.faceQualityStatus = AiChlFace.FaceQuality(
//                        3,
//                        it.RGB24!!,
//                        it.width,
//                        it.height,
//                        (it.detectResult as com.AiChlFace.FACE_DETECT_RESULT?)!!,
//                        fq
//                    )
//                    if (it.faceQualityStatus == 0) {
//                        it.nMask = fq.nMask
//                        it.nHat = fq.nHat
//                        it.nGlasses = fq.nGlasses
//                        it.nBrightLevel = fq.nBrightLevel
//                        it.nBlur = fq.nBlur
//                    }
//                }
//            }
//            logd("质量检测时间${System.currentTimeMillis() - start}")
//        }
//        it
//    }
//}


