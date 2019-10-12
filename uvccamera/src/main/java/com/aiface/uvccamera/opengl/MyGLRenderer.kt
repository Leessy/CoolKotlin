package com.aiface.uvccamera.opengl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import java.nio.ByteBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * GLSurfaceView.Renderer 渲染类
 * @author Created by 刘承. on 2019/10/12
 *
 * --深圳市尚美欣辰科技有限公司.
 */
class MyGLRenderer : GLSurfaceView.Renderer {
    companion object {
        private const val TAG = "MyGLRenderer"
    }

    private lateinit var mProgram: MyGLProgram
    // GLSurfaceView宽度
    private var mScreenWidth: Int = 0
    // GLSurfaceView高度
    private var mScreenHeight: Int = 0
    // 预览YUV数据宽度
    private var mVideoWidth: Int = 0
    // 预览YUV数据高度
    private var mVideoHeight: Int = 0

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    var isStartDrawYuv = false

    var isFullscreen = false


    //  x +向 默认值1 0 0   Y +默认值0 1 0        Z +默认值0 0 1
    //  x   y   z            1   0   0
    //  x   y   z            0   1   0
    //  x   y   z            0   0   1

    //cos(90)=0;cos(180)=-1;cos(270)=0;cos(0)=1;
    //sin(90)=1;sin(180)=0;sin(270)=-1;sin(0)=0;
    //旋转变换（Rotation）通用为沿Z轴旋转
    //  cos(angle)       -sin(angle)     z
    //  sin(angle)       cos(angle)      z
    //  x                 y              z

    //沿X轴旋转
    //  x       y              z
    //  x       cos(angle)     -sin(angle)
    //  x       sin(angle)     cos(angle)

    //沿Y轴旋转
    //  cos(angle)        y     sin(angle)
    //  x                 y     z
    //  -sin(angle)       y     cos(angle)

    //旋转90°示例
//    private val CMartix = floatArrayOf(
//        0.0f, -1.0f, 0.0f, 0.0f,
//        1.0f, 0.0f, 0.0f, 0.0f,
//        0.0f, 0.0f, 1.0f, 0.0f,
//        0.0f, 0.0f, 0.0f, 1.0f
//    )
//    //旋转180°示例
//    private val CMartix = floatArrayOf(
//        -1.0f, 0.0f, 0.0f, 0.0f,
//        0.0f, -1.0f, 0.0f, 0.0f,
//        0.0f, 0.0f, 1.0f, 0.0f,
//        0.0f, 0.0f, 0.0f, 1.0f
//    )

//    //沿Y轴旋转90°示例  相当于宽的缩放效果
//    private val CMartix = floatArrayOf(
//        0.5f, 0.0f, 0.8f, 0.0f,
//        0.0f, 1.0f, 0.0f, 0.0f,
//        -0.8f, 0.0f, 0.5f, 0.0f,
//        0.0f, 0.0f, 0.0f, 1.0f
//    )

//    //左右翻转示例 改变X轴视角方向（镜像）
//    private val CMartix = floatArrayOf(
//        -1.0f, 0.0f, 0.0f, 0.0f,
//        0.0f, 1.0f, 0.0f, 0.0f,
//        0.0f, 0.0f, 1.0f, 0.0f,
//        0.0f, 0.0f, 0.0f, 1.0f
//    )
//    //上下翻转示例 改变Y轴视角方向
//    private val CMartix = floatArrayOf(
//        1.0f, 0.0f, 0.0f, 0.0f,
//        0.0f, -1.0f, 0.0f, 0.0f,
//        0.0f, 0.0f, 1.0f, 0.0f,
//        0.0f, 0.0f, 0.0f, 1.0f
//    )

    //    翻转矩阵？ 将Y坐标翻转// 反转Y轴坐标 ，使输出的图像为正向
    //正常输入
    private val CMartix = floatArrayOf(
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
    )

    // y分量数据
    private var y: ByteBuffer = ByteBuffer.allocate(0)
    // u分量数据
    private var u: ByteBuffer = ByteBuffer.allocate(0)
    // v分量数据
    private var v: ByteBuffer = ByteBuffer.allocate(0)
    // uv分量数据
    private var uv: ByteBuffer = ByteBuffer.allocate(0)

    // YUV数据格式 0 -> I420  1 -> NV12  2 -> NV21
    private var type: Int = 0
    // 标识GLSurfaceView是否准备好
    private var hasVisibility = false

    //  Called once to set up the view's OpenGL ES environment.
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        //GLclampf red,GLclampf green,Glclampf blue,GLclampf alpha
        GLES20.glClearColor(MyGLColor.red, MyGLColor.green, MyGLColor.blue, MyGLColor.alpha)
        // 配置OpenGL ES 环境
        mProgram = MyGLProgram()
    }

    //  Called if the geometry of the view changes, for example when the device's screen orientation changes.
    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        mScreenWidth = width
        mScreenHeight = height

//        val ratio: Float = width.toFloat() / height.toFloat()
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
//        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

        // Set the camera position (View matrix)
