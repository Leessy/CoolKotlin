package com.leessy.aifacecore.opt

import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.datas.RectData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */

object FaceRectEmitterCenter {
    /**
     * 统一人脸分发中心
     */
    internal val faceSubject = PublishSubject.create<RectData>()

    /**
     * 发送人框
     */
    internal fun sendFaceRect(mageColor: ImageColor, cameraId: Int, faceresult: Any) {
        faceSubject.onNext(RectData(mageColor, cameraId).apply {
            transf(this, faceresult)
        })
    }


    /**
     * 指定id获取人脸数据
     *
     * 默认为 id 0
     */
    fun FaceRectObservableForID(imageColor: ImageColor = ImageColor.COLOR, camreraId: Int = 0): Observable<RectData> {
        return faceSubject.FollowId(imageColor, camreraId)
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