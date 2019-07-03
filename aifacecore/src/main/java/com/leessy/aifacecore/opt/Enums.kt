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
