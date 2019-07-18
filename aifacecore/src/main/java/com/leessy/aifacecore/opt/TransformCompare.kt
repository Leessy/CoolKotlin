package com.leessy.aifacecore.opt

import com.leessy.aifacecore.AiFaceCore.Compare.ComparList
import com.leessy.aifacecore.datas.FaceData
import com.leessy.aifacecore.datas.isColor
import com.leessy.aifacecore.datas.isIr
import com.leessy.aifacecore.datas.isReadyCompare
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */


/**
 * 人脸库识别  彩色库
 */
fun Observable<FaceData>.CompareListColor(): Observable<FaceData> {
    return map {
        if (it.isColor()) {
            var l = ComparList.ListCompare(it.nChannelNo.ordinal, it.feature!!)
            it.apply {
                CompareDataID = l[0]
                CompareRet = l[1]
            }
        } else it
    }
}

/**
 * 人脸库识别  红外库(暂时不做实现)
 */
//fun Observable<FaceData>.CompareListIr(): Observable<FaceData> {
//    return filter { it.isReadyCompare() }
//        .map {
//            if (it.isIr()) {
//                var l = ComparList.ListCompare(it.nChannelNo.ordinal, it.feature!!)
//                it.apply {
//                    CompareDataID = l[0]
//                    CompareRet = l[1]
//                }
//            } else it
//        }
//}