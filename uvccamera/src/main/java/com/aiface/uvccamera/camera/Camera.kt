package com.aiface.uvccamera.camera

import android.graphics.SurfaceTexture
import android.util.Log
import android.view.Surface
import com.serenegiant.usb.*


/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */
class Camera(var controlBlock: USBMonitor.UsbControlBlock) : base() {
    internal var uvcCamera: UVCCamera? = null
    private var previewSurfaceTexture: SurfaceTexture? = null
    internal var call: IFrameCall? = null
    internal var isPreview = false
    private var Width: Int = 0//相机实际启动宽  可能出现预览宽高与回调数据宽高不一致
    private var Height: Int = 0//相机实际启动高  可能出现预览宽高与回调数据宽高不一致

    private val iFrameCallback = IFrameCallback {
        call?.run {
            call(it, Width, Height)
        }
    }


    fun startCapture(surface: Surface) {
        uvcCamera?.startCapture(surface)
    }

    fun stopCapture() {
        uvcCamera?.stopCapture()
    }

    /**
     * 启动相机
     */
    @Synchronized
    fun openCamera(): Boolean {
        if (isOpen()) return true
        try {
            uvcCamera = UVCCamera()
            uvcCamera?.open(controlBlock)
//            setPreviewSize(CamerasMng.defaultPreviewWidth, CamerasMng.defaultPreviewHeight)//设置预览参数
            if (CamerasMng.usingDfSize && CamerasMng.defaultPreviewWidth != 0 && CamerasMng.defaultPreviewHeight != 0) {
                uvcCamera?.setPreviewSize(
                    CamerasMng.defaultPreviewWidth,
                    CamerasMng.defaultPreviewHeight
                )
                uvcCamera?.setAutoFocus(true)
            }
            return true
        } catch (e: Exception) {
            Log.d("------", "$pid openCamera()  faild $e ")
            uvcCamera?.destroy()//open失败的时候调用，回调USBMonitor注册的 disconnect（）
            uvcCamera = null
        }
        return false
    }

    /**
     * 获取相机分辨率列表
     */
    fun getSupportedSizeList() = uvcCamera?.supportedSizeList


    /**
     * 相机数据回调
     */
    fun setFrameCall(calls: IFrameCall?) {
        call = calls
    }

    /**
     * 设置预览宽高
     * 目前问题:1.红外相机在mjpeg模式下，有些分辨率设置失效，但是回调的数据分辨率是正确的(联咏，松汉镜头组)
     *
     *
     *      int type = this.mCurrentFrameFormat > 0 ? 6 : 4;
     * ---> 根据代码推断type=6为mjpeg分辨率，type=4为yuv分辨率
     *
     * {"formats":[{"index":1,"type":6,"default":1,"size":["640x480","800x600","848x480","1024x768","1280x800","1280x720","1920x1080"]},
     * {"index":2,"type":4,"default":1,"size":["640x480","800x600","848x480","1024x768","1280x800","1280x720","1920x1080"]}]}
     */
    @Synchronized
    fun setPreviewSize(
        w: Int,
        h: Int,
        max_fps: Int = DEFAULT_PREVIEW_MAX_FPS,
        frameType: Int = UVCCamera.FRAME_FORMAT_MJPEG,
        bandwidthFactor: Float = 0.5F
    ): Boolean {
        if (!isOpen()) return false
        val list = uvcCamera?.supportedSizeList
        Log.d("------", "相机pid$pid supportedSize :  ${uvcCamera?.supportedSize} ")
        list?.forEach { Log.d("------", "相机pid$pid setPreviewSize :  ${it} ") }
        var size: Size? = null//查询是否支持预设值=宽高
        list?.forEach { if (it.width == w && it.height == h) size = it }
        Log.d("------", "相机pid$pid  setPreviewSize  最终设置size:  ${size} ")
        Log.d("------", "max_fps $max_fps  frameType ${frameType} ")
        return if (size != null) {
            try {
                uvcCamera?.let {
                    it.setPreviewSize(//设置预览尺寸 根据设备自行设置
                        size!!.width,
                        size!!.height,
                        DEFAULT_PREVIEW_MIN_FPS,
                        max_fps,
                        frameType, //此格式设置15帧生效  -----  UVCCamera.FRAME_FORMAT_YUYV,
                        bandwidthFactor
                    )
                    it.updateCameraParams()
                }
            } catch (e: IllegalArgumentException) {
                Log.d("------", "相机参数设置失败 $e")
                return false
            }
            true
        } else false
    }


