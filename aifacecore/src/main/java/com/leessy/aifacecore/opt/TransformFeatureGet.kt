package com.leessy.aifacecore.opt

import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.datas.FaceData
import com.leessy.aifacecore.datas.isReadyFeature
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
        if (it.isReadyFeature()) {
            val start = System.currentTimeMillis()//开始时间
            when (it.nChannelNo) {
                AiFaceChannelNo.IRNo0 -> {
                    it.feature = ByteArray(AiFaceCore.AiChlIrFaceSize)
                    it.run {
                        featureGetStatu = AiFaceOpt.FeatureGetIR_0(
                            it.RGB24!!,
                            it.width,
                            it.height,
                            (it.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT?)!!,
                            it.feature!!
                        )
                    }
                }
                AiFaceChannelNo.COLORNo1 -> {
                    it.feature = ByteArray(AiFaceCore.AiChlFaceSize)
                    it.run {
                        featureGetStatu = AiFaceOpt.FeatureGet_1(
                            it.RGB24!!,
                            it.width,
                            it.height,
                            (it.detectResult as FACE_DETECT_RESULT?)!!,
                            it.feature!!
                        )
                    }
                }
                AiFaceChannelNo.IRNo2 -> {
                    it.feature = ByteArray(AiFaceCore.AiChlIrFaceSize)
                    it.run {
                        featureGetStatu = AiFaceOpt.FeatureGetIR_2(
                            it.RGB24!!,
                            it.width,
                            it.height,
                            (it.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT?)!!,
                            it.feature!!
                        )
                    }
                }
                AiFaceChannelNo.COLORNo3 -> {
                    it.feature = ByteArray(AiFaceCore.AiChlFaceSize)
                    it.run {
                        featureGetStatu = AiFaceOpt.FeatureGet_1(
                            it.RGB24!!,
                            it.width,
                            it.height,
                            (it.detectResult as FACE_DETECT_RESULT?)!!,
                            it.feature!!
                        )
                    }
                }
            }
            it.testTime_feature = System.currentTimeMillis() - start
//            Log.d(
//                "-----",
//                "*****人脸特征时间  ${it.testTime_feature}     thread ${Thread.currentThread().name}"
//            )
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


