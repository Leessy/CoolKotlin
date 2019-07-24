package com.aiface.uvccamera.camera

import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext

abstract class base {
    protected val SURFACE_TEXTURE = SurfaceTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)//不显示时做接收数据源使用
    var devName = ""
    var vid = 0
    var pid = 0
    var mBusNum = 0
    var mDevNum = 0

}