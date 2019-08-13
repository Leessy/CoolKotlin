package com.leessy.aifacecore.opt

import android.util.Log
import com.AiChlFace.AiChlFace
import com.AiChlFace.FACE_DETECT_RESULT
import com.AiChlIrFace.AiChlIrFace
import com.leessy.aifacecore.datas.FaceData
import com.leessy.aifacecore.datas.haveFaceData
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */


/**
 * 单彩色活体 检测
 */
fun Observable<FaceData>.Livings(): Observable<FaceData> {
    return map {
        if (!it.haveFaceData()) {//没有人脸的数据
            return@map it
        }
        Log.d(
            "LLLLL",
            "${it.nChannelNo == AiFaceChannelNo.COLORNo1}  ${it.width}  ${it.height}  ${it.RGB24}  ${it.detectResult}"
        )
        it.apply {
            if (it.imageColor == ImageColor.IR) {
                var start = System.currentTimeMillis()
                Livings = AiChlIrFace.LiveDetectOneCamera(
                    if (nChannelNo == AiFaceChannelNo.IRNo0) 0 else 2,
                    1,
                    width,
                    height,
                    RGB24,
                    detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                )
                Log.d("LLLLL", "活体时间 IR ： ${System.currentTimeMillis() - start}")

            } else if (it.imageColor == ImageColor.COLOR) {
                Log.d("LLLLL", "GetLiveFaceThreshold  ${AiChlFace.GetLiveFaceThreshold()}")
                val d = (detectResult as FACE_DETECT_RESULT)
                val lx = d.nLeftEyeX
                val ly = d.nLeftEyeY
                val rx = d.nRightEyeX
                val ry = d.nRightEyeY
                Log.d("LLLLL", "${lx}  ${ly}  ${rx}  ${ry}")

                var start = System.currentTimeMillis()
//                Livings =
//                    AiChlFace.LiveDetectEyesOneCamera(
//                        if (nChannelNo == AiFaceChannelNo.COLORNo1) 1 else 3,
//                        0,
//                        width,
//                        height,
//                        RGB24,
//                        (detectResult as FACE_DETECT_RESULT).nLeftEyeX,
//                        (detectResult as FACE_DETECT_RESULT).nLeftEyeY,
//                        (detectResult as FACE_DETECT_RESULT).nRightEyeX,
//                        (detectResult as FACE_DETECT_RESULT).nRightEyeY
//                    )

                Livings = AiChlFace.LiveDetectOneCamera(
                    if (nChannelNo == AiFaceChannelNo.COLORNo1) 1 else 3,
                    0,
                    width,
                    height,
                    RGB24,
                    detectResult as FACE_DETECT_RESULT
                )

                Log.d("LLLLL", "活体时间 ： ${System.currentTimeMillis() - start}")
            }
        }
    }
}




/**
 * 单彩色活体 检测
 */
fun Observable<FaceData>.LivingsIr(): Observable<FaceData> {
    return map {
        if (!it.haveFaceData()) {//没有人脸的数据
            return@map it
        }
        Log.d(
            "LLLLL",
            "${it.nChannelNo == AiFaceChannelNo.COLORNo1}  ${it.width}  ${it.height}  ${it.RGB24}  ${it.detectResult}"
        )
        it.apply {
            if (it.imageColor == ImageColor.IR) {
                var start = System.currentTimeMillis()
                Livings = AiChlIrFace.LiveDetectOneCamera(
                    if (nChannelNo == AiFaceChannelNo.IRNo0) 0 else 2,
                    1,
                    width,
                    height,
                    RGB24,
                    detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                )
                Log.d("LLLLL", "活体时间 IR ： ${System.currentTimeMillis() - start}")

            } else if (it.imageColor == ImageColor.COLOR) {
                Log.d("LLLLL", "GetLiveFaceThreshold  ${AiChlFace.GetLiveFaceThreshold()}")
                val d = (detectResult as FACE_DETECT_RESULT)
                val lx = d.nLeftEyeX
                val ly = d.nLeftEyeY
                val rx = d.nRightEyeX
                val ry = d.nRightEyeY
                Log.d("LLLLL", "${lx}  ${ly}  ${rx}  ${ry}")

                var start = System.currentTimeMillis()
//                Livings =
//                    AiChlFace.LiveDetectEyesOneCamera(
//                        if (nChannelNo == AiFaceChannelNo.COLORNo1) 1 else 3,
//                        0,
//                        width,
//                        height,
//                        RGB24,
//                        (detectResult as FACE_DETECT_RESULT).nLeftEyeX,
//                        (detectResult as FACE_DETECT_RESULT).nLeftEyeY,
//                        (detectResult as FACE_DETECT_RESULT).nRightEyeX,
//                        (detectResult as FACE_DETECT_RESULT).nRightEyeY
//                    )

                Livings = AiChlFace.LiveDetectOneCamera(
                    if (nChannelNo == AiFaceChannelNo.COLORNo1) 1 else 3,
                    0,
                    width,
                    height,
                    RGB24,
                    detectResult as FACE_DETECT_RESULT
                )

                Log.d("LLLLL", "活体时间 ： ${System.currentTimeMillis() - start}")
            }
        }
    }
}