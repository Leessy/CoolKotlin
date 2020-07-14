package com.leessy.coolkotlin

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.printer.PicFromPrintUtils
import com.printer.PrinterText
import java.io.IOException
import java.io.InputStream

/**
 * 打印机测试
 */
class PrinterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer)
        PrinterText.instance().create("/dev/ttyS4")
    }

    fun printerBitmap(view: View) {
        var bmp: Bitmap? = getBitmap() ?: return
        bmp = PicFromPrintUtils.compressBitmap(bmp, 360, 120)
        val data = PicFromPrintUtils.draw2PxPoint(bmp)
        PrinterText.instance().sendCommand(0x1B, 0x41, 0x01);    //图片居中指令
        PrinterText.instance().PrintByte(data)
    }

    fun printertext(view: View) {
        PrinterText.instance().printer("aaa", "BBB", "CCC", "111111")
    }

    private fun getBitmap(): Bitmap? {
        try {
            var inputStream: InputStream = resources.assets.open("printerlogo.jpg")
            var bmplogo = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return bmplogo
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}