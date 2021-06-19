package com.leessy.aifacecore.opt

import com.huiyuenet.faceCheck.THFI_FacePos
import com.leessy.aifacecore.datas.RectData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
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
        faceresult: THFI_FacePos
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
     fun transf(f: RectData, faceresult: THFI_FacePos) {
        f.run {
            nFaceLeft = faceresult.rcFace.left
            nFaceTop = faceresult.rcFace.top
            nFaceRight = faceresult.rcFace.right
            nFaceBottom = faceresult.rcFace.bottom
            nLeftEyeX = faceresult.ptLeftEye.x
            nLeftEyeY = faceresult.ptLeftEye.x
            nRightEyeX = faceresult.ptRightEye.x
            nRightEyeY = faceresult.ptRightEye.y
            nMouthX = faceresult.ptMouth.x
            nMouthY = faceresult.ptMouth.y
            nNoseX = faceresult.ptNose.x
            nNoseY = faceresult.ptNose.y
            nAngleYaw = faceresult.fAngle.yaw
            nAnglePitch = faceresult.fAngle.pitch
            nAngleRoll = faceresult.fAngle.roll
            nQuality = (faceresult.fAngle.confidence * 100F).toInt()//人脸的质量，业务代码从0-100判断
        }
    }
}