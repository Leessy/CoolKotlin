package com.leessy.aifacecore.opt

/**
 * 源数据  图片类型  红外图 或 彩色图
 */
enum class ImageColor {
    COLOR, IR
}

/**
 * 人脸算法通道 指定值（现默认使用 4个通道）
 */
enum class AiFaceChannelNo {
    IRNo0, COLORNo1, IRNo2, COLORNo3
}

/**
 * 人脸过滤未通过类型
 */
enum class AiFaceFilter {
    FACE_ANGLE, FACE_WIDTH_MINI, FACE_WIDTH_MAX, FACE_QUALITY, FACE_EDGE
}
