package com.leessy.coolkotlin

import android.app.Presentation
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.TextureView
import com.aiface.uvccamera.camera.Camera
import com.aiface.uvccamera.camera.CamerasMng
import com.aiface.uvccamera.camera.IFrameCall
import com.aiface.uvccamera.opengl.MyGLColor
import com.blankj.utilcode.util.ToastUtils
import com.leessy.KotlinExtension.onClick
import com.leessy.aifacecore.AiFaceCore.AiFaceCore
import com.leessy.aifacecore.datas.RectData
import com.leessy.aifacecore.datas.isLivings
import com.leessy.aifacecore.opt.*
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import kotlinx.android.synthetic.main.activity_ai_face_core_test.textureview
import kotlinx.android.synthetic.main.activity_presentation_camera.*
import kotlinx.android.synthetic.main.activity_presentation_camera.faceview
import kotlinx.android.synthetic.main.presentation_view.*
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.TimeUnit

class PresentationCameraActivity : RxAppCompatActivity() {
    private val TAG = javaClass.name
    var c: Camera? = null
    var c2: Camera? = null
    val cameraColorW = 1280
    val cameraColorH = 720
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
//            Log.d("CamerasMng", "CamerasMng ${it.pid}")
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
                c?.stopSecede()
//                c?.stopPreview()
//                c?.destroyCamera()
                return true
            }
        }
        showPresentationView()
        testX.onClick {
            presentation?.showmirror()
        }
        testY.onClick {
            presentation?.showmirrorY()
        }

        //人脸框数据处理
        AiFaceCore.FollowFaceRect(imageColor = ImageColor.COLOR)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .observeOn(Schedulers.newThread())
            .map {
                it.apply {
                    //                    if (!isNoneFace()) {
//                        FaceFilterCalculate(16, 250, 500, 30, 0, 85)
//                    }
                    rect = RectF(
                        it.nFaceLeft.toFloat(),
                        it.nFaceTop.toFloat(),
                        it.nFaceRight.toFloat(),
                        it.nFaceBottom.toFloat()
                    )
                }
            }
//            .FaceFilterCalculate(16, 250, 500, 30, 0, 85)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("---- 人脸框  过滤值=", "${it.rect} ")
                //画彩色人脸框或者红外人脸框
                when (it.imageColor) {
                    ImageColor.COLOR ->
//                        faceview.setFaces(it.rect, 480, 640)
                    {
                        faceview.setFaces(RectF(it.rect), cameraColorW, cameraColorH)
                        presentation?.setFaces(it)
                    }

//                    ImageColor.IR ->
//                        faceview2.setFaces(it.rect, cameraIrH, cameraIrW)
                }

            }, {
                Log.d("---- 人脸框 异常", " $it")
            })

        AiFaceCore.Follows(ImageColor.COLOR)
            .compose(this.bindUntilEvent(ActivityEvent.STOP))
            .sample(300, TimeUnit.MILLISECONDS)
            .DetectFaceAndFilter()
            .subscribe({
                //                Log.d(TAG, "人脸个数:${it.faceNum} ")
                Log.d(TAG, "人脸:${it.faceResult} ")
            }, {})
//            .observeOn(Schedulers.io())
//            .compose(this.bindUntilEvent(ActivityEvent.STOP))
//            .sample(200, TimeUnit.MILLISECONDS)
//            .LivingsSinglePass()
//            .filter { it.isLivings() }
//            .FeatureGet()
    }

    var num1 = 0L
    var call = object : IFrameCall {
        override fun call(bf: ByteBuffer, w: Int, h: Int) {
            val bytes = ByteArray(bf.capacity())
            bf.get(bytes, 0, bytes.size)
            presentation?.setYuvData(bytes, 2)

            //发送到算法库识别
            if (num1++ % 3 != 0L) return
            AiFaceCore.dataEmitter(
                bytes,
                ImageColor.COLOR,
                cameraColorW,
                cameraColorH,
                bMirror = 0,
                nRotate = 0
            )
        }
    }

    //视图转换  镜像
    private fun MatrixView(textureView: TextureView, w: Int, h: Int) {
        val matrix = Matrix()
        textureView.getTransform(matrix)
        //                matrix.postRotate(90, i / 2, i1 / 2);//绕某个点旋转90度，这里选择的原点是图片的中心点
        //                matrix.setSinCos(1, 0, i / 2, i1 / 2);//把图像旋转90度，那么90度对应的sin和cos分别是1和0。
//        matrix.preRotate(180f)

//        * translate（平移），rotate（旋转），scale（缩放）和skew（倾斜）四种*/
        //旋转后的宽高比不正确，如果选哟铺满全屏显示，还需设置缩放比例额
//        matrix.setScale((w / h).toFloat(), (h / w).toFloat(), (w / 2).toFloat(), (h / 2).toFloat())
        Log.d(TAG, "缩放比例=${(w.toFloat() / h.toFloat())}  ${(h.toFloat() / w.toFloat())}")

//        matrix.postRotate(90F, (w / 2).toFloat(), 1f)//绕某个点旋转90度，这里选择的原点是图片的中心点
//        matrix.setScale(-1f, 1f)//设置初始值，会把之前的计算重置


        //先旋转
        matrix.postRotate(90F, (w.toFloat() / 2), h.toFloat() / 2)//绕某个点旋转90度，这里选择的原点是图片的中心点

        //镜像---相当于把X轴1转到-1（沿X0旋转了180°，此时图片在负轴，不可见），然后再往X轴平移宽度过来
        matrix.postScale(-1f, 1f)
        matrix.postTranslate(w.toFloat(), 0f)
        //缩放
        matrix.postScale(
            (w.toFloat() / h),
            (h.toFloat() / w),
            w.toFloat() / 2,
            h.toFloat() / 2
        )
        textureView.setTransform(matrix)
    }

    private fun showPresentationView() {
        if (display == null) return
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
            MyGLColor.setBackgroundColor(1.0f, 0.0f, 0f, 1f)//在glview创建之前配置背景color值
            setContentView(R.layout.presentation_view)
            myglsurfaceview.requestRender()//更新背景颜色
            //初始化参数
            myglsurfaceview.setYuvDataSize(
                cameraColorW,
                cameraColorH,
                isfullscreen = true,
                mirrorX = true
            )
            testBt1.onClick {
                ToastUtils.showLong("副屏点击按钮测试")
            }
        }

        fun setFaces(rect: RectData) {
            faceview222.setFaces(rect.rect, cameraColorW, cameraColorH, true)
        }

        var ismX = false
        var ismY = false
        fun showmirror() {
            ismX = !ismX
            //初始化参数
            myglsurfaceview.setMirrorXY(
                mirrorX = ismX,
                mirrorY = ismY
            )
        }

        fun showmirrorY() {
            ismY = !ismY
            //初始化参数
            myglsurfaceview.setMirrorXY(
                mirrorX = ismX,
                mirrorY = ismY
            )
        }

        //设置预览数据
        fun setYuvData(yuvData: ByteArray?, type: Int) {
            myglsurfaceview.feedData(yuvData, type)
        }

    }
}

