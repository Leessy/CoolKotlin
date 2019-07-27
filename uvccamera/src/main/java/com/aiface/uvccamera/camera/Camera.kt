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

    /**
     * 启动相机
     */
    fun openCamera(): Boolean {
        if (isOpen()) return true
        try {
            uvcCamera = UVCCamera()
            uvcCamera?.open(controlBlock)
//            setPreviewSize(CamerasMng.defaultPreviewWidth, CamerasMng.defaultPreviewHeight)//设置预览参数
            uvcCamera?.setPreviewSize(CamerasMng.defaultPreviewWidth, CamerasMng.defaultPreviewHeight)
            uvcCamera?.setAutoFocus(true)
            return true
        } catch (e: Exception) {
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
     * 目前问题:1.红外相机在mjpeg模式下，有些分辨率设置失效，但是回调的数据分辨率是正确的
     */
    @Synchronized
    fun setPreviewSize(w: Int, h: Int, max_fps: Int = 15, frameType: Int = UVCCamera.FRAME_FORMAT_MJPEG): Boolean {
        if (!isOpen()) return false
        val list = uvcCamera?.getSupportedSizeList()
        var size: Size? = null//查询是否支持预设值=宽高
        list?.forEach { if (it.width == w && it.height == h) size = it }
        Log.d("------", "$devName setPreviewSize *-****:  ${size} ")
        Log.d("------", "max_fps $max_fps  frameType ${frameType} ")
        return if (size != null) {
            try {
                uvcCamera?.let {
                    it.setPreviewSize(//设置预览尺寸 根据设备自行设置
                        size!!.width,
                        size!!.height,
                        1,
                        max_fps,
                        frameType, //此格式设置15帧生效  -----  UVCCamera.FRAME_FORMAT_YUYV,
                        0.4f
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
        previewcall: IFrameCall? = null
    ) {
        if (openCamera()) {
            previewcall?.let {
                setFrameCall(previewcall)//此处为空不再重置为空
            }
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
                    this@Camera.setPreviewSize(w, h)
                }
                setFrameCallback(iFrameCallback, UVCCamera.PIXEL_FORMAT_YUV420SP)// uvcCamera.PIXEL_FORMAT_NV21
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
        } else Log.d("------", "open  faild ")
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
        if (!isPreview) return
        isPreview = false
        call = null
        uvcCamera?.stopPreview()
        previewSurfaceTexture = null
    }


    /**
     * 使相机进入关闭状态
     */
    fun destroyCamera() {
        if (isPreviewing()) stopPreview()
        uvcCamera?.destroy()
        uvcCamera = null
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
}

fun Camera.isOpen(): Boolean {
    return uvcCamera != null
}

fun Camera.isPreviewing(): Boolean {
    return isPreview
}