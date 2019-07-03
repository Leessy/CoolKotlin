package com.leessy.aifacecore.opt

import com.AiChlFace.AiChlFace
import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.datas.FaceData
import com.leessy.aifacecore.datas.isReadyFeature
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 */


/**
 * 提取特征码
 */
fun Observable<FaceData>.FeatureGet(): Observable<FaceData> {
    return filter { it.isReadyFeature() }
        .map {
            if (it.RGB24 != null && it.detectResult != null) {
                it.FeatureGet = when (it.nChannelNo) {
                    AiFaceChannelNo.IRNo0 -> {
                        it.Feature = ByteArray(AiChlFace.FeatureSize())
                        it.run {
                            AiFaceOpt.FeatureGetIR_0(
                                it.RGB24!!,
                                it.width,
                                it.height,
                                (it.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT?)!!,
                                it.Feature!!
                            )
                        }
                    }
                    AiFaceChannelNo.COLORNo1 -> {
                        it.Feature = ByteArray(AiChlFace.FeatureSize())
                        it.run {
                            AiFaceOpt.FeatureGet_1(
                                it.RGB24!!,
                                it.width,
                                it.height,
                                (it.detectResult as FACE_DETECT_RESULT?)!!,
                                it.Feature!!
                            )
                        }
                    }
                    AiFaceChannelNo.IRNo2 -> {
                        it.Feature = ByteArray(AiChlFace.FeatureSize())
                        it.run {
                            AiFaceOpt.FeatureGetIR_2(
                                it.RGB24!!,
                                it.width,
                                it.height,
                                (it.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT?)!!,
                                it.Feature!!
                            )
                        }
                    }
                    AiFaceChannelNo.COLORNo3 -> {
                        it.Feature = ByteArray(AiChlFace.FeatureSize())
                        it.run {
                            AiFaceOpt.FeatureGet_1(
                                it.RGB24!!,
                                it.width,
                                it.height,
                                (it.detectResult as FACE_DETECT_RESULT?)!!,
                                it.Feature!!
                            )
                        }
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
    return FeatureGet()?.filter { it.FeatureGet == 0 }
}


