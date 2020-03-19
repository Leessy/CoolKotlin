package com.leessy.aifacecore.opt

import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.datas.RectData
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */

object FaceRectEmitterCenter {
    var dsp: Disposable? = null
    var lastFaceisNullColor = true//最后一个不是空人脸数据
    var lastFaceisNullIr = true//最后一个不是空人脸数据

    /**
     * 统一人脸分发中心
     */
    private val faceSubject = PublishSubject.create<RectData>()

    /**
     * 发送人框
     */
    internal fun sendFaceRect(
        mageColor: ImageColor,
        cameraId: Int,
        w: Int,
        h: Int,
        faceresult: Any
    ) {
        if (mageColor == ImageColor.COLOR) lastFaceisNullColor = true else lastFaceisNullIr = true
        faceSubject.onNext(RectData(mageColor, cameraId = cameraId, width = w, height = h).apply {
            transf(this, faceresult)
        })
    }

    /**
     * 发送一个空人脸框
     */
    internal fun sendFaceRect(mageColor: ImageColor, cameraId: Int) {
        if (mageColor == ImageColor.COLOR) {
            if (lastFaceisNullColor) {
                lastFaceisNullColor = false
                faceSubject.onNext(RectData(mageColor, cameraId))
            }
        } else {
            if (lastFaceisNullIr) {
                lastFaceisNullIr = false
                faceSubject.onNext(RectData(mageColor, cameraId))
            }
        }

    }

    fun getEmitter(): Observable<RectData> {
        return faceSubject.doOnSubscribe {
            if (dsp != null) dsp!!.dispose()
            dsp = it
        }
    }

    /**
     * 指定id获取人脸数据
     *
     * 默认为 id 0
     */
    fun FaceRectObservableForID(imageColor: ImageColor, camreraId: Int): Observable<RectData> {
        return getEmitter()
    }


    /**
     * 解析转换人脸数据
     */
    private fun transf(f: RectData, faceresult: Any) {
        f.run {
            if (faceresult is FACE_DETECT_RESULT) {
                nFaceLeft = faceresult.nFaceLeft
                nFaceTop = faceresult.nFaceTop
                nFaceRight = faceresult.nFaceRight
                nFaceBottom = faceresult.nFaceBottom
                nLeftEyeX = faceresult.nLeftEyeX
                nLeftEyeY = faceresult.nLeftEyeY
                nRightEyeX = faceresult.nRightEyeX
                nRightEyeY = faceresult.nRightEyeY
                nMouthX = faceresult.nMouthX
                nMouthY = faceresult.nMouthY
                nNoseX = faceresult.nNoseX
                nNoseY = faceresult.nNoseY
                nAngleYaw = faceresult.nAngleYaw
                nAnglePitch = faceresult.nAnglePitch
                nAngleRoll = faceresult.nAngleRoll
                nQuality = faceresult.nQuality
            } else if (faceresult is com.AiChlIrFace.FACE_DETECT_RESULT) {
                nFaceLeft = faceresult.nFaceLeft
                nFaceTop = faceresult.nFaceTop
                nFaceRight = faceresult.nFaceRight
                nFaceBottom = faceresult.nFaceBottom
                nLeftEyeX = faceresult.nLeftEyeX
                nLeftEyeY = faceresult.nLeftEyeY
                nRightEyeX = faceresult.nRightEyeX
                nRightEyeY = faceresult.nRightEyeY
                nMouthX = faceresult.nMouthX
                nMouthY = faceresult.nMouthY
                nNoseX = faceresult.nNoseX
                nNoseY = faceresult.nNoseY
                nAngleYaw = faceresult.nAngleYaw
                nAnglePitch = faceresult.nAnglePitch
                nAngleRoll = faceresult.nAngleRoll
                nQuality = faceresult.nQuality
            }
        }
    }
}