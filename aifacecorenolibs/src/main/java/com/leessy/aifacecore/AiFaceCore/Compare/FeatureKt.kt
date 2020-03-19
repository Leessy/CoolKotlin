package com.leessy.aifacecore.AiFaceCore.Compare

import com.leessy.aifacecore.AiFaceCore.AiFaceCore

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */


/**
 * 检测彩色特征码是否ok
 */
fun ITemp.isColorComplete(): Boolean {
    return getFeaturenNormalTemp().size == AiFaceCore.AiChlFaceSize
}

/**
 * 检测红外特征码是否ok
 */
fun ITemp.isIrComplete(): Boolean {
    return getFeaturenNormalTemp().size == AiFaceCore.AiChlFaceSize
}