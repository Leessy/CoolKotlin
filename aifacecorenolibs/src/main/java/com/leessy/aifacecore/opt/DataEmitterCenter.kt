package com.leessy.aifacecore.opt

import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.datas.CameraData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
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
    private var faceSubjectColor1Dp: Disposable? = null
    private var faceSubjectColor2Dp: Disposable? = null
    private var faceSubjectIr1Dp: Disposable? = null
    private var faceSubjectIr2Dp: Disposable? = null

    private val faceSubjectColor1 = PublishSubject.create<CameraData>()
    private val faceSubjectColor2 = PublishSubject.create<CameraData>()
    private val faceSubjectIr1 = PublishSubject.create<CameraData>()
    private val faceSubjectIr2 = PublishSubject.create<CameraData>()

    internal fun getEmitter(imageColor: ImageColor = ImageColor.COLOR, CameraID: Int = 0): Observable<CameraData> {
        return checkout(imageColor, CameraID)
    }

    //筛选
    private fun checkout(imageColor: ImageColor, CameraID: Int): Observable<CameraData> {
        return when (imageColor) {
            ImageColor.COLOR -> if (CameraID == 0) {
                faceSubjectColor1.doOnSubscribe {
                    faceSubjectColor1Dp = if (faceSubjectColor1Dp != null) {
                        faceSubjectColor1Dp!!.dispose()
                        it
                    } else it
                }
            } else {
                faceSubjectColor2.doOnSubscribe {
                    faceSubjectColor2Dp = if (faceSubjectColor2Dp != null) {
                        faceSubjectColor2Dp!!.dispose()
                        it
                    } else it
                }
            }
            ImageColor.IR -> if (CameraID == 0) {
                faceSubjectIr1.doOnSubscribe {
                    faceSubjectIr1Dp = if (faceSubjectIr1Dp != null) {
                        faceSubjectIr1Dp!!.dispose()
                        it
                    } else it
                }
            } else {
                faceSubjectIr2.doOnSubscribe {
                    faceSubjectIr2Dp = if (faceSubjectIr2Dp != null) {
                        faceSubjectIr2Dp!!.dispose()
                        it
                    } else it
                }
            }
        }
    }

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
        when (imageColor) {
            ImageColor.COLOR -> if (CameraID == 0) {
                faceSubjectColor1
            } else {
                faceSubjectColor2
            }
            ImageColor.IR -> if (CameraID == 0) {
                faceSubjectIr1
            } else {
                faceSubjectIr2
            }
        }.apply {
            //检查算法是否初始化
            if (AiFaceCore.isInit()) {
                onNext(CameraData(byteArray, width, height, CameraID, imageColor).apply {
                    this.stream = stream
                    this.bMirror = bMirror
                    this.nRotate = nRotate
                })
            }
        }
    }

}