package com.leessy.coolkotlin

import android.app.Presentation
import android.content.Context
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.hardware.display.DisplayManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.TextureView
import android.webkit.URLUtil
import com.aiface.uvccamera.camera.Camera
import com.aiface.uvccamera.camera.CamerasMng
import com.aiface.uvccamera.camera.IFrameCall
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.opt.ImageColor
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import kotlinx.android.synthetic.main.presentation_view.*
import java.nio.ByteBuffer
import java.util.*

class PresentationCameraActivity : AppCompatActivity() {
    private val TAG = javaClass.name
    var c: Camera? = null
    var c2: Camera? = null
    val cameraColorW = 640
    val cameraColorH = 480
    private var mDisplayManager: DisplayManager? = null
    private var display: Display? = null
    private var presentation: PresentationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation_camera)
        mDisplayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        if (mDisplayManager != null) {
            if (mDisplayManager!!.displays.size > 1) {
                display = mDisplayManager!!.displays[1]
            }
        }

        //获取设备列表
        CamerasMng.cameraList.forEach {
            Log.d("CamerasMng", "CamerasMng ${it.pid}")
            if (it.pid == 33073) {//33073
                c = it
                c?.openCamera()
                c?.setPreviewSize(cameraColorW, cameraColorH)
                c?.setFrameCall(call)
            }
        }
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
                return true
            }
        }
        showPresentationView()
    }

    var call = object : IFrameCall {
        override fun call(bf: ByteBuffer, w: Int, h: Int) {
            val bytes = ByteArray(bf.capacity())
            bf.get(bytes, 0, bytes.size)
//                发送到算法库识别
//            AiFaceCore.dataEmitter(
//                bytes,
//                ImageColor.COLOR,
//                cameraColorW,
//                cameraColorH,
//                bMirror = 1,
//                nRotate = 2
//            )
            presentation?.setYuvData(bytes, 2)
        }
    }
    //翻转矩阵？ 将Y坐标翻转
    private val CMartix = floatArrayOf(
        -1.0f, 0.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
    ) // 反转Y轴坐标 ，使输出的图像为正向

    //视图转换  镜像
    private fun MatrixView(textureView: TextureView, w: Int, h: Int) {
        val matrix = Matrix()

        textureView.getTransform(matrix)
        //                matrix.postRotate(90, i / 2, i1 / 2);//绕某个点旋转90度，这里选择的原点是图片的中心点
        //                matrix.setSinCos(1, 0, i / 2, i1 / 2);//把图像旋转90度，那么90度对应的sin和cos分别是1和0。
        matrix.setScale(-1f, 1f)
        matrix.postTranslate(w.toFloat(), 0f)
        textureView.setTransform(matrix)
        matrix.getValues(CMartix)
        Log.d(TAG, "---***   ${Arrays.toString(CMartix)}")
    }

    private fun showPresentationView() {
        presentation = PresentationView(this, display)
        presentation?.show()
    }


    inner class PresentationView : Presentation {
        constructor(outerContext: Context?, display: Display?) : super(outerContext, display)

        constructor(outerContext: Context?, display: Display?, theme: Int) : super(
            outerContext,
            display,
            theme
        )

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.presentation_view)

            myglsurfaceview.setYuvDataSize(
                cameraColorW,
                cameraColorH,
                mirrorX = true
            )
        }

        fun setYuvData(yuvData: ByteArray?, type: Int) {
            myglsurfaceview.feedData(yuvData, type)
        }

    }
}

