package com.leessy.OCR

import android.os.Bundle
import android.util.Log
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.*
import com.google.gson.Gson
import com.leessy.KotlinExtension.onClick
import com.leessy.coolkotlin.BaseActivity
import com.leessy.coolkotlin.R
import kotlinx.android.synthetic.main.activity_ocr.*
import kotlinx.coroutines.launch
import java.io.File

class OcrActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr)
        initocr.onClick { initOcr() }

        ocridcard.onClick { ocridcard() }
        ocrcar.onClick { ocrCar() }
        passport.onClick { passport() }

    }

    //护照
    fun passport() {
        // 驾驶证识别参数设置
        val param = OcrRequestParams()
        param.imageFile = File("/storage/emulated/0/hu.jpg")
//        param.putParam("imageQuality", "85")
        param.putParam("detect_direction", true)

        OCR.getInstance(this)
            .recognizePassport(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    Log.d(localClassName, "ocridcard 数据  " + result.jsonRes)
                    launch {
                        text.text = "护照识别结果 " + result.jsonRes
                    }
                }

                override fun onError(error: OCRError) {
                    Log.d(localClassName, " 数据 onError " + error)
                }
            })

    }

    fun ocrCar() {
        // 驾驶证识别参数设置
        val param = OcrRequestParams()
        // 设置image参数
        param.imageFile = File("/storage/emulated/0/timg2.jpg")
        // 设置其他参数
        param.putParam("detect_direction", true)

        OCR.getInstance(this)
            .recognizeDrivingLicense(param, object : OnResultListener<OcrResponseResult> {
                override fun onResult(result: OcrResponseResult) {
                    // 调用成功，返回OcrResponseResult对象
                    Log.d(localClassName, "ocridcard 数据  " + result.jsonRes)
                    launch {
                        text.text = result.jsonRes
                    }
                }

                override fun onError(error: OCRError) {
                    // 调用失败，返回OCRError对象
                    Log.d(localClassName, " 数据 onError " + error)
                }
            })
    }

    private fun ocridcard() {
        // 身份证识别参数设置
        val param = IDCardParams()
        param.imageFile = File("/storage/emulated/0/DCIM/Camera/IMG_20191115_160112.jpg")
        // 设置身份证正反面
        param.idCardSide = IDCardParams.ID_CARD_SIDE_FRONT
        // 设置方向检测
        param.isDetectDirection = true
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.imageQuality = 85

        // 调用身份证识别服务
        OCR.getInstance(applicationContext)
            .recognizeIDCard(param, object : OnResultListener<IDCardResult> {
                override fun onResult(result: IDCardResult) {
                    // 调用成功，返回IDCardResult对象
                    Log.d(localClassName, "ocridcard 数据  " + result.jsonRes)
                    launch {
                        text.text = result.jsonRes
                    }
                }

                override fun onError(error: OCRError) {
                    // 调用失败，返回OCRError对象
                    Log.d(localClassName, "ocridcard 数据 onError " + error)
                }
            })

    }

    fun initOcr() {
        OCR.getInstance(applicationContext).isAutoCacheToken = true
        OCR.getInstance(applicationContext)
            .initAccessTokenWithAkSk(object : OnResultListener<AccessToken> {
                override fun onResult(result: AccessToken) {
                    // 调用成功，返回AccessToken对象
                    val token = result.accessToken
                    Log.d(localClassName, "initOcr   " + token)

                }

                override fun onError(error: OCRError) {
                    // 调用失败，返回OCRError子类SDKError对象
                    Log.d(localClassName, "initOcr   " + error)
                }
            }, applicationContext, "xhZ7sNBCFCFSGa99DFXt5Cr7", "cUdwjk1eP02muRM3q0Ti8f8fpqGlm8Ar")
    }
}