    /**
     * 设置预览
     *
     * 可在此设置宽高、回调数据、surface
     */
    @Synchronized
    fun startPreview(
        previewTexture: SurfaceTexture? = null,
        surface: Surface? = null,
        w: Int = 0,
        h: Int = 0,
        max_fps: Int = 15,
        frameType: Int = UVCCamera.FRAME_FORMAT_MJPEG,
        previewcall: IFrameCall? = null
    ): Boolean {
        if (openCamera()) {
            previewcall?.let {
                setFrameCall(previewcall)//此处为空不再重置为空
            }
            try {
                previewSurfaceTexture = previewTexture
                uvcCamera?.run {
                    if (previewTexture != null) {
                        setPreviewTexture(previewTexture)
                    } else if (surface != null) {
                        setPreviewDisplay(surface)
                    } else {
                        setPreviewTexture(SURFACE_TEXTURE)
                    }
                    if (w != 0 && h != 0) {
                        this@Camera.setPreviewSize(w, h, max_fps = max_fps, frameType = frameType)
                    }
                    setFrameCallback(
                        iFrameCallback,
                        UVCCamera.PIXEL_FORMAT_YUV420SP
                    )// uvcCamera.PIXEL_FORMAT_NV21
                    previewSize?.let {
                        //获取不到分辨率数据的自行get，人脸算法需要
                        Height = previewSize.height
                        Width = previewSize.width
                    }

                    startPreview()
                    powerlineFrequency = 1
                    updateCameraParams()
                    isPreview = true
                }
            } catch (e: java.lang.Exception) {
                Log.d("------", "stratPreview() faild $pid $e")
                isPreview = false
            }
            return isPreview
        } else {
            Log.d("------", "stratPreview() openCamera()  faild $pid")
            return false
        }
    }

    //假关闭，从相机中脱离，相机逻辑不变，应用不再使用相机数据
    fun stopSecede() {
        if (!isPreview) return
        uvcCamera?.setPreviewTexture(SURFACE_TEXTURE)
        uvcCamera?.setFrameCallback(null, UVCCamera.PIXEL_FORMAT_YUV420SP)
        previewSurfaceTexture = null
        setFrameCall(null)//重置为空
    }


    //取消预览
    @Synchronized
    fun stopPreview() {
        uvcCamera?.stopPreview()
        isPreview = false
        call = null
        previewSurfaceTexture = null
    }


    /**
     * 使相机进入关闭状态
     */
    @Synchronized
    fun destroyCamera() {
//        if (isPreviewing()) stopPreview()
        uvcCamera?.destroy()
        uvcCamera = null
        isPreview = false
        call = null
        previewSurfaceTexture = null
    }

    //初始化相机硬件相关内容
    internal fun initmarking() {
        controlBlock.run {
            devName = deviceName
            vid = venderId
            pid = productId
            val v = if (devName.isNullOrEmpty()) devName.split("/") else null
            v?.let {
                mBusNum = Integer.parseInt(v[v.size - 2])
                mDevNum = Integer.parseInt(v[v.size - 1])
            }
        }
    }

    /**
     * 设置电源频率
     */
    fun setPowerlineFrequency(powerline: Int) {
        uvcCamera?.powerlineFrequency = powerline
    }

    fun getPowerlineFrequency(): Int = uvcCamera?.powerlineFrequency!!


    /**
     * 相机参数相关常量
     */
    companion object {
        val DEFAULT_PREVIEW_MIN_FPS = 1
        val DEFAULT_PREVIEW_MAX_FPS = 15//30
        val FRAME_FORMAT_YUYV = 0
        val FRAME_FORMAT_MJPEG = 1

        val PIXEL_FORMAT_RAW = 0
        val PIXEL_FORMAT_YUV = 1
        val PIXEL_FORMAT_RGB565 = 2
        val PIXEL_FORMAT_RGBX = 3
        val PIXEL_FORMAT_YUV420SP = 4
        val PIXEL_FORMAT_NV21 = 5
    }
}

fun Camera.isOpen(): Boolean {
    return uvcCamera != null
}

fun Camera.isPreviewing(): Boolean {
    return isPreview
}