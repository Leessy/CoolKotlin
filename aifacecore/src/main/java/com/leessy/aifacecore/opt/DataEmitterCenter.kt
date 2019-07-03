package com.leessy.aifacecore.opt

import com.leessy.aifacecore.datas.CameraData
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */

object DataEmitterCenter {
    /**
     * 统一人脸数据分发 构建中心
     */
    val faceSubject = PublishSubject.create<CameraData>()

    @Synchronized
    internal fun buidler(
        byteArray: ByteArray,
        width: Int,
        height: Int,
        CameraID: Int = 0,
        imageColor: ImageColor,
        stream: Int,
        bMirror: Int,
        nRotate: Int
    ) {
        faceSubject.onNext(CameraData(byteArray, width, height, CameraID, imageColor).apply {
            this.stream = stream
            this.bMirror = bMirror
            this.nRotate = nRotate
        })
    }

}