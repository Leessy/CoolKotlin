package com.leessy.aifacecore.opt

import com.leessy.aifacecore.datas.FRectData
import io.reactivex.Observable

/**
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */

fun Observable<FRectData>.FollowId(cameraID: Int): Observable<FRectData> {
    return filter { it.cameraId == cameraID }
}