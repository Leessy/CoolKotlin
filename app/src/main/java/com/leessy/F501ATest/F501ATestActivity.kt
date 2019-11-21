package com.leessy.F501ATest

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.TextureView
import com.aiface.uvccamera.camera.Camera
import com.aiface.uvccamera.camera.CamerasMng
import com.leessy.coolkotlin.R
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import kotlinx.android.synthetic.main.activity_f501_atest.*
import kotlinx.android.synthetic.main.activity_f501_atest.textureview

/**
 * f501A   散装油单屏机 单晨一镜头  测试
 */
class F501ATestActivity : AppCompatActivity() {
    var c: Camera? = null
    var c2: Camera? = null
    val cameraColorW = 640
    val cameraColorH = 480
    val cameraIrW = 640
    val cameraIrH = 480
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_f501_atest)
        initCamera()
    }

    private fun initCamera() {
        //获取设备列表
        CamerasMng.cameraList.forEach {
            //            /dev/bus/usb/002/005  7727  38789
//            /dev/bus/usb/002/004  6935  2048
            Log.d("CamerasMng", "CamerasMng ${it.pid}")
            if (it.pid == 3) {//33073
                c = it
                c?.openCamera()
//                var l = c?.getSupportedSizeList()
//                l?.forEachIndexed { index, size ->
//                    Log.d("----", ": $size ")
//                }
                c?.setPreviewSize(cameraColorW, cameraColorH, max_fps = 30)
            } else if (it.pid == 1) {
                c2 = it
                c2?.openCamera()
                c2?.setPreviewSize(cameraIrW, cameraIrH, max_fps = 30)
            }
        }

//        textureview.rotation = -90F
        textureview.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureAvailable")
                //                MatrixView(textureview, width, height)
                c?.startPreview(surface)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureSizeChanged")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                //                Log.d("----", "--   onSurfaceTextureUpdated")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("----", "--   onSurfaceTextureDestroyed")
                c?.stopPreview()
                //                c?.destroyCamera()
                return true
            }
        }

        textureviewS.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureAvailable")
                //                MatrixView(textureview, width, height)
                c2?.startPreview(surface)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureSizeChanged")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
                //                Log.d("----", "--   onSurfaceTextureUpdated")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("----", "--   onSurfaceTextureDestroyed")
                c2?.stopPreview()
                //                c?.destroyCamera()
                return true
            }
        }
    }
}
