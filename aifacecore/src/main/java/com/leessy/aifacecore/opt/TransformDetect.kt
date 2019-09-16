package com.leessy.aifacecore.opt

import android.util.Log
import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.datas.CameraData
import com.leessy.aifacecore.datas.FaceData
import com.leessy.logd
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 */


/**
 * 人脸检测   并提取特征码
 */
//fun Observable<CameraData>.DetectFace_Feature(): Observable<FaceData> {
//    return map {
//        FaceData().apply {
//            val start = System.currentTimeMillis()//记录时间
//            CameraID = it.CameraID
//            YUV = it.byteArray
//            imageColor = it.imageColor
//            RGB24 = ByteArray(it.width * it.height * 3)
//            when (it.imageColor) {
//                ImageColor.COLOR -> {
//                    nChannelNo = if (it.CameraID == 0) AiFaceChannelNo.COLORNo1
//                    else AiFaceChannelNo.COLORNo3 //指定默认通道  目前为默认值0一个通道，其他值一个通道，共两个
//                    detectResult = FACE_DETECT_RESULT()
//                }
//                ImageColor.IR -> {
//                    nChannelNo = if (it.CameraID == 0) AiFaceChannelNo.IRNo0
//                    else AiFaceChannelNo.IRNo2//指定默认通道  目前为默认值0一个通道，其他值一个通道，共两个
//                    detectResult = com.AiChlIrFace.FACE_DETECT_RESULT()
//                }
//            }
//            val w = IntArray(1)
//            val h = IntArray(1)
//
//            when (it.imageColor) {
//                ImageColor.COLOR -> {
//                    if (nChannelNo == AiFaceChannelNo.COLORNo1) {
//                        faceNum = AiFaceOpt.DetectFaceEx_1(
//                            it.stream, it.byteArray, it.width, it.height, 0, 0, 0, 0,
//                            it.nRotate, it.bMirror, RGB24!!, w, h, detectResult as FACE_DETECT_RESULT
//                        )
//                        this.testTime_face = System.currentTimeMillis() - start
//                    } else {
//                        faceNum = AiFaceOpt.DetectFaceEx_3(
//                            it.stream, it.byteArray, it.width, it.height, 0, 0, 0, 0,
//                            it.nRotate, it.bMirror, RGB24!!, w, h, detectResult as FACE_DETECT_RESULT
//                        )
//                        this.testTime_face = System.currentTimeMillis() - start
//                    }
//                }
//                ImageColor.IR -> {
//                    if (nChannelNo == AiFaceChannelNo.IRNo0) {
//                        faceNum = AiFaceOpt.DetectFaceExIR_0(
//                            it.stream, it.byteArray, it.width, it.height,
//                            0, 0, 0, 0,
//                            it.nRotate, it.bMirror, RGB24!!, w, h,
//                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
//                        )
//                        this.testTime_face = System.currentTimeMillis() - start
//                    } else {
//                        faceNum = AiFaceOpt.DetectFaceExIR_2(
//                            it.stream, it.byteArray, it.width, it.height,
//                            0, 0, 0, 0,
//                            it.nRotate, it.bMirror, RGB24!!, w, h,
//                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
//                        )
//                        this.testTime_face = System.currentTimeMillis() - start
//                    }
//                }
//            }
//
//            if (faceNum > 0) {
//                FaceRectEmitterCenter.sendFaceRect(it.imageColor, it.CameraID, detectResult!!)
//            }
//        }
//    }.FeatureGet()
//}


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
                    faceNum = if (nChannelNo == AiFaceChannelNo.COLORNo1) {
                        AiFaceOpt.DetectFaceEx_1(
                            it.stream,
                            it.byteArray,
                            it.width,
                            it.height,
                            0,
                            0,
                            0,
                            0,
                            it.nRotate,
                            it.bMirror,
                            RGB24!!,
                            w,
                            h,
                            detectResult as FACE_DETECT_RESULT
                        )
                    } else {
                        AiFaceOpt.DetectFaceEx_3(
                            it.stream,
                            it.byteArray,
                            it.width,
                            it.height,
                            0,
                            0,
                            0,
                            0,
                            it.nRotate,
                            it.bMirror,
                            RGB24!!,
                            w,
                            h,
                            detectResult as FACE_DETECT_RESULT
                        )
                    }
                }
                ImageColor.IR -> {
                    faceNum = if (nChannelNo == AiFaceChannelNo.IRNo0) {
                        AiFaceOpt.DetectFaceExIR_0(
                            it.stream, it.byteArray, it.width, it.height,
                            0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h,
                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                        )
                    } else {
                        AiFaceOpt.DetectFaceExIR_2(
                            it.stream, it.byteArray, it.width, it.height,
                            0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h,
                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                        )
                    }
                }
            }

            if (faceNum > 0) {
                FaceRectEmitterCenter.sendFaceRect(
                    it.imageColor,
                    it.CameraID,
                    it.width,
                    it.height,
                    detectResult!!
                )
            } else {
                FaceRectEmitterCenter.sendFaceRect(it.imageColor, it.CameraID)
            }
            width = w[0]
            height = h[0]
            this.testTime_face = System.currentTimeMillis() - start
            logd(
                "---",
                "-**-人脸时间 ${this.testTime_face}   $imageColor    Thread=${Thread.currentThread().name}"
            )
        }
    }
}


/**
 * 人脸检测 并过滤无人脸的数据
 */
fun Observable<CameraData>.DetectFaceAndFilter(): Observable<FaceData> {
    return DetectFace()?.filter { it.faceNum > 0 }
}

/**
 * 过滤无人脸的数据
 */
fun Observable<FaceData>.filterNoneface(): Observable<FaceData> {
    return filter { it.faceNum > 0 }
}