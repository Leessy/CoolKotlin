package com.leessy.aifacecore.opt

import com.huiyuenet.faceCheck.FaceFunction
import com.huiyuenet.faceCheck.THFI_FacePos
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
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
            width = it.width
            height = it.height

            when (it.imageColor) {
                ImageColor.COLOR -> {
                    bitmap = AiFaceCore.yuvToBitmap(YUV!!, it.width, it.height)//转bitmap
                    BGR24 = FaceFunction.getPixelsBGR(bitmap)//转bgr24
                    val t = THFI_FacePos().apply { detectResult = this }
                    val faceResult = arrayOf(t)
                    faceNum = FaceFunction.faceDetect(BGR24, it.width, it.height, 30, faceResult!!)
                }
                ImageColor.IR -> {
                    bitmap = AiFaceCore.yuvToBitmap(YUV!!, it.width, it.height)//转bitmap
                    BGR24 = FaceFunction.getPixelsBGR(bitmap)//转bgr24
                    val t = THFI_FacePos().apply { detectResult = this }
                    val faceResult = arrayOf(t)
                    faceNum = FaceFunction.faceDetect(BGR24, it.width, it.height, 30, faceResult!!)
                }
            }
            if (faceNum > 0) {
                FaceRectEmitterCenter.sendFaceRect(
                    imageColor,
                    CameraID,
                    width,
                    height,
                    detectResult!!
                )
            } else {
                FaceRectEmitterCenter.sendFaceRect(it.imageColor, it.CameraID)
            }
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