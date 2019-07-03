package com.leessy.camera

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
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
class Camera(var controlBlock: USBMonitor.UsbControlBlock) {
    internal var uvcCamera: UVCCamera? = null
    private val SURFACE_TEXTURE = SurfaceTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)//不显示时做接收数据源使用
    private var previewSurfaceTexture: SurfaceTexture? = null
    var call: IFrameCall? = null
    var devName = ""

    var isPreview = false

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
        try {
            uvcCamera = UVCCamera()
            uvcCamera?.open(controlBlock)
            setCameraDefaultParameter(uvcCamera)//设置预览参数
            uvcCamera?.setAutoFocus(true)
            return true
        } catch (e: Exception) {
            uvcCamera = null
        }
        return false
    }


    /**
     * 重新设置预览宽高
     */
    fun setPreviewSize(w: Int, h: Int): Boolean {
        if (!isOpen()) return false
        if (w == Width && h == Height) return true
        val list = uvcCamera?.getSupportedSizeList()
        //查询是否支持预设值=宽高
        var size: Size? = null
        list?.forEach {
            if (it.width == CamerasMng.defaultPreviewWidth
                || it.height == CamerasMng.defaultPreviewHeight
            ) {
                size = it
            }
        }
        return if (size == null) {
            false
        } else {
            uvcCamera?.setPreviewSize(w, h)
            uvcCamera?.updateCameraParams()
            true
        }
    }

    /**
     * 相机默认参数
     */
    @Synchronized
    private fun setCameraDefaultParameter(camera: UVCCamera?) {
        camera?.let {
            try {
                val list = camera.getSupportedSizeList()
                list.forEach {
                    Log.d("------", "*** $devName $it")
                }
                Width = CamerasMng.defaultPreviewWidth
                Height = CamerasMng.defaultPreviewHeight

                //查询是否支持预设值=宽高
                var size: Size? = null
                list.forEach {
                    if (it.width == CamerasMng.defaultPreviewWidth
                        || it.height == CamerasMng.defaultPreviewHeight
                    ) {
                        size = it
                    }
                }

                //没有预设的宽高 默认使用第一个分辨率
                if (size == null && list.size > 0) {
                    Width = list[0].width
                    Height = list[0].height
                }
                //设置预览尺寸 根据设备自行设置
                camera.setPreviewSize(
                    Width,
                    Height,
                    1,
                    15,
                    //                    UVCCamera.FRAME_FORMAT_YUYV,
                    UVCCamera.FRAME_FORMAT_MJPEG, //此格式设置15帧生效
                    0.4f
                )
                Width = camera.previewSize.width
                Height = camera.previewSize.height

            } catch (e: IllegalArgumentException) {
            }
        }
    }


    //设置预览
    @Synchronized
    fun startPreview(previewTexture: SurfaceTexture? = null, previewcall: IFrameCall? = null) {
        if (!isOpen()) openCamera()

        if (isOpen()) {
            call = previewcall
            previewSurfaceTexture = previewTexture
            if (previewTexture == null) {
                uvcCamera?.setPreviewTexture(SURFACE_TEXTURE)
            } else {
                uvcCamera?.setPreviewTexture(previewTexture)
            }
            uvcCamera?.setFrameCallback(iFrameCallback, UVCCamera.PIXEL_FORMAT_YUV420SP)// uvcCamera.PIXEL_FORMAT_NV21
            uvcCamera?.startPreview()
            isPreview = true
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
}

fun Camera.isOpen(): Boolean {
    return uvcCamera != null
}

fun Camera.isPreviewing(): Boolean {
    return isPreview
}