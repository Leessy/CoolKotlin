package com.aiface.arcface

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import com.arcsoft.face.*
import com.arcsoft.face.util.ImageUtils
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.name
    private var faceEngine = FaceEngine()

    private var faceEngineCode = -1
    var bmp: Bitmap? = null
    var bytes: ByteArray? = null
    var w = 0
    var h = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var inputStream: InputStream? = null

        try {
            inputStream = this.resources.assets.open("aaaa.jpg")
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
        bmp = BitmapFactory.decodeStream(inputStream)
        w = bmp!!.width
        h = bmp!!.height



        RxView.clicks(activeEngine)
            .observeOn(Schedulers.io())
            .subscribe {
                val activeCode = faceEngine.activeOnline(
                    application, "DPkmbQYAauH9JDXAy5et5RgpyBQKia8i514TTHMzfMC5",
                    "4TF1mVPfTV3d1jjGdTc3UFXXtBi9ea9fmFUfaysNda9Y"
                )

                if (activeCode == ErrorInfo.MOK) {
                    showToast(getString(R.string.active_success))
                } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                    showToast(getString(R.string.already_activated))
                } else {
                    showToast(getString(R.string.active_failed, activeCode))
                }

                val activeFileInfo = ActiveFileInfo()
                val res = faceEngine.getActiveFileInfo(application, activeFileInfo)
                if (res == ErrorInfo.MOK) {
                    Log.i(TAG, activeFileInfo.toString())
                }
            }

        RxView.clicks(init).observeOn(Schedulers.io())
            .subscribe { initEngine() }
        RxView.clicks(start).observeOn(Schedulers.io())
            .subscribe {
                Thread(Runnable {
                    while (true) {
//                        Thread.sleep(1000)
                        start1()
                    }
                }).start()
                Thread(Runnable {
                    while (true) {
//                        Thread.sleep(1000)
                        start1()
                    }
                }).start()

            }
    }

    //开始识别人脸
    private fun start1() {
        if (bmp == null) return
        //bitmap转bgr
        val s = System.currentTimeMillis()
        val bgr24 = ImageUtils.bitmapToBgr24(bmp)
        Log.i(TAG, "processImage: 图片转换耗时 = " + (System.currentTimeMillis() - s))


        //人脸检测       --------------------------------------------------------------
        val faceInfoList = ArrayList<FaceInfo>()
        val fdStartTime = System.currentTimeMillis()
        val detectCode = faceEngine.detectFaces(bgr24, w, h, FaceEngine.CP_PAF_BGR24, faceInfoList)
        if (detectCode == ErrorInfo.MOK) {
            Log.i(TAG, "processImage:人脸检测耗时= " + (System.currentTimeMillis() - fdStartTime))
            Log.i(TAG, "processImage:人脸检测 num=${faceInfoList.size}   ${faceInfoList[0]}")
        }


        //获取3维和角度信息--------------------------------------------------------------
        val processStartTime = System.currentTimeMillis()
        val faceProcessCode = faceEngine.process(
            bgr24,
            w,
            h,
            FaceEngine.CP_PAF_BGR24,
            faceInfoList,
            FaceEngine.ASF_AGE//年龄信息
                    or FaceEngine.ASF_GENDER//性别
                    or FaceEngine.ASF_FACE3DANGLE//3维角度
//                    or FaceEngine.ASF_LIVENESS//活体信息
        )
        if (faceProcessCode == ErrorInfo.MOK) {
            Log.i(TAG, "processImage:人脸详细信息获取 耗时 = " + (System.currentTimeMillis() - processStartTime))
        }
        //年龄信息结果
        val ageInfoList = ArrayList<AgeInfo>()
        //性别信息结果
        val genderInfoList = ArrayList<GenderInfo>()
        //人脸三维角度结果
        val face3DAngleList = ArrayList<Face3DAngle>()
        //活体检测结果
        val livenessInfoList = ArrayList<LivenessInfo>()
        //获取年龄、性别、三维角度、活体结果
        val ageCode = faceEngine.getAge(ageInfoList)
        val genderCode = faceEngine.getGender(genderInfoList)
        val face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList)
        val livenessCode = faceEngine.getLiveness(livenessInfoList)

        if (ageCode or genderCode or livenessCode == ErrorInfo.MOK) {//ageCode or genderCode or
            for (i in ageInfoList.indices) {
                Log.i(TAG, "processImage:人脸信息 年龄= ${ageInfoList[i].age}")
            }
            for (i in genderInfoList.indices) {
                Log.i(TAG, "processImage:人脸信息 性别= ${genderInfoList[i].gender}")
            }

//        if (livenessCode == ErrorInfo.MOK) {

            for (i in face3DAngleList.indices) {
                Log.i(
                    TAG, "processImage:人脸信息 3维= ${face3DAngleList[i].pitch}" +
                            " **${face3DAngleList[i].pitch}" +
                            " **${face3DAngleList[i].roll}" +
                            " **${face3DAngleList[i].status}" +
                            " **${face3DAngleList[i].yaw}"
                )
            }
            for (i in genderInfoList.indices) {
                Log.i(
                    TAG, "processImage:人脸信息 活体= ${when (livenessInfoList[i].liveness) {
                        LivenessInfo.ALIVE -> "ALIVE"
                        LivenessInfo.NOT_ALIVE -> "NOT_ALIVE"
                        LivenessInfo.UNKNOWN -> "UNKNOWN"
                        LivenessInfo.FACE_NUM_MORE_THAN_ONE -> "FACE_NUM_MORE_THAN_ONE"
                        else -> "UNKNOWN"
                    }}"
                )
            }
        }
//        提取人脸特征数据
        var startget = System.currentTimeMillis()
        val faceFeature = FaceFeature()
        //特征提取
        var code = faceEngine.extractFaceFeature(
            bgr24, w, h, FaceEngine.CP_PAF_BGR24,
            faceInfoList[0], faceFeature
        )
        if (code == ErrorInfo.MOK) {
            Log.i(TAG, "processImage:人脸特征码获取 耗时 = " + (System.currentTimeMillis() - startget))
        }


    }

    //初始化
    private fun initEngine() {
        var start = System.currentTimeMillis()
        faceEngine = FaceEngine()
        faceEngineCode = faceEngine.init(
            this,
            FaceEngine.ASF_DETECT_MODE_IMAGE,
            FaceEngine.ASF_OP_0_ONLY,
            16,
            10,
            FaceEngine.ASF_FACE_RECOGNITION or FaceEngine.ASF_FACE_DETECT or FaceEngine.ASF_AGE or FaceEngine.ASF_GENDER or FaceEngine.ASF_FACE3DANGLE or FaceEngine.ASF_LIVENESS
        )
        val versionInfo = VersionInfo()
        faceEngine.getVersion(versionInfo)
        Log.i(
            TAG,
            "initEngine  初始化结果: init: $faceEngineCode  version:$versionInfo   时间${System.currentTimeMillis() - start}"
        )
    }

    /**
     * 销毁引擎
     */
    private fun unInitEngine() {
        if (faceEngine != null) {
            faceEngineCode = faceEngine.unInit()
            Log.i(TAG, "unInitEngine: $faceEngineCode")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unInitEngine()
        Log.d(TAG, "onDestroy: ")
    }

    var toast: Toast? = null
    private fun showToast(s: String) {
        Log.i(TAG, "process:----=${s}")
//
//        if (toast == null) {
//            toast = Toast.makeText(this, s, Toast.LENGTH_SHORT)
//        } else {
//            toast?.setText(s)
//        }
//        runOnUiThread {
//            toast?.show()
//        }
    }
}