//        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 1.0f, 0.0f, 0.0f)


        if (mVideoWidth > 0 && mVideoHeight > 0) {
            createBuffers(mVideoWidth, mVideoHeight)
        }
        hasVisibility = true
        Log.d(TAG, "onSurfaceChanged width:$width * height:$height")
    }

    //  Called for each redraw of the view.
    override fun onDrawFrame(unused: GL10) {
        if (!isStartDrawYuv) {//还没有yuv数据的时候画背景数据
            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            return
        }
        synchronized(this) {
            if (y.capacity() > 0) {
                y.position(0)
                if (type == 0) {
                    u.position(0)
                    v.position(0)
                    mProgram.feedTextureWithImageData(y, u, v, mVideoWidth, mVideoHeight)
                } else {
                    uv.position(0)
                    mProgram.feedTextureWithImageData(y, uv, mVideoWidth, mVideoHeight)
                }
                // Redraw background color
//                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

                // Calculate the projection and view transformation
//                Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

                try {
                    mProgram.drawTexture(CMartix, type)
                } catch (e: Exception) {
                    Log.w(TAG, e.message)
                }
            }
        }
    }

    /**
     * 设置显示方向
     * @param degrees 显示旋转角度（逆时针），有效值是（0, 90, 180, and 270.）
     */
    fun setDisplayOrientation(degrees: Int) {
        android.graphics.Matrix()

        // Set the camera position (View matrix)
        if (degrees == 0) {
            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 1.0f, 0.0f, 0.0f)
        } else if (degrees == 90) {
            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0.0f, 1.0f, 0.0f)
        } else if (degrees == 180) {
            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, -1.0f, 0.0f, 0.0f)
        } else if (degrees == 270) {
            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0.0f, -1.0f, 0.0f)
        } else {
            Log.e(TAG, "degrees pram must be in (0, 90, 180, 270) ")
        }
    }


    /**
     * 是否全屏显示，默认拉伸
     */
    fun setDisplayFullscreen(b: Boolean) {
        isFullscreen = b
    }

    /**
     * 镜像翻转处理
     */
    fun setMirror(mirrorX: Boolean, mirrorY: Boolean) {
        CMartix[0] = if (mirrorX) -1f else 1f
        CMartix[5] = if (mirrorY) -1f else 1f
    }

    /**
     * 设置渲染的YUV数据的宽高
     * @param width 宽度
     * @param height 高度
     */
    fun setYuvDataSize(
        width: Int,
        height: Int,
        mirrorX: Boolean = false,
        mirrorY: Boolean = false
    ) {
        setMirror(mirrorX, mirrorY)
        if (width > 0 && height > 0) {
            // 调整比例
            createBuffers(width, height)
            // 初始化容器
            if (width != mVideoWidth && height != mVideoHeight) {
                this.mVideoWidth = width
                this.mVideoHeight = height
                val yarraySize = width * height
                val uvarraySize = yarraySize / 4
                synchronized(this) {
                    y = ByteBuffer.allocate(yarraySize)
                    u = ByteBuffer.allocate(uvarraySize)
                    v = ByteBuffer.allocate(uvarraySize)
                    uv = ByteBuffer.allocate(uvarraySize * 2)
                }
            }
        }
    }

    /**
     * 调整渲染纹理的缩放比例
     * @param width YUV数据宽度
     * @param height YUV数据高度
     */
    private fun createBuffers(
        width: Int,
        height: Int
    ) {
        if (mScreenWidth > 0 && mScreenHeight > 0) {
            val f1 = mScreenHeight.toFloat() / mScreenWidth.toFloat()
            val f2 = height.toFloat() / width.toFloat()
            //直接全屏（拉伸）
            if (isFullscreen) {
                mProgram.createBuffers(MyGLProgram.squareVertices)
                return
            }
            //根据比例显示
            if (f1 == f2) {
                mProgram.createBuffers(MyGLProgram.squareVertices)
            } else if (f1 < f2) {
                val widthScale = f1 / f2
                mProgram.createBuffers(
                    floatArrayOf(
                        -widthScale,
                        -1.0f,
                        widthScale,
                        -1.0f,
                        -widthScale,
                        1.0f,
                        widthScale,
                        1.0f
                    )
                )
            } else {
                val heightScale = f2 / f1
                mProgram.createBuffers(
                    floatArrayOf(
                        -1.0f,
                        -heightScale,
                        1.0f,
                        -heightScale,
                        -1.0f,
                        heightScale,
                        1.0f,
                        heightScale
                    )
                )
            }
        }
    }

    /**
     * 预览YUV格式数据
     * @param yuvdata yuv格式的数据
     * @param type YUV数据的格式 0 -> I420  1 -> NV12  2 -> NV21
     */
    fun feedData(yuvdata: ByteArray, type: Int = 0) {
        synchronized(this) {
            if (hasVisibility) {
                this.type = type
                if (type == 0) {
                    y.clear()
                    u.clear()
                    v.clear()
                    y.put(yuvdata, 0, mVideoWidth * mVideoHeight)
                    u.put(yuvdata, mVideoWidth * mVideoHeight, mVideoWidth * mVideoHeight / 4)
                    v.put(
                        yuvdata,
                        mVideoWidth * mVideoHeight * 5 / 4,
                        mVideoWidth * mVideoHeight / 4
                    )
                } else {
                    y.clear()
                    uv.clear()
                    y.put(yuvdata, 0, mVideoWidth * mVideoHeight)
                    uv.put(yuvdata, mVideoWidth * mVideoHeight, mVideoWidth * mVideoHeight / 2)
                }
                isStartDrawYuv = true//开始draw
            }
        }
    }
}