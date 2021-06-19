package com.leessy.aifacecore.AiFaceCore

//import com.AiChlFace.AiChlFace
//import com.AiChlFace.FACE_DETECT_RESULT
//import com.AiChlIrFace.AiChlIrFace
import android.content.Context
import android.graphics.Bitmap
import android.renderscript.*
import android.util.Log
import android.widget.Toast
import com.huiyuenet.faceCheck.FaceCheck
import com.huiyuenet.faceCheck.THFI_Param
import com.leessy.aifacecore.datas.CameraData
import com.leessy.aifacecore.datas.RectData
import com.leessy.aifacecore.opt.DataEmitterCenter
import com.leessy.aifacecore.opt.FaceRectEmitterCenter
import com.leessy.aifacecore.opt.ImageColor
import com.usface.activation.net.HttpUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */

object AiFaceCore {
    private val TAG = "AiFaceCore"
    private val account = ""
    private val password = ""
    private var AiFaceSDK: Boolean = false
    internal lateinit var call: IAiFaceInitCall

    /*图库转换相关*/
    var renderScript: RenderScript? = null
    var yuvToRgbIntrinsic: ScriptIntrinsicYuvToRGB? = null
    var allocationIn: Allocation? = null
    var allocationOut: Allocation? = null

    /*初始化图片转换器*/
    private fun initTransform() {
        if (renderScript != null) return
        renderScript = RenderScript.create(mContext)
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(renderScript, Element.U8_4(renderScript))
        if (allocationIn == null) {
            val x = (THFI_Param.IMG_WIDTH * THFI_Param.IMG_HEIGHT * 1.5).toInt()
            val yuvType = Type.Builder(renderScript, Element.U8(renderScript)).setX(x)
            allocationIn =
                Allocation.createTyped(renderScript, yuvType.create(), Allocation.USAGE_SCRIPT)
        }
        if (allocationOut == null) {
            val rgbaType = Type.Builder(renderScript, Element.RGBA_8888(renderScript))
                .setX(THFI_Param.IMG_WIDTH).setY(THFI_Param.IMG_HEIGHT)
            allocationOut =
                Allocation.createTyped(renderScript, rgbaType.create(), Allocation.USAGE_SCRIPT)
        }
    }

    /*是否需要两路分开转换*/
    fun yuvToBitmapIr(yuv: ByteArray, width: Int, height: Int): Bitmap {
        allocationIn?.copyFrom(yuv)
        yuvToRgbIntrinsic?.setInput(allocationIn)
        yuvToRgbIntrinsic?.forEach(allocationOut)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        allocationOut?.copyTo(bitmap)
        return bitmap
    }

    @Synchronized
    fun yuvToBitmap(yuv: ByteArray, width: Int, height: Int): Bitmap {
        allocationIn?.copyFrom(yuv)
        yuvToRgbIntrinsic?.setInput(allocationIn)
        yuvToRgbIntrinsic?.forEach(allocationOut)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        allocationOut?.copyTo(bitmap)
        return bitmap
    }

    fun isInit() = AiFaceSDK

//    fun getAiChlFaceSize(): Int {
//        return AiChlFaceSize
//    }
//
//    fun getAiChlIrFaceSize(): Int {
//        return AiChlFaceSize
//    }

    lateinit var mContext: Context

    /**
     * 初始化人脸算法 ，默认使用时间授权
     */
    fun initAiFace(con: Context, calls: IAiFaceInitCall) {
        this.mContext = con
        call = calls
        init(con)
        initTransform()
    }


    /**
     * 初始化 算法相关
     */
    private fun init(mContext: Context) {
        val errorCode = FaceCheck.init(mContext)
        if (errorCode > -1999 && errorCode < -999) {     // 授权检测错误，需要联网授权
            var needCertificate = true
            if (errorCode == THFI_Param.ERROR_INVALID_DEVICE) {
                showTips(mContext, "授权文件校验失败，设备未授权")
            } else if (errorCode == THFI_Param.ERROR_INVALID_DATE) {
                showTips(mContext, "授权文件校验失败，设备超过授权使用期限")
            } else if (errorCode == THFI_Param.ERROR_INVALID_COUNT) {
                showTips(mContext, "授权文件校验失败，设备超过授权数量限制")
            } else {
                needCertificate = false
            }
            //想需要授权
            if (needCertificate) {
                val certificate = certificate(mContext)
                if (certificate) {//授权成功重新初始化
                    init(mContext)
                    return
                }
            }
        } else if (errorCode < 0) {    // 算法相关错误，需要根据具体错误码进行不同的处理
            showTips(mContext, "算法初始化错误，请重试，code:$errorCode")
        } else if (errorCode >= 0) {
            AiFaceSDK = true
        }
        call.call(AiFaceSDK)
    }

