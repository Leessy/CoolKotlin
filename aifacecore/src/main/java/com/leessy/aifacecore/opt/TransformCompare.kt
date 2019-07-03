package com.leessy.aifacecore.opt

import com.leessy.aifacecore.AiFaceCore.Compare.ComparList
import com.leessy.aifacecore.datas.FaceData
import com.leessy.aifacecore.datas.isReadyCompare
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */


/**
 * 人脸库识别
 */
fun Observable<FaceData>.CompareList(): Observable<FaceData> {
    return filter { it.isReadyCompare() }
        .map {
            //TODO 可根据类行自动选择彩色 或 红外库识别
            var l = ComparList.ListCompare(it.nChannelNo.ordinal, it.Feature!!)
            it.apply {
                CompareDataID = l[0]
                CompareRet = l[1]
            }
        }
}