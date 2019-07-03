package com.leessy.aifacecore.opt

import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.datas.FRectData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */

object FaceRectCenter {
    /**
     * 统一人脸分发中心
     */
    private val faceSubject = PublishSubject.create<FRectData>()

    /**
     * 发送人框
     */
    internal fun sendFaceRect(cameraId: Int, faceresult: Any) {
        faceSubject.onNext(FRectData(cameraId = cameraId).apply {
            transf(this, faceresult)
        })
    }


    /**
     * 获取人脸框数据
     */
    fun FaceRectObservable(): Observable<FRectData> {
        return faceSubject
    }


    /**
     * 指定id获取人脸数据
     *
     * 默认为 id 0
     */
    fun FaceRectObservableForID(camreraId: Int = 0): Observable<FRectData> {
        return faceSubject.FollowId(cameraID = camreraId)
    }


    /**
     * 解析转换人脸数据
     */
    private fun transf(f: FRectData, faceresult: Any) {
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