    /*授权申请*/
    private fun certificate(mContext: Context): Boolean {
        HttpUtil.setHttpURL(THFI_Param.SERVER_IP)
        val result = HttpUtil.verifyAlgo(mContext, account, password)
        if (result != null) {
            if (result.code == "1000") {
                showTips(mContext, "certificate: 授权激活成功")
                return true
            } else {     // 激活失败
                showTips(mContext, "certificate: 授权激活失败，错误码：${result.code},错误信息：${result.msg}")
            }
        }
        showTips(mContext, "certificate: 授权激活失败，请检查网络连接！")
        return false
    }

    fun showTips(mContext: Context, s: String) {
        Log.d(TAG, "showTips: $s")
        AndroidSchedulers.mainThread().scheduleDirect {
            Toast.makeText(mContext, s, Toast.LENGTH_LONG).show()
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
    fun Follows(
        imageColor: ImageColor = ImageColor.COLOR,
        CameraID: Int = 0
    ): Observable<CameraData> {
        return DataEmitterCenter.getEmitter(imageColor, CameraID)
            .observeOn(Schedulers.computation())
            .sample(160, TimeUnit.MILLISECONDS)

    }

    /**
     *根据颜色类型+相机id 获取人脸数据
     */
    fun FollowFaceRect(
        imageColor: ImageColor = ImageColor.COLOR,
        CameraID: Int = 0
    ): Observable<RectData> {
        return FaceRectEmitterCenter.FaceRectObservableForID(imageColor, CameraID)
    }

//    /**
//     * 使用读卡模块授权时 设置读取
//     */
//    fun setCardReader() {
//        //todo 待实现读卡授权的逻辑
//    }


    /**
     * 模板1：1对比返回对比值
     *
     * @param face1 特征码
     * @param face2 特征码
     * @return -1000 模板无效
     * @return 1--100 对比值
     * @return 其他错误码对照算法
     * @see AiChlFace.FeatureCompare
     */
//    fun compare11(channel: Int, face1: ByteArray, face2: ByteArray): Int {
//        if (face1.size == AiChlFaceSize && face2.size == AiChlFaceSize) {
//            return AiChlFace.FeatureCompare(channel, face1, face2)
//        }
//        return -1000
//    }
//
//    fun compare11(face1: ByteArray, face2: ByteArray): Int {
//        if (face1.size == AiChlFaceSize && face2.size == AiChlFaceSize) {
//            return AiChlFace.FeatureCompare(0, face1, face2)
//        }
//        return -1000
//    }
//
//    /**
//     * 检测图片人脸
//     */
//    fun detectFaceFile(f: String): Observable<FaceData> {
//        val bitmap = BitmapFactory.decodeFile(f)
//        if (bitmap != null) {
//            return detectFaceBmp(bitmap)
//        }
//        return Observable.just(FaceData())
//    }

    /**
     * 检测图片人脸
     */
//    fun detectFaceBmp(bmp: Bitmap): Observable<FaceData> {
//        return Observable.just(bmp)
//            .observeOn(Schedulers.io())
//            .map {
//                var faceData = FaceData()
//                val yuv = ImageUtils.BitmapToYUV420SP(it)//转换yuv
//                val rgb24 = ByteArray(it.getWidth() * it.getHeight() * 3)
//                val face_detect_result = FACE_DETECT_RESULT()
//                val w = IntArray(1)
//                val h = IntArray(1)
//
//                faceData.faceNum = AiChlFace.DetectFaceEx(
//                    0, 2, yuv,
//                    it.getWidth(), it.getHeight(),
//                    0, 0, 0, 0, 0, 0,
//                    rgb24, w, h, face_detect_result
//                )
//                if (faceData.faceNum > 0) {
//                    faceData.detectResult = face_detect_result
//                    faceData.feature = ByteArray(AiChlFace.FeatureSize())
//                    faceData.RGB24 = rgb24
//                    faceData.width = w[0]
//                    faceData.height = h[0]
//                    faceData.featureGetStatu =
//                        AiChlFace.FeatureGet(
//                            0,
//                            rgb24,
//                            w[0],
//                            h[0],
//                            face_detect_result,
//                            faceData.feature
//                        )
//                }
//                faceData
//            }
//    }
}

