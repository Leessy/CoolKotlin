package com.leessy.aifacecore.AiFaceCore

import android.content.Context
import android.util.Log
import com.AiChlFace.AiChlFace
import com.AiChlIrFace.AiChlIrFace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */

object AiFaceCore {
    private var AiFaceSDK: Boolean = false
    private var AiIrFaceSDK: Boolean = false
    lateinit var initMode: AiFaceType
    private val channelNum = 4//默认是全部使用各4通道
    lateinit var call: IAiFaceInitCall
    internal var AiChlFaceSize: Int = 0
    internal var AiChlIrFaceSize: Int = 0

    fun isInit() = AiFaceSDK && AiIrFaceSDK

//    fun getAiChlFaceSize(): Int {
//        return AiChlFaceSize
//    }
//
//    fun getAiChlIrFaceSize(): Int {
//        return AiChlFaceSize
//    }

    /**
     * 初始化人脸算法 ，默认使用时间授权
     */
    fun initAiFace(con: Context, type: AiFaceType = AiFaceType.MODE_DEBUG, calls: IAiFaceInitCall) {
//        this.con = con
        call = calls
        initMode = type
        init(con)
    }


    private fun init(mContext: Context) {
        GlobalScope.launch {
            if (!AiFaceSDK) {
                var ret = -100
                if (initMode == AiFaceType.MODE_DEBUG) {
                    ret = AiChlFace.InitDebug(mContext, channelNum)
                } else if (initMode == AiFaceType.MODE_DM2016) {
                    ret = AiChlFace.InitDm2016License(mContext, channelNum)
                } else if (initMode == AiFaceType.MODE_CARD) {
                    ret = AiChlFace.InitCardLicense(mContext, channelNum)
                }
                AiFaceSDK = ret == 0
                if (AiFaceSDK) AiChlFaceSize = AiChlFace.FeatureSize()

            }

            if (!AiIrFaceSDK) {
                var ret = -100
                if (initMode == AiFaceType.MODE_DEBUG) {
                    ret = AiChlIrFace.InitDebug(mContext, channelNum)
                } else if (initMode == AiFaceType.MODE_DM2016) {
                    ret = AiChlIrFace.InitDm2016License(mContext, channelNum)
                } else if (initMode == AiFaceType.MODE_CARD) {
                    ret = AiChlIrFace.InitCardLicense(mContext, channelNum)
                }
                AiIrFaceSDK = ret == 0
                if (AiIrFaceSDK) AiChlIrFaceSize = AiChlIrFace.FeatureSize()
            }
            call.call(AiFaceSDK, AiIrFaceSDK)
        }
    }


    /**
     * 使用读卡模块授权时 设置读取
     */
    fun setCardReader() {
        //todo 待实现读卡授权的逻辑

    }
}

