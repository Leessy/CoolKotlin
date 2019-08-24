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
 * 单彩色活体 检测 有一次通过后，后续默认为通过
 */
fun Observable<FaceData>.LivingsSinglePass(): Observable<FaceData> {
    var b = false
    return map {
        if (!it.haveFaceData()) {//没有人脸的数据
            return@map it
        }
        Log.d("LLLLLbbbbb", "${b}")
        if (b) {//本轮活体已经通过
            it.Livings = 1
            return@map it
        }
        it.apply {
            if (it.imageColor == ImageColor.IR) {
                val start = System.currentTimeMillis()
                Livings = AiChlIrFace.LiveDetectOneCamera(
                    if (nChannelNo == AiFaceChannelNo.IRNo0) 0 else 2,
                    1,
                    width,
                    height,
                    RGB24,
                    detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                )
                Log.d("Livings", "活体时间 IR ： ${System.currentTimeMillis() - start}")

            } else if (it.imageColor == ImageColor.COLOR) {
                Log.d("Livings", "GetLiveFaceThreshold  ${AiChlFace.GetLiveFaceThreshold()}")
                val start = System.currentTimeMillis()
                Livings = AiChlFace.LiveDetectOneCamera(
                    if (nChannelNo == AiFaceChannelNo.COLORNo1) 1 else 3,
                    0,
                    width,
                    height,
                    RGB24,
                    detectResult as FACE_DETECT_RESULT
                )
                Log.d("Livings", "活体时间 ： ${System.currentTimeMillis() - start}")
            }
            b = Livings == 1
        }
    }
}

/**
 * 单彩色活体 检测
 */
fun Observable<FaceData>.Livings(): Observable<FaceData> {
    return map {
        if (!it.haveFaceData()) {//没有人脸的数据
            return@map it
        }
        it.apply {
            if (it.imageColor == ImageColor.IR) {
                val start = System.currentTimeMillis()
                Livings = AiChlIrFace.LiveDetectOneCamera(
                    if (nChannelNo == AiFaceChannelNo.IRNo0) 0 else 2,
                    1,
                    width,
                    height,
                    RGB24,
                    detectResult as com.AiChlIrFace.FACE_DETECT_RESULT
                )
                Log.d("Livings", "活体时间 IR ： ${System.currentTimeMillis() - start}")

            } else if (it.imageColor == ImageColor.COLOR) {
                Log.d("Livings", "GetLiveFaceThreshold  ${AiChlFace.GetLiveFaceThreshold()}")
                val start = System.currentTimeMillis()
                Livings = AiChlFace.LiveDetectOneCamera(
                    if (nChannelNo == AiFaceChannelNo.COLORNo1) 1 else 3,
                    0,
                    width,
                    height,
                    RGB24,
                    detectResult as FACE_DETECT_RESULT
                )
                Log.d("Livings", "活体时间 ： ${System.currentTimeMillis() - start}")
            }
        }
    }
}
