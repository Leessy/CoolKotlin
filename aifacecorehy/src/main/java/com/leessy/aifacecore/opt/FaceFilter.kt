package com.leessy.aifacecore.opt

import com.leessy.aifacecore.datas.FaceData
import com.leessy.aifacecore.datas.RectData
import com.leessy.aifacecore.datas.isNoneFace
import io.reactivex.Observable
import kotlin.math.abs

/**
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */


/**
 * 质量过滤
 */
fun FaceData.faceQualityFilter(miniQuality: Int): Boolean {
    if (detectResult == null) return false
    return getRectData().nQuality > miniQuality
//    return when (imageColor) {
//        ImageColor.COLOR ->
//            abs(getRectData().nQuality) > miniQuality
//        ImageColor.IR ->
//            abs(getRectData().nQuality) > miniQuality
//    }
}

/**
 * 质量过滤
 */
fun RectData.faceQualityFilter(miniQuality: Int): Boolean {
    return nQuality > miniQuality
}

/**
 * 质量过滤  离边缘最少距离  横向 竖向
 */
fun FaceData.faceEdgeFilter(miniEdgeHorizontal: Int, miniEdgeVertical: Int): Boolean {
    if (detectResult == null) return false
    return getRectData().nFaceLeft >= miniEdgeHorizontal
            && width - getRectData().nFaceRight >= miniEdgeHorizontal
            && getRectData().nFaceTop >= miniEdgeVertical
            && height - getRectData().nFaceBottom >= miniEdgeVertical
//    return detectResult.return when (imageColor) {
//        ImageColor.COLOR ->
//            getRectData().nFaceLeft >= miniEdgeHorizontal
//                    && width - getRectData().nFaceRight >= miniEdgeHorizontal
//                    && getRectData().nFaceTop >= miniEdgeVertical
//                    && height - getRectData().nFaceBottom >= miniEdgeVertical
//
//        ImageColor.IR ->
//            getRectData().nFaceLeft >= miniEdgeHorizontal
//                    && width - getRectData().nFaceRight >= miniEdgeHorizontal
//                    && getRectData().nFaceTop >= miniEdgeVertical
//                    && height - getRectData().nFaceBottom >= miniEdgeVertical
//    }
}

/**
 * 质量过滤  离边缘最少距离  横向 竖向
 */
fun RectData.faceEdgeFilter(miniEdgeHorizontal: Int, miniEdgeVertical: Int): Boolean {
    return nFaceLeft >= miniEdgeHorizontal
            && width - nFaceRight >= miniEdgeHorizontal
            && nFaceTop >= miniEdgeVertical
            && height - nFaceBottom >= miniEdgeVertical
}

/**
 * 角度计算
 */
fun FaceData.faceAngleFilter(maxAngle: Int): Boolean {
    if (detectResult == null) return false
    return abs(getRectData().nAngleYaw) < maxAngle
            && abs(getRectData().nAnglePitch) < maxAngle
            && abs(getRectData().nAngleRoll) < maxAngle

}

/**
 * 角度计算
 */
fun RectData.faceAngleFilter(maxAngle: Int): Boolean {
    return abs(nAngleYaw) < maxAngle
            && abs(nAnglePitch) < maxAngle
            && abs(nAngleRoll) < maxAngle
}

/**
 * 大小计算 宽度 最小值
 */
fun FaceData.faceWidthFilterMin(minWidth: Int): Boolean {
    if (detectResult == null) return false
    return when (imageColor) {
        ImageColor.COLOR ->
            abs(getRectData().run {
                nFaceLeft - nFaceRight
            }) >= minWidth
        ImageColor.IR ->
            abs(getRectData().run {
                nFaceLeft - nFaceRight
            }) >= minWidth
    }
}

/**
 * 大小计算 宽度 最小值
 */
fun RectData.faceWidthFilterMin(minWidth: Int): Boolean {
    return abs(nFaceLeft - nFaceRight) >= minWidth
}

/**
 * 大小计算 宽度  最大值
 */
fun FaceData.faceWidthFilterMax(maxWidth: Int): Boolean {
    if (detectResult == null) return false
    return when (imageColor) {
        ImageColor.COLOR ->
            abs(getRectData().run {
                nFaceLeft - nFaceRight
            }) <= maxWidth
        ImageColor.IR ->
            abs(getRectData().run {
                nFaceLeft - nFaceRight
            }) <= maxWidth
    }
}

/**
 * 大小计算 宽度  最大值
 */
fun RectData.faceWidthFilterMax(maxWidth: Int): Boolean {
    return abs(nFaceLeft - nFaceRight) <= maxWidth
}

/**
 * 条件过滤
 */
