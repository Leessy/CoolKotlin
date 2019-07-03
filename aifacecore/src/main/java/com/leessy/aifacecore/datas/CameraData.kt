package com.leessy.aifacecore.datas

import com.leessy.aifacecore.opt.ImageColor

/**
 *
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 *
 * 相机数据 实体类
 */
class CameraData(
    val byteArray: ByteArray,
    val width: Int,
    val height: Int,
    var CameraID: Int = 0,
    var imageColor: ImageColor = ImageColor.COLOR  //给默认值
) {
    var stream = 2  //输入源图象数据格式（0：YUV420P, 1: NV12，2: NV21） 根据算法定义值  默认 2
    var bMirror = 0   //左右镜象（相对于旋转后的图象，0：左右不镜象，1：左右镜象）
    var nRotate = 0   //旋转方式（对输入源图象旋转，0：不旋转，1：左旋90度，2：右旋90度）

}