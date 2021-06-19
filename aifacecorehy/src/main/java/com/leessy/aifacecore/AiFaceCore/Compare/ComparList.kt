package com.leessy.aifacecore.AiFaceCore.Compare

import com.huiyuenet.faceCheck.FaceCheck
import com.huiyuenet.faceCheck.FaceFunction
import io.reactivex.schedulers.Schedulers

/**
 * @author 刘承.
 * @date 2018/2/1
 */
object ComparList {
    var TAG = "ComparList"

    /**
     * 是否已创建对比列表
     */

//    private var hList: Int = 0//人脸sdk 初始化句柄
//    private val tempDataId = ArrayList<Long>()
    private val setId = linkedSetOf<Long>()//去重用

//    private val nPos = IntArray(1)//ListInsert 固定参数
//    var pos: Int = 0
//    private lateinit var nScores: ByteArray
//    private var comparmax = 1000//创建的对比列表大小 默认以最大值创建

    /******************使用算法库******************/
    /**
     * 初始化数据 启动1：N内存
     */
    fun initCompareList(max: Int): Boolean {
//        if (!AiFaceCore.isInit()) return isInit
//        if (isInit) {
//            return isInit
//        }
//        comparmax = max
//        hList = AiChlFace.ListCreate(comparmax)//创建容纳列表
//        nScores = ByteArray(comparmax)//返回的相似度参数顺序同列表
//        logd(TAG, "AifaceON: 创建列表返回值句柄=$hList")
//        isInit = hList != 0
        FaceCheck.clearFeature()
        return true
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
//        val rets = LongArray(2)
//        rets[0] = 0L
//        rets[1] = 0L
//        if (!isInit || pos <= 0) return rets   //对比列表没有特征  或者没有初始化
//        for (j in 0 until nScores.size) nScores[j] = 0  //重置列表值
        val f = FaceFunction.faceComparison1ToNMem(temple)
        if (f[0] > 0 && f[1] > 0) {
            val toInt = f[0].toInt()
            setId.forEachIndexed { index, l ->
                if (index == toInt) {
                    f[0] = l
                }
            }
        }
        return f

//        val ret = AiChlFace.ListCompare(nChannelNo, hList, temple, 0, 0, nScores)
//        logd(TAG, "comparison: 对比成功  列表总数=$pos   对比返回参与总数ret=$ret")
//        if (ret != pos) return rets
//        for (i in 0 until pos) {
//            if (nScores[i] > rets[1]) {
//                rets[0] = tempDataId[i]
//                rets[1] = nScores[i].toLong()
//            }
//        }
//        return rets
    }

    /**
     * 批量 增加模版到对比列表
     */
    @Synchronized
    fun addTempList(itempList: List<ITemp>?) {
        if (itempList.isNullOrEmpty()) {
            return
        }
        Schedulers.io().scheduleDirect {
            itempList.map {
                addTemp(it)
            }
        }
    }

    /**
     * 增加模版到对比列表::新增或者更新
     */
    @Synchronized
    fun addTemp(itemp: ITemp) {
        if (itemp.isColorComplete()) {
            if (setId.add(itemp.getDataBaseID())) {//添加成功说明此ID没有，直接新增到最后
                val r = FaceCheck.addFeature(setId.size - 1, itemp.getFeaturenNormalTemp())
                if (r != 0) {
                    setId.remove(itemp.getDataBaseID())
                }
            }
        } else {//已经有这个人的数据，那么找到index 直接更新
            setId.forEachIndexed { index, l ->
                if (l == itemp.getDataBaseID()) FaceCheck.addFeature(
                    index,
                    itemp.getFeaturenNormalTemp()
                )
            }
        }
    }

    /**
     * 更新对比列表
     * 重新加载
     */
    fun upDateComparlist(itempList: List<ITemp>) {
        clearAllComparisonlist()
        addTempList(itempList)
    }


//    /**
//     * 增加一个模版 到对比列表  默认添加到末尾
//     *
//     * @param bytes fe
//     * @return
//     */
//    @Synchronized
//    private fun insertTemp(bytes: ByteArray): Int {
//        return AiChlFace.ListInsert(hList, nPos, bytes, 1)
//    }

    /**
     * 删除单个模版
     * hList ---- 要删除特征的特征比对列表句柄
     * nPos ---- 要删除的特征的起始位置
     * nFeatureNum ---- 要删除的特征数量
     *
     * @return result
     */
//    @Synchronized
//    fun ListRemove(id: Long): Boolean {
    // TODO: 2021/6/19  特征码不能单个删除，如果处理？？？？？？？？？？？？？？？艹4
    // set删除id后下标前移，特征码库如何处理
//        var e = -1
//        for (i in tempDataId.indices) {
//            if (tempDataId[i] == id) {
//                e = i
//                break
//            }
//        }
//        if (e == -1) {
//            return false
//        }
//        tempDataId.removeAt(e)
//        val i = AiChlFace.ListRemove(hList, e, 1)
//        return if (i == pos - 1) {
//            pos--
//            true
//        } else {
//            pos++
//            false
//        }
//    }

    /**
     * 清空对比列表
     */
    @Synchronized
    fun clearAllComparisonlist() {
        FaceCheck.clearFeature()
    }

    /**
     * 销毁对比列表
     */
    fun destroyComparList() {
    }
}

