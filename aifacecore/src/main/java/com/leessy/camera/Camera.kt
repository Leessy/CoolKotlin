package com.leessy.camera

import android.graphics.SurfaceTexture
import android.util.Log
import com.serenegiant.usb.IFrameCallback
import com.serenegiant.usb.Size
import com.serenegiant.usb.USBMonitor
import com.serenegiant.usb.UVCCamera


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

    private var Width: Int = 0//相机实际启动宽
    private var Height: Int = 0//相机实际启动高


    private val iFrameCallback = IFrameCallback {
        Log.d("------", "iFrameCallback  $devName   $Width   $Height")
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
            setPreviewSize(CamerasMng.defaultPreviewWidth, CamerasMng.defaultPreviewHeight)//设置预览参数
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
     */
    @Synchronized
    fun setPreviewSize(w: Int, h: Int): Boolean {
        if (!isOpen()) {
            return false
        }
        try {
            val list = uvcCamera?.getSupportedSizeList()
            //查询是否支持预设值=宽高
            var size: Size? = null
            list?.forEach {
                if (it.width == w || it.height == h) size = it
            }
            return if (size == null) {
                false
            } else {
                uvcCamera?.let {
                    it.setPreviewSize(//设置预览尺寸 根据设备自行设置
                        Width,
                        Height,
                        1,
                        15,
                        UVCCamera.FRAME_FORMAT_MJPEG, //此格式设置15帧生效  -----  UVCCamera.FRAME_FORMAT_YUYV,
                        0.4f
                    )
                    Width = it.previewSize.width
                    Height = it.previewSize.height
                    it.updateCameraParams()
                }
                true
            }
        } catch (e: Exception) {
        }
        return false
    }


    /**
     * 设置预览
     *
     * 可在此设置宽高、回调数据、surface
     */
    @Synchronized
    fun startPreview(previewTexture: SurfaceTexture? = null, w: Int = 0, h: Int = 0, previewcall: IFrameCall? = null) {
        if (openCamera()) {
            setFrameCall(previewcall)
            previewSurfaceTexture = previewTexture
            uvcCamera?.run {
                if (previewTexture == null) {
                    setPreviewTexture(SURFACE_TEXTURE)
                } else {
                    setPreviewTexture(previewTexture)
                }
                if (w != 0 && h != 0) {
                    this@Camera.setPreviewSize(w, h)
                }
                setFrameCallback(iFrameCallback, UVCCamera.PIXEL_FORMAT_YUV420SP)// uvcCamera.PIXEL_FORMAT_NV21
                startPreview()
                isPreview = true
            }
        }
    }


    //取消预览
    @Synchronized
    fun stopPreview() {
        isPreview = false
        call = null
        uvcCamera?.stopPreview()
        previewSurfaceTexture = null
    }


    /**
     * 使相机进入关闭状态
     */
    @Synchronized
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
}

fun Camera.isOpen(): Boolean {
    return uvcCamera != null
}

fun Camera.isPreviewing(): Boolean {
    return isPreview
}