package com.leessy.aifacecore.AiFaceCore.Compare

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */
interface ITemp {
    fun getDataBaseID(): Long

    fun getFeaturenNormalTemp(): ByteArray //彩色

    fun getFeaturenIrTemp(): ByteArray //红外
}