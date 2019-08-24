package com.leessy.aifacecore.opt

import com.AiChlFace.FACE_DETECT_RESULT
import com.leessy.aifacecore.datas.FaceData
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
    return when (imageColor) {
        ImageColor.COLOR ->
            abs((detectResult as FACE_DETECT_RESULT).nQuality) > miniQuality
        ImageColor.IR ->
            abs((detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).nQuality) > miniQuality
    }
}

/**
 * 质量过滤  离边缘最少距离  横向 竖向
 */
fun FaceData.faceEdgeFilter(miniEdgeHorizontal: Int, miniEdgeVertical: Int): Boolean {
    if (detectResult == null) return false
    return when (imageColor) {
        ImageColor.COLOR ->
            (detectResult as FACE_DETECT_RESULT).nFaceLeft >= miniEdgeHorizontal
                    && width - (detectResult as FACE_DETECT_RESULT).nFaceRight >= miniEdgeHorizontal
                    && (detectResult as FACE_DETECT_RESULT).nFaceTop >= miniEdgeVertical
                    && height - (detectResult as FACE_DETECT_RESULT).nFaceBottom >= miniEdgeVertical

        ImageColor.IR ->
            (detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).nFaceLeft >= miniEdgeHorizontal
                    && width - (detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).nFaceRight >= miniEdgeHorizontal
                    && (detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).nFaceTop >= miniEdgeVertical
                    && height - (detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).nFaceBottom >= miniEdgeVertical
    }
}

/**
 * 角度计算
 */
fun FaceData.faceAngleFilter(maxAngle: Int): Boolean {
    if (detectResult == null) return false
    return when (imageColor) {
        ImageColor.COLOR ->
            abs((detectResult as FACE_DETECT_RESULT).nAngleYaw) < maxAngle
                    && abs((detectResult as FACE_DETECT_RESULT).nAnglePitch) < maxAngle
                    && abs((detectResult as FACE_DETECT_RESULT).nAngleRoll) < maxAngle
        ImageColor.IR ->
            abs((detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).nAngleYaw) < maxAngle
                    && abs((detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).nAnglePitch) < maxAngle
                    && abs((detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).nAngleRoll) < maxAngle
    }
}

/**
 * 大小计算 宽度 最小值
 */
fun FaceData.faceWidthFilterMin(minWidth: Int): Boolean {
    if (detectResult == null) return false
    return when (imageColor) {
        ImageColor.COLOR ->
            abs((detectResult as FACE_DETECT_RESULT).run {
                nFaceLeft - nFaceRight
            }) >= minWidth
        ImageColor.IR ->
            abs((detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).run {
                nFaceLeft - nFaceRight
            }) >= minWidth
    }
}

/**
 * 大小计算 宽度  最大值
 */
fun FaceData.faceWidthFilterMax(maxWidth: Int): Boolean {
    if (detectResult == null) return false
    return when (imageColor) {
        ImageColor.COLOR ->
            abs((detectResult as FACE_DETECT_RESULT).run {
                nFaceLeft - nFaceRight
            }) <= maxWidth
        ImageColor.IR ->
            abs((detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).run {
                nFaceLeft - nFaceRight
            }) <= maxWidth
    }
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
        it.faceAngleFilter(maxAngle).apply {
            if (!this) {
                call?.call(AiFaceFilter.FACE_ANGLE)
            }
        }
                && it.faceWidthFilterMin(minWidth).apply {
            if (!this) {
                call?.call(AiFaceFilter.FACE_WIDTH_MINI)
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
                val ofs = (t2.detectResult as FACE_DETECT_RESULT).run {
                    if (offset > 0.01 || offset < 0.5) {
                        ((nFaceRight - nFaceLeft) * offset).toInt()//超出设置值 将使用默认值
                    } else
                        ((nFaceRight - nFaceLeft) * 0.1).toInt()
                }
                abs((t2.detectResult as FACE_DETECT_RESULT).run {
                    nFaceLeft + (nFaceRight - nFaceLeft) / 2
                } - (t1.detectResult as FACE_DETECT_RESULT).run {
                    nFaceLeft + (nFaceRight - nFaceLeft) / 2
                }) < ofs

                        && abs((t2.detectResult as FACE_DETECT_RESULT).run {
                    nFaceBottom + (nFaceTop - nFaceBottom) / 2
                } - (t1.detectResult as FACE_DETECT_RESULT).run {
                    nFaceBottom + (nFaceTop - nFaceBottom) / 2
                }) < ofs
            }

            ImageColor.IR -> {
                val ofs = (t2.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).run {
                    if (offset > 0.01 || offset < 0.5) {
                        ((nFaceRight - nFaceLeft) * offset).toInt()//超出设置值 将使用默认值
                    } else
                        ((nFaceRight - nFaceLeft) * 0.1).toInt()
                }
                abs((t2.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).run {
                    nFaceLeft + (nFaceRight - nFaceLeft) / 2
                } - (t1.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).run {
                    nFaceLeft + (nFaceRight - nFaceLeft) / 2
                }) < ofs
                        && abs((t2.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).run {
                    nFaceBottom + (nFaceTop - nFaceBottom) / 2
                } - (t1.detectResult as com.AiChlIrFace.FACE_DETECT_RESULT).run {
                    nFaceBottom + (nFaceTop - nFaceBottom) / 2
                }) < ofs
            }
        }
    }.skip(1)
}
