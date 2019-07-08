package com.leessy.aifacecore.AiFaceCore.Compare

import android.util.Log
import com.AiChlFace.AiChlFace
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author 刘承.
 * @date 2018/2/1
 */
object ComparList {
    var TAG = "ComparList"

    /**
     * 是否已创建对比列表
     */
    var isInit = false

    private var hList: Int = 0//人脸sdk 初始化句柄
    private val tempDataId = ArrayList<Long>()
    private val nPos = IntArray(1)//ListInsert 固定参数
    var pos: Int = 0
        private set//记录导入次数   判断是否导入成功
    private lateinit var nScores: ByteArray
    private var comparmax = 1000//创建的对比列表大小 默认以最大值创建

    /**
     * 初始化数据 启动1：N内存
     */
    fun initCompareList(max: Int): Boolean {
        if (!AiFaceCore.isInit()) return isInit
        if (isInit) {
            return isInit
        }
        comparmax = max
        hList = AiChlFace.ListCreate(comparmax)//创建容纳列表
        nScores = ByteArray(comparmax)//返回的相似度参数顺序同列表
        Log.d(TAG, "AifaceON: 创建列表返回值句柄=$hList")
        isInit = hList != 0
        return isInit
    }


    /**
     * 进行 可见光库对比 返回数据库ID 失败返回-1
     *
     * @param nChannelNo 对比通道
     * @param temple     模版
     * @return LongArray  -->index 0 对应数据库id , -->index 1 对比结果
     */

    @Synchronized
    fun ListCompare(nChannelNo: Int, temple: ByteArray): LongArray {
        val rets = LongArray(2)
        rets[0] = 0L
        rets[1] = 0L
        if (!isInit || pos <= 0) return rets   //对比列表没有特征  或者没有初始化
        for (j in 0 until nScores.size) nScores[j] = 0  //重置列表值

        val ret = AiChlFace.ListCompare(nChannelNo, hList, temple, 0, 0, nScores)
        Log.d(TAG, "comparison: 对比成功  列表总数=$pos   对比返回参与总数ret=$ret")
        if (ret != pos) return rets
        for (i in 0 until pos) {
            if (nScores[i] > rets[1]) {
                rets[0] = tempDataId[i]
                rets[1] = nScores[i].toLong()
            }
        }
        return rets
    }

    /**
     * 批量 增加模版到对比列表
     */
    @Synchronized
    fun addTempList(itempList: List<ITemp>?) {
        if (!isInit || itempList.isNullOrEmpty()) {
            return
        }
        GlobalScope.launch {
            itempList.map {
                addTemp(it)
            }
        }
    }

    /**
     * 增加模版到对比列表
     */
    @Synchronized
    fun addTemp(itemp: ITemp) {
        if (itemp.isColorComplete()) {
            if (++pos == insertTemp(itemp.getFeaturenNormalTemp())) {
                tempDataId.add(itemp.getDataBaseID())
                return
            }
            pos--
        }
    }

    /**
     * 更新对比列表
     * 重新加载
     */
    fun upDateComparlist(itempList: List<ITemp>) {
        if (!isInit || itempList.size <= 0) return
        clearAllComparisonlist()
        addTempList(itempList)
    }


    /**
     * 增加一个模版 到对比列表  默认添加到末尾
     *
     * @param bytes fe
     * @return
     */
    @Synchronized
    private fun insertTemp(bytes: ByteArray): Int {
        nPos[0] = -1
        return AiChlFace.ListInsert(hList, nPos, bytes, 1)
    }

    /**
     * 删除单个模版
     * hList ---- 要删除特征的特征比对列表句柄
     * nPos ---- 要删除的特征的起始位置
     * nFeatureNum ---- 要删除的特征数量
     *
     * @return result
     */
    @Synchronized
    fun ListRemove(id: Long): Boolean {
        if (!isInit) return false
        var e = -1
        for (i in tempDataId.indices) {
            if (tempDataId[i] == id) {
                e = i
                break
            }
        }
        if (e == -1) {
            return false
        }
        tempDataId.removeAt(e)
        val i = AiChlFace.ListRemove(hList, e, 1)
        return if (i == pos - 1) {
            pos--
            true
        } else {
            pos++
            false
        }
    }

    /**
     * 清空对比列表
     */
    @Synchronized
    fun clearAllComparisonlist() {
        if (!isInit) return
        AiChlFace.ListClearAll(hList)
        tempDataId.clear()
        pos = 0
    }

    /**
     * 销毁对比列表
     */
    fun destroyComparList() {
        if (!isInit) return
        AiChlFace.ListDestroy(hList)
        tempDataId.clear()
        isInit = false
    }
}