fun Observable<FaceData>.faceFilter(
    maxAngle: Int,
    minWidth: Int,
    maxWidth: Int,
    miniEdgeHorizontal: Int,
    miniEdgeVertical: Int,
    miniQuality: Int,
    call: IFaceFitersCall? = null
): Observable<FaceData> {
    return filter {
        it.faceWidthFilterMin(minWidth).apply {
            if (!this) {
                call?.call(AiFaceFilter.FACE_WIDTH_MINI)
            }
        }
                && it.faceAngleFilter(maxAngle).apply {
            if (!this) {
                call?.call(AiFaceFilter.FACE_ANGLE)
            }
        }
                && it.faceWidthFilterMax(maxWidth).apply {
            if (!this) {
                call?.call(AiFaceFilter.FACE_WIDTH_MAX)
            }
        }
                && it.faceEdgeFilter(miniEdgeHorizontal, miniEdgeVertical).apply {
            if (!this) {
                call?.call(AiFaceFilter.FACE_EDGE)
            }
        }
                && it.faceQualityFilter(miniQuality).apply {
            if (!this) {
                call?.call(AiFaceFilter.FACE_QUALITY)
            }
        }
    }
}

/**
 * 前后帧过滤 偏移量   人脸宽的百分比  超出设置值 将使用默认值0.1
 */
fun Observable<FaceData>.faceOffsetFilter(offset: Float): Observable<FaceData> {
    return distinctUntilChanged { t1, t2 ->
        return@distinctUntilChanged !when (t1.imageColor) {
            ImageColor.COLOR -> {
                val ofs = (t2.getRectData()).run {
                    if (offset > 0.01 || offset < 0.5) {
                        ((nFaceRight - nFaceLeft) * offset).toInt()//超出设置值 将使用默认值
                    } else
                        ((nFaceRight - nFaceLeft) * 0.1).toInt()
                }
                abs((t2.getRectData()).run {
                    nFaceLeft + (nFaceRight - nFaceLeft) / 2
                } - (t1.getRectData()).run {
                    nFaceLeft + (nFaceRight - nFaceLeft) / 2
                }) < ofs

                        && abs((t2.getRectData()).run {
                    nFaceBottom + (nFaceTop - nFaceBottom) / 2
                } - (t1.getRectData()).run {
                    nFaceBottom + (nFaceTop - nFaceBottom) / 2
                }) < ofs
            }

            ImageColor.IR -> {
                val ofs = (t2.getRectData()).run {
                    if (offset > 0.01 || offset < 0.5) {
                        ((nFaceRight - nFaceLeft) * offset).toInt()//超出设置值 将使用默认值
                    } else
                        ((nFaceRight - nFaceLeft) * 0.1).toInt()
                }
                abs((t2.getRectData()).run {
                    nFaceLeft + (nFaceRight - nFaceLeft) / 2
                } - (t1.getRectData()).run {
                    nFaceLeft + (nFaceRight - nFaceLeft) / 2
                }) < ofs
                        && abs((t2.getRectData()).run {
                    nFaceBottom + (nFaceTop - nFaceBottom) / 2
                } - (t1.getRectData()).run {
                    nFaceBottom + (nFaceTop - nFaceBottom) / 2
                }) < ofs
            }
        }
    }.skip(1)
}


/**
 * 人脸过滤位置判断，使用人脸框
 */
fun Observable<RectData>.FaceFilterCalculate(
    maxAngle: Int,
    minWidth: Int,
    maxWidth: Int,
    miniEdgeHorizontal: Int,
    miniEdgeVertical: Int,
    miniQuality: Int
): Observable<RectData> {
    return map {
        if (!it.isNoneFace()) {
            it.FaceFilterCalculate(
                maxAngle,
                minWidth,
                maxWidth,
                miniEdgeHorizontal,
                miniEdgeVertical,
                miniQuality
            )
        }
        it
    }
}

fun RectData.FaceFilterCalculate(
    maxAngle: Int,
    minWidth: Int,
    maxWidth: Int,
    miniEdgeHorizontal: Int,
    miniEdgeVertical: Int,
    miniQuality: Int
): RectData {
    return also {
        it.faceWidthFilterMin(minWidth).apply {
            if (!this) {
                faceFilterRet = AiFaceFilter.FACE_WIDTH_MINI
            }
        }
                && it.faceAngleFilter(maxAngle).apply {
            if (!this) {
                faceFilterRet = AiFaceFilter.FACE_ANGLE
            }
        }
                && it.faceWidthFilterMax(maxWidth).apply {
            if (!this) {
                faceFilterRet = AiFaceFilter.FACE_WIDTH_MAX
            }
        }
                && it.faceEdgeFilter(miniEdgeHorizontal, miniEdgeVertical).apply {
            if (!this) {
                faceFilterRet = AiFaceFilter.FACE_EDGE
            }
        }
                && it.faceQualityFilter(miniQuality).apply {
            if (!this) {
                faceFilterRet = AiFaceFilter.FACE_QUALITY
            }
        }
    }

}
