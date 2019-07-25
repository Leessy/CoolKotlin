package com.leessy.aifacecore.AiFaceCore

import android.content.Context
import com.AiChlFace.AiChlFace
import com.AiChlIrFace.AiChlIrFace
import com.leessy.aifacecore.datas.CameraData
import com.leessy.aifacecore.datas.RectData
import com.leessy.aifacecore.opt.DataEmitterCenter
import com.leessy.aifacecore.opt.FaceRectEmitterCenter
import com.leessy.aifacecore.opt.ImageColor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


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
    internal lateinit var call: IAiFaceInitCall
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
        Schedulers.io().scheduleDirect {
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
     * 数据转发中心  发射器
     *
     *    //        nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //        nFmt ---- 输入源图象数据格式（0：YUV420P, 1: NV12，2: NV21）
    //        bSrcImg ---- 输入源图象数据
    //        nWidth ---- 输入源图象的宽度（象素单位）
    //        nHeight ---- 输入源图象的高度（象素单位）
    //        nLeft ---- 检测区域左上角X坐标(相对于输入源图象，全图检测时填0)
    //        nTop ---- 检测区域左上角Y坐标(相对于输入源图象，全图检测时填0)
    //        nRight ---- 检测区域右下角X坐标(相对于输入源图象，全图检测时也可填0)
    //        nBottom ---- 检测区域右下角Y坐标(相对于输入源图象，全图检测时也可填0)
    //        nRotate ---- 旋转方式（对输入源图象旋转，0：不旋转，1：左旋90度，2：右旋90度）
    //        bMirror ---- 左右镜象（相对于旋转后的图象，0：左右不镜象，1：左右镜象）
    // 输出参数:
    //        bRgb24 ---- 输出的RGB24格式图象数据(裁减、旋转和镜象后的图象数据)
    //        nNewWidth ---- 输出图象的宽度(裁减、旋转和镜象后的图象宽度)
    //        nNewHeight ---- 输出图象的高度(裁减、旋转和镜象后的图象宽度)
    //        sFaceResult ---- 检测到的人脸参数（人脸及眼睛等坐标位置及角度等，相对于裁减、旋转和镜象后的图象，调用前必须分配有效的空间）
    // 返回：返回1表示检测到人脸，0表示无人脸，< 0 表示检测失败
     *
     * @param CameraID  同一相机颜色最多可以使用2个id，默认id为0
     * @param imageColor 说明相机颜色类型 根据颜色类型使用红外或者彩色算法
     *
     */
    fun dataEmitter(
        byteArray: ByteArray,
        imageColor: ImageColor,
        width: Int,
        height: Int,
        CameraID: Int = 0,
        stream: Int = 2,
        bMirror: Int = 0,
        nRotate: Int = 0
    ) = DataEmitterCenter.buidler(
        byteArray,
        width,
        height,
        CameraID,
        imageColor,
        stream,
        bMirror,
        nRotate
    )


    /**
     * 获取数据源
     * @param CameraID      根据 (颜色 +id)   获取数据   不通颜色可以有相同id 默认id为0
     * @param imageColor    根据 (颜色 +id)   获取数据  不通颜色可以有相同id 默认id为0
     */
    fun Follows(imageColor: ImageColor = ImageColor.COLOR, CameraID: Int = 0): Observable<CameraData> {
        return DataEmitterCenter.getEmitter(imageColor, CameraID)
            .observeOn(Schedulers.computation())
            .sample(160, TimeUnit.MILLISECONDS)

    }

    /**
     *根据颜色类型+相机id 获取人脸数据
     */
    fun FollowFaceRect(imageColor: ImageColor = ImageColor.COLOR, CameraID: Int = 0): Observable<RectData> {
        return FaceRectEmitterCenter.FaceRectObservableForID(imageColor, CameraID)
    }

//    /**
//     * 获取全部数据人脸框数据
//     */
//    fun FollowFaceRectAll(): Observable<RectData> {
//        return FaceRectEmitterCenter.getEmitter()
//    }
//
//    /**
//     * 使用读卡模块授权时 设置读取
//     */
//    fun setCardReader() {
//        //todo 待实现读卡授权的逻辑
//    }
}

