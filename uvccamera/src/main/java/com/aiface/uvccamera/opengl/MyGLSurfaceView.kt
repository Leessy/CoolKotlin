package com.aiface.uvccamera.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log

/**
 *
 * 自定义GLSurfaceView
 *
 * @author Created by 刘承. on 2019/10/12
 *
 * --深圳市尚美欣辰科技有限公司.
 */
class MyGLSurfaceView(context: Context, attributeSet: AttributeSet?) :
    GLSurfaceView(context, attributeSet) {
    companion object {
        private const val TAG = "MyGLSurfaceView"
    }

    constructor(context: Context) : this(context, null)

    private val renderer: MyGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    /**
     * 设置显示方向
     * @param degrees 显示旋转角度（逆时针），有效值是（0, 90, 180, and 270.）
     */
//    fun setDisplayOrientation(degrees: Int) {
//        renderer.setDisplayOrientation(degrees)
//    }


//    /**
//     * 全屏拉伸显示
//     */
//    fun setDisplayFullscreen(b: Boolean) {
//        renderer.setDisplayFullscreen(b)
//    }

    /**
     * 镜像翻转
     */
    fun setMirrorXY(mirrorX: Boolean = false, mirrorY: Boolean = false) {
        renderer.setMirror(mirrorX, mirrorY)
    }

    /**
     * 设置渲染的YUV数据的宽高
     * @param width 宽度
     * @param height 高度
     */
    fun setYuvDataSize(
        width: Int,
        height: Int,
        isfullscreen: Boolean = false,
        mirrorX: Boolean = false,
        mirrorY: Boolean = false
    ) {
        Log.d(TAG, "setYuvDataSize $width * $height")
        renderer.setDisplayFullscreen(isfullscreen)
        renderer.setYuvDataSize(width, height, mirrorX, mirrorY)
    }

    /**
     * 填充预览YUV格式数据
     * @param yuvData yuv格式的数据
     * @param type YUV数据的格式 0 -> I420  1 -> NV12  2 -> NV21
     */
    fun feedData(yuvData: ByteArray?, type: Int = 0) {
        if (yuvData == null) {
            return
        }
        renderer.feedData(yuvData, type)
        // 请求渲染新的YUV数据
        requestRender()
    }

//
//    //镜像
//    fun dataMirror(data: ByteArray, width: Int, height: Int) {
//        var tempData: Byte
//        for (i in 0 until height * 3 / 2) {
//            for (j in 0 until width / 2) {
//                tempData = data[i * width + j]
//                data[i * width + j] = data[(i + 1) * width - 1 - j]
//                data[(i + 1) * width - 1 - j] = tempData
//            }
//        }
//    }
//
//    //Y Y Y Y ... U V . --NV12       Y Y Y Y ... V U--NV21
//    fun NV21ToYuv420P(nv12: ByteArray, yuv420p: ByteArray, width: Int, height: Int): ByteArray {
//        val ySize = width * height
//        var i: Int
//        var j: Int
//        //y
//        i = 0
//        while (i < ySize) {
//            yuv420p[i] = nv12[i]
//            i++
//        }
//        //V
//        i = 0
//        j = 0
//        while (j < ySize / 2) {
//            yuv420p[ySize * 5 / 4 + i] = nv12[ySize + j]
//            i++
//            j += 2
//        }
//        //U
//        i = 0
//        j = 1
//        while (j < ySize / 2) {
//            yuv420p[ySize + i] = nv12[ySize + j]
//            i++
//            j += 2
//        }
//        return yuv420p
//    }
//
//    //NV21: YYYY VUVU  镜像
//    internal fun NV21_mirror(nv21_data: ByteArray, width: Int, height: Int): ByteArray {
//        var i: Int
//        var left: Int
//        var right: Int
//        var temp: Byte
//        var startPos = 0
//
//        //mirrorY
//        i = 0
//        while (i < height) {
//            left = startPos
//            right = startPos + width - 1
//            while (left < right) {
//                temp = nv21_data[left]
//                nv21_data[left] = nv21_data[right]
//                nv21_data[right] = temp
//                left++
//                right--
//            }
//            startPos += width
//            i++
//        }
//
//
//        //mirrorUandV
//        val offset = width * height
//        startPos = 0
//        i = 0
//        while (i < height / 2) {
//            left = offset + startPos
//            right = offset + startPos + width - 2
//            while (left < right) {
//                temp = nv21_data[left]
//                nv21_data[left] = nv21_data[right]
//                nv21_data[right] = temp
//                left++
//                right--
//
//                temp = nv21_data[left]
//                nv21_data[left] = nv21_data[right]
//                nv21_data[right] = temp
//                left++
//                right--
//            }
//            startPos += width
//            i++
//        }
//        return nv21_data
//    }
}