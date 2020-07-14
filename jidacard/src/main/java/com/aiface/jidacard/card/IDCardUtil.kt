package com.aiface.jidacard.card

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.util.Log
import com.lzw.qlhsshare.Tool
import com.lzw.qlhsshare.Wlt2bmpShare
import com.pboc.TransLib
import jdkj.Base16Encoding
import vpos.apipackage.IDCard
import vpos.apipackage.Picc
import vpos.apipackage.Sys
import java.io.UnsupportedEncodingException

/**
 *
 * @author Created by 刘承. on 2019/12/13
 *
 * --深圳市尚美欣辰科技有限公司.
 */
object IDCardUtil {
    var cardSDkopen = false//sdk open状态，未open不进行寻卡 读卡操作

    //开始读卡
    @Synchronized
    fun readCard(): IDCardInfo {
        var idCardInfo = IDCardInfo()
        if (!cardSDkopen) return idCardInfo
        val bf = ByteArray(5 * 1024)
        val ret = IDCard.Lib_IDCardReadRaw(bf, 5)
        if (ret == 0) {
            try {
                val uids = ByteArray(8)
                IDCard.Lib_IDCardReadUid(uids)
                idCardInfo.uuid = CardHexStringUtils.byteArrayToHexstring(uids, 0, uids.size)
                val pucCHMsg = ByteArray(256)
                val pucPHMsg = ByteArray(1024)
                val pucFGMsg = ByteArray(1024)
                System.arraycopy(bf, 11, pucCHMsg, 0, 256)
                System.arraycopy(bf, 11 + 256, pucPHMsg, 0, 1024)
                System.arraycopy(bf, 11 + 256 + 1024, pucFGMsg, 0, 1024)
                var tmpStr = String(pucCHMsg, charset("UTF16-LE"))
                tmpStr = String(tmpStr.toByteArray(charset("UTF-8")))
                idCardInfo.finger = pucFGMsg
                parseData(idCardInfo, tmpStr)
                idCardInfo.picBitmap = getPicBitmap(pucPHMsg)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return idCardInfo
    }


    //拆分数据
    private fun parseData(idCardInfo: IDCardInfo, TmpStr: String) {
        val decodeInfo = arrayOfNulls<String>(15)
        if (TmpStr.length < 128) {
            return
        }
        try {
            val cardType = TmpStr.substring(124, 125)
            Log.d("***", "TmpStr: $TmpStr")
            Log.d("***", "cardtype: $cardType")
            if (cardType.contains("I")) {//外国人居住证
                decodeInfo[0] = TmpStr.substring(0, 60)//英文名
                decodeInfo[1] = TmpStr.substring(60, 61)//性别
                decodeInfo[2] = TmpStr.substring(61, 76)//证件号码
                decodeInfo[3] = TmpStr.substring(76, 79)//国籍或者地区码
                decodeInfo[4] = TmpStr.substring(79, 94)//中文名
                decodeInfo[5] = TmpStr.substring(94, 102)//签发日期
                decodeInfo[6] = TmpStr.substring(102, 110)
                decodeInfo[7] = TmpStr.substring(110, 118)//生日
                decodeInfo[8] = TmpStr.substring(118, 120)//证件版本号
                decodeInfo[9] = TmpStr.substring(120, 124)//申请受理机关
                decodeInfo[10] = TmpStr.substring(124, 125)//证件类型 "I"
//                decodeInfo[11] = TmpStr.substring(125, 128)//预留
                idCardInfo.apply {
                    finger = null
                    retCode = 1
                    eng_name = decodeInfo[0]
                    sex = if (decodeInfo[1] == "1") {
                        "男"
                    } else {
                        "女"
                    }
                    cardNo = decodeInfo[2]
                    district_code = decodeInfo[3]
                    issuer_code = decodeInfo[3]
                    name = decodeInfo[4]//中文名
                    issueOffice = decodeInfo[9]
                    validDateStart = decodeInfo[5]
                    validDateEnd = decodeInfo[6]
                    birthday = decodeInfo[7]
                    ver = decodeInfo[8]
                    address = CardParse.parseCountry(decodeInfo[3])
                    this.cardType = cardType
                }
            } else if (cardType.contains("J")) {//港澳台居住证
                decodeInfo[0] = TmpStr.substring(0, 15)//姓名
                decodeInfo[1] = TmpStr.substring(15, 16)//性别
                decodeInfo[2] = TmpStr.substring(16, 18)//预留区（身份证时为名族）
                decodeInfo[3] = TmpStr.substring(18, 26)//出生日期
                decodeInfo[4] = TmpStr.substring(26, 61)//住址
                decodeInfo[5] = TmpStr.substring(61, 79)//身份证号码
                decodeInfo[6] = TmpStr.substring(79, 94)//签发机关
                decodeInfo[7] = TmpStr.substring(94, 102)//有效期起止
                decodeInfo[8] = TmpStr.substring(102, 110)
                decodeInfo[9] = TmpStr.substring(110, 119)//通行证号码
                decodeInfo[10] = TmpStr.substring(119, 121)//签发次数
//                decodeInfo[11] = TmpStr.substring(121, 124)//预留区
                decodeInfo[12] = TmpStr.substring(124, 125)//证件类型 "J"
//                decodeInfo[13] = TmpStr.substring(125, 128)//预留区

                idCardInfo.apply {
                    retCode = 2
                    name = decodeInfo[0]
                    sex = if (decodeInfo[1] == "1") {
                        "男"
                    } else {
                        "女"
                    }
                    birthday = decodeInfo[3]//出生日期格式以转换  2017-09-09
                    address = decodeInfo[4]
                    cardNo = decodeInfo[5]
                    issueOffice = decodeInfo[6]
                    validDateStart = decodeInfo[7]
                    validDateEnd = decodeInfo[8]
                    passNo = decodeInfo[9]
                    this.cardType = cardType
                }
            } else {//身份证
                decodeInfo[0] = TmpStr.substring(0, 15)//姓名
                decodeInfo[1] = TmpStr.substring(15, 16)//性别  1:男  2：女
                decodeInfo[2] = TmpStr.substring(16, 18)//名族
                decodeInfo[3] = TmpStr.substring(18, 26)//出生日期
                decodeInfo[4] = TmpStr.substring(26, 61)//住址
                decodeInfo[5] = TmpStr.substring(61, 79)//身份证号码
                decodeInfo[6] = TmpStr.substring(79, 94)//签发机关
                decodeInfo[7] = TmpStr.substring(94, 102)//有效期止
                decodeInfo[8] = TmpStr.substring(102, 110)//有效期止
//                decodeInfo[9] = TmpStr.substring(110, 128)//无数据
//                decodeInfo[11] = TmpStr.substring(124, 125)//证件类型 ""  身份证
//                decodeInfo[12] = TmpStr.substring(125, 128)//预留区
                try {
                    val code = Integer.parseInt(decodeInfo[2])
                    decodeInfo[2] = CardParse.parseNation(code)
                } catch (var16: Exception) {
                    decodeInfo[2] = ""
                }
                idCardInfo.apply {
                    retCode = 0
                    name = decodeInfo[0]
                    sex = if (decodeInfo[1] == "1") {
                        "男"
                    } else {
                        "女"
                    }
                    nation = decodeInfo[2]
                    birthday = decodeInfo[3]//出生日期格式以转换  2017-09-09
                    address = decodeInfo[4]
                    cardNo = decodeInfo[5]
                    issueOffice = decodeInfo[6]
                    validDateStart = decodeInfo[7]
                    validDateEnd = decodeInfo[8]
                    this.cardType = cardType
                }
            }
        } catch (e: UnsupportedEncodingException) {
            Log.d("解析证件数据异常", "$e")
        }
    }

    /**
     * @param pic_src 读卡器返回的1024 + 字节的照片数据
     */
    private fun getPicBitmap(pic_src: ByteArray): Bitmap? {
        val BMP_LEN = 38556
        val tmp_data = ByteArray(40 * 1024)
        val result = Wlt2bmpShare.picUnpack(pic_src, tmp_data)
        if (result != 1)
            return null
        val bmp_data = ByteArray(BMP_LEN)
        System.arraycopy(tmp_data, 0, bmp_data, 0, BMP_LEN)
        return Tool.createRgbBitmap(bmp_data, 102, 126)
    }


    //模块上电
    fun openSdk(): Boolean {
        var ret = Picc.Lib_PiccOpen()//不需要单独寻卡或者读IC卡可不初始化
        ret = IDCard.Lib_IDCardOpen()
        cardSDkopen = ret == 0
        return cardSDkopen
    }

    //模块下电
    fun closeSdk() {
        if (cardSDkopen) {
            Picc.Lib_PiccClose()
            IDCard.Lib_IDCardClose()
        }
        cardSDkopen = false
    }

    //版本号
    fun getVersion(): ByteArray {
        val data = ByteArray(8)
        if (!cardSDkopen) return data
        Sys.Lib_GetVersion(data)
        return data;
    }

    //初始化读卡模块参数配置
    fun initSdk(context: Context, serialport: String) {
        Sys.initEnv(context)
        Sys.Lib_SetComPath(serialport)
        TransLib.TransLibSetOnCardholderAction(TransLib.cardholderAction)
    }

    //寻身份证
    fun searchIDCard(): Boolean {
        if (!cardSDkopen) return false
        val temp = ByteArray(128)
        val cardtype = ByteArray(2)
        val ret = Picc.Lib_PiccCheck(66.toByte(), cardtype, temp)
        if (ret == 0) {
//            Log.d("---", "测试  UID=" + Arrays.toString(temp))
            return true
        }
        return false
    }


    //寻银行卡
    fun searchBankCard(): Boolean {
        if (!cardSDkopen) return false
        val temp = ByteArray(128)
        val cardtype = ByteArray(2)
        val ret = Picc.Lib_PiccCheck(65.toByte(), cardtype, temp)
        return ret == 0
    }


    //读ic卡 uid
    fun readICCard(): String {
        if (!cardSDkopen) return ""
        val temp = ByteArray(128)
        val cardtype = ByteArray(2)
        val ret = Picc.Lib_PiccCheck(65.toByte(), cardtype, temp)
        return if (ret == 0) {
            Base16Encoding.byteArrayToHexstring(temp, 1, temp[0] + 1)
        } else ""
    }

    fun readBankCardNotSearch(): BankCardInfo {
        val cardInfo = BankCardInfo()
        if (!cardSDkopen) return cardInfo
        val temp = ByteArray(128)
        val cardtype = ByteArray(2)
        var ret = Picc.Lib_PiccCheck(65.toByte(), cardtype, temp)
        if (ret != 0) {
            return cardInfo
        }
        val uid = Base16Encoding.byteArrayToHexstring(temp, 1, temp[0] + 1)
        cardInfo.setUid(uid)
        val pan = TransLib.TransLibGetPan()
        cardInfo.setCardNo(pan)
        if (!TextUtils.isEmpty(pan)) {
            val bin = pan.substring(0, 6)
            cardInfo.setBin(bin)
        }
        var balance: String? = null
        val data = ByteArray(6)
        ret = TransLib.TransLibGetData(40825, data)
        if (ret == 0) {
            balance = com.pboc.Base16Encoding.byteArrayToHexstring(data)
            val fbalance = balance.toFloat() / 100.0f
            balance = fbalance.toString() + ""
            cardInfo.setBalance(balance)
        }
        cardInfo.setRetCode(1)
        //        Picc.Lib_PiccClose();
        return cardInfo
    }
}