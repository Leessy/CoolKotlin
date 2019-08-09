package com.leessy.ofm1000test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 *
 * @Author:  陈博
 * @create time:  2019/6/5  15:57
 */
object Base64Utils {

    /**
     * 将图片转换为base64
     */
    fun bitmapToBase64(bitmap: Bitmap?):String?{
        if (bitmap == null){
            return ""
        }
        var baos :ByteArrayOutputStream ? = null
        try {
            baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
            baos?.let {
                it.flush()
                it.close()
                var byteArray = baos.toByteArray()
                return Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        }catch (e:Exception ){}
        return ""
    }

    /**
     * base64转图片
     */
    fun base64ToBitmap(base64:String?):Bitmap?{
        var header = "data:image/png;base64,"
        var header2 = "data:image/jpg;base64,"
        base64?.let {
            var base = if (it.startsWith(header) || it.startsWith(header2))
                it.split(",")[1] else it
            var byteArray = Base64.decode(base,Base64.DEFAULT)
            byteArray?.let {
                array->
                return BitmapFactory.decodeByteArray(array,0,array.size)
            }
        }
        return null
    }
}