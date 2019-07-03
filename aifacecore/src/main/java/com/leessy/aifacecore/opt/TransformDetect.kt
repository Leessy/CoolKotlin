package com.leessy.aifacecore.opt

import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.datas.CameraData
import com.leessy.aifacecore.datas.FaceData
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 */


/**
 * 人脸检测
 */
fun Observable<CameraData>.DetectFace(): Observable<FaceData> {
    return map {
        FaceData().apply {
            CameraID = it.CameraID
            YUV = it.byteArray
            imageColor = it.imageColor
            RGB24 = ByteArray(it.width * it.height * 3)
            when (it.imageColor) {
                ImageColor.COLOR -> {
                    nChannelNo = if (it.CameraID == 0) AiFaceChannelNo.COLORNo3
                    else AiFaceChannelNo.COLORNo1 //指定默认通道  目前为默认值0一个通道，其他值一个通道，共两个
                    detectResult = FACE_DETECT_RESULT()
                }
                ImageColor.IR -> {
                    nChannelNo = if (it.CameraID == 0) AiFaceChannelNo.IRNo2
                    else AiFaceChannelNo.IRNo0//指定默认通道  目前为默认值0一个通道，其他值一个通道，共两个
                    detectResult = com.AiChlIrFace.FACE_DETECT_RESULT()
                }
            }
            val w = IntArray(1)
            val h = IntArray(1)
            faceNum = when (it.imageColor) {
                ImageColor.COLOR -> {
                    if (nChannelNo == AiFaceChannelNo.COLORNo1) {
                        AiFaceOpt.DetectFaceEx_1(
                            it.stream, it.byteArray, it.width, it.height, 0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h, detectResult as FACE_DETECT_RESULT
                        )
                    } else {
                        AiFaceOpt.DetectFaceEx_3(
                            it.stream, it.byteArray, it.width, it.height, 0, 0, 0, 0,
                            it.nRotate, it.bMirror, RGB24!!, w, h, detectResult as FACE_DETECT_RESULT
                        )
                    }
                }
                ImageColor.IR -> {
                    if (nChannelNo == AiFaceChannelNo.IRNo0) {
                        AiFaceOpt.DetectFaceExIR_0(
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
                            detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                        )
                    } else {
                        AiFaceOpt.DetectFaceExIR_2(
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
        }
    }
}


/**
 * 人脸检测 并过滤无人脸的数据
 */
fun Observable<CameraData>.DetectFaceAndFilter(cd: Observable<CameraData>): Observable<FaceData>? {
    return DetectFace()?.filter { it.faceNum > 0 }
}