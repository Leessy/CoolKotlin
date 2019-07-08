package com.leessy.aifacecore.opt

import android.util.Log
import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.datas.CameraData
import com.leessy.aifacecore.datas.FaceData
import com.leessy.aifacecore.datas.isReadyFeature
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 */


/**
 * 人脸检测   并提取特征码
 */
fun Observable<CameraData>.DetectFace_Feature(): Observable<FaceData> {
    return map {
        FaceData().apply {
            val start = System.currentTimeMillis()//记录时间
            CameraID = it.CameraID
            YUV = it.byteArray
            imageColor = it.imageColor
            RGB24 = ByteArray(it.width * it.height * 3)
            when (it.imageColor) {
                ImageColor.COLOR -> {
                    nChannelNo = if (it.CameraID == 0) AiFaceChannelNo.COLORNo1
                    else AiFaceChannelNo.COLORNo3 //指定默认通道  目前为默认值0一个通道，其他值一个通道，共两个
                    detectResult = FACE_DETECT_RESULT()
                }
                ImageColor.IR -> {
                    nChannelNo = if (it.CameraID == 0) AiFaceChannelNo.IRNo0
                    else AiFaceChannelNo.IRNo2//指定默认通道  目前为默认值0一个通道，其他值一个通道，共两个
                    detectResult = com.AiChlIrFace.FACE_DETECT_RESULT()
                }
            }
            val w = IntArray(1)
            val h = IntArray(1)

            when (it.imageColor) {
                ImageColor.COLOR -> {
                    if (nChannelNo == AiFaceChannelNo.COLORNo1) {
                        faceNum = AiFaceOpt.DetectFaceEx_1(
                            it.stream, it.byteArray, it.width, it.height, 0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h, detectResult as FACE_DETECT_RESULT
                        )
                        this.testTime_face = System.currentTimeMillis() - start

                        if (isReadyFeature()) {
                            val start = System.currentTimeMillis()//开始时间
                            feature = ByteArray(AiFaceCore.AiChlFaceSize)
                            width = w[0]
                            height = h[0]
                            featureGetStatu = AiFaceOpt.FeatureGet_1(
                                RGB24!!,
                                width,
                                height,
                                (detectResult as FACE_DETECT_RESULT?)!!,
                                feature!!
                            )
                            testTime_feature = System.currentTimeMillis() - start
                        }
                    } else {
                        faceNum = AiFaceOpt.DetectFaceEx_3(
                            it.stream, it.byteArray, it.width, it.height, 0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h, detectResult as FACE_DETECT_RESULT
                        )
                        this.testTime_face = System.currentTimeMillis() - start
                        if (isReadyFeature()) {
                            feature = ByteArray(AiFaceCore.AiChlFaceSize)
                            width = w[0]
                            height = h[0]
                            featureGetStatu = AiFaceOpt.FeatureGet_3(
                                RGB24!!,
                                width,
                                height,
                                (detectResult as FACE_DETECT_RESULT?)!!,
                                feature!!
                            )
                        }
                    }
                }
                ImageColor.IR -> {
                    if (nChannelNo == AiFaceChannelNo.IRNo0) {
                        faceNum = AiFaceOpt.DetectFaceExIR_0(
                            it.stream, it.byteArray, it.width, it.height,
                            0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h,
                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                        )
                        this.testTime_face = System.currentTimeMillis() - start
                        if (isReadyFeature()) {
                            feature = ByteArray(AiFaceCore.AiChlIrFaceSize)
                            width = w[0]
                            height = h[0]
                            featureGetStatu = AiFaceOpt.FeatureGetIR_0(
                                RGB24!!,
                                width,
                                height,
                                (detectResult)!! as com.AiChlIrFace.FACE_DETECT_RESULT,
                                feature!!
                            )
                        }
                    } else {
                        faceNum = AiFaceOpt.DetectFaceExIR_2(
                            it.stream, it.byteArray, it.width, it.height,
                            0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h,
                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                        )
                        this.testTime_face = System.currentTimeMillis() - start
                        if (isReadyFeature()) {
                            feature = ByteArray(AiFaceCore.AiChlIrFaceSize)
                            width = w[0]
                            height = h[0]
                            featureGetStatu = AiFaceOpt.FeatureGetIR_2(
                                RGB24!!,
                                width,
                                height,
                                (detectResult)!! as com.AiChlIrFace.FACE_DETECT_RESULT,
                                feature!!
                            )
                        }
                    }
                }
            }

            if (faceNum > 0) {
                FaceRectEmitterCenter.sendFaceRect(it.imageColor, it.CameraID, detectResult!!)
            }
        }
    }
}


/**
 * 人脸检测
 */
fun Observable<CameraData>.DetectFace(): Observable<FaceData> {
    return map {
        FaceData().apply {
            val start = System.currentTimeMillis()//记录时间
            CameraID = it.CameraID
            YUV = it.byteArray
            imageColor = it.imageColor
            RGB24 = ByteArray(it.width * it.height * 3)
            when (it.imageColor) {
                ImageColor.COLOR -> {
                    nChannelNo = if (it.CameraID == 0) AiFaceChannelNo.COLORNo1
                    else AiFaceChannelNo.COLORNo3 //指定默认通道  目前为默认值0一个通道，其他值一个通道，共两个
                    detectResult = FACE_DETECT_RESULT()
                }
                ImageColor.IR -> {
                    nChannelNo = if (it.CameraID == 0) AiFaceChannelNo.IRNo0
                    else AiFaceChannelNo.IRNo2//指定默认通道  目前为默认值0一个通道，其他值一个通道，共两个
                    detectResult = com.AiChlIrFace.FACE_DETECT_RESULT()
                }
            }
            val w = IntArray(1)
            val h = IntArray(1)

            when (it.imageColor) {
                ImageColor.COLOR -> {
                    if (nChannelNo == AiFaceChannelNo.COLORNo1) {
                        var test0 = System.currentTimeMillis()

                        faceNum = AiFaceOpt.DetectFaceEx_1(
                            it.stream, it.byteArray, it.width, it.height, 0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h, detectResult as FACE_DETECT_RESULT
                        )
                        Log.d(
                            "-----",
                            "****人脸时间  ${imageColor.name}     ${System.currentTimeMillis() - test0}   thread ${Thread.currentThread().name}"
                        )
                    } else {
                        faceNum = AiFaceOpt.DetectFaceEx_3(
                            it.stream, it.byteArray, it.width, it.height, 0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h, detectResult as FACE_DETECT_RESULT
                        )
                    }
                }
                ImageColor.IR -> {
                    if (nChannelNo == AiFaceChannelNo.IRNo0) {
                        faceNum = AiFaceOpt.DetectFaceExIR_0(
                            it.stream, it.byteArray, it.width, it.height,
                            0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h,
                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                        )
                    } else {
                        faceNum = AiFaceOpt.DetectFaceExIR_2(
                            it.stream, it.byteArray, it.width, it.height,
                            0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h,
                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                        )
                    }
                }
            }

            if (faceNum > 0) {
                FaceRectEmitterCenter.sendFaceRect(it.imageColor, it.CameraID, detectResult!!)
            }
            width = w[0]
            height = h[0]
            this.testTime_face = System.currentTimeMillis() - start
        }
    }
}


/**
 * 人脸检测 并过滤无人脸的数据
 */
fun Observable<CameraData>.DetectFaceAndFilter(cd: Observable<CameraData>): Observable<FaceData>? {
    return DetectFace()?.filter { it.faceNum > 0 }
}