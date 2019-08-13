package com.leessy

import android.graphics.Bitmap
import android.text.TextUtils
import java.io.*

/**
 *
 * @Author:  陈博
 * @create time:  2019/4/10  16:08
 */
object FileUtils {
    /**
     * 保存Bitmap
     * @param bitmap
     * @param dirPath 保存bitmap的文件夹
     * @return 图片路径
     */
    fun bitmapToFile(bitmap: Bitmap?, dirPath: String, name: String?): String? {
        if (bitmap == null) {
            return null
        }

        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        try {
            var fileName = if (TextUtils.isEmpty(name)) System.currentTimeMillis().toString() + ".jpg" else "$name.jpg"
            val targetFile = File(dir, fileName)
            val outputStream = FileOutputStream(targetFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            //确保图片保存后不会出现0kb
            outputStream.fd.sync()
            outputStream.close()
            return targetFile.absolutePath
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 删除文件
     * @param path 文件路径
     */
    fun delFile(path: String) {
        if (!TextUtils.isEmpty(path)) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
    }

    /**
     * 从文件路径中截取文件名
     * @param path 文件路径
     * @return
     */
    fun getFileNameByPath(path: String): String? {
        var name = System.currentTimeMillis().toString() + ""
        try {
            if (TextUtils.isEmpty(path)) {
                return null
            }
            val index = path.lastIndexOf("/")
            name = path.substring(index + 1, path.length)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return name
    }

    /**
     * 文件拷贝
     * @param src  源文件路径
     * @param target 目标文件夹
     */
    fun copyFile(src: String, target: String) {
        if (TextUtils.isEmpty(src) || TextUtils.isEmpty(target)) {
            return
        }

        val targetDir = File(target)
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }

        val file = File(src)
        if (!file.exists()) {
            return
        }

        val name = file.name
        val newFile = File(target, name)
        try {
            val inputStream = FileInputStream(file)
            val outputStream = FileOutputStream(newFile)
            val buf = ByteArray(1024)
            while (inputStream.read(buf, 0, buf.size) > 0) {
                outputStream.write(buf, 0, buf.size)
            }

            outputStream.flush()
            outputStream.fd.sync()
            outputStream.close()
            inputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}