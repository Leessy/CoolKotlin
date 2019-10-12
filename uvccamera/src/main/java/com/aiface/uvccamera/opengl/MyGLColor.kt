package com.aiface.uvccamera.opengl

/**
 *  glview clear 颜色
 * @author Created by 刘承. on 2019/10/12
 *
 * --深圳市尚美欣辰科技有限公司.
 */
object MyGLColor {
    //默认背景颜色
    var red = 0.3f
    var green = 0.3f
    var blue = 0.3f
    var alpha = 1.0f

    /**
     * 设置背景GLview颜色背景颜色
     */
    fun setBackgroundColor(red: Float, green: Float, blue: Float, alpha: Float) {
        this.red = red
        this.green = green
        this.blue = blue
        this.alpha = alpha
    }

}