package com.leessy.mediarecord

import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.Toast
import com.aiface.uvccamera.camera.Camera
import com.aiface.uvccamera.camera.CamerasMng
import com.aiface.uvccamera.camera.IFrameCall
import com.leessy.coolkotlin.R
import com.leessy.mediarecord.encodemp4.VideoRecorder
import kotlinx.android.synthetic.main.activity_ai_face_core_test.*
import java.nio.ByteBuffer

/**
 * 视频录制测试
 */
class MediaRecordActivity : AppCompatActivity() {
    var c: Camera? = null
    var cameraColorW = 1280
    var cameraColorH = 720

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_record)
        initcamera()
//        avcEncoder = AvcEncoder(cameraColorW, cameraColorH, 25)
//        avcEncoder?.createfile("/storage/emulated/0", "录制" + System.currentTimeMillis() + ".avc")
    }

    private var mRecorder: VideoRecorder? = null
    private var path: String? = null

    var num = 0L

    var call = object : IFrameCall {
        override fun call(bf: ByteBuffer, w: Int, h: Int) {
            Log.d("相机回调速度", "------------ ")
            if (num++ % 2 == 0L) {
                val bytes = ByteArray(bf.capacity())
                bf.get(bytes, 0, bytes.size)
//            avcEncoder?.StartRecord(bytes)
//            if (mRecorder != null) {
//                mRecorder?.addVideoData(bytes)
//            }
                MeadiaRecordMng.addDatas(bytes)
            }
        }
    }

    fun start(view: View) {
        Toast.makeText(this, "start record...", Toast.LENGTH_SHORT).show()
//        startRecord()
//        path = "/storage/emulated/0/MV" + System.currentTimeMillis() + ".mp4"
//        mRecorder = VideoRecorder(path)
//        mRecorder?.start()
        MeadiaRecordMng.startRecordVideo(
            "/storage/emulated/0/MV" + System.currentTimeMillis() + ".mp4",
            cameraColorW, cameraColorH
        )
    }


    fun stop(view: View) {
        MeadiaRecordMng.stopRecordVideo()
//        Toast.makeText(this, "saved: $path", Toast.LENGTH_LONG).show()
//        stopRecord()
//        mRecorder?.stop()
//        mRecorder = null
        finish()
    }


    private var recordThread: Thread? = null
    private var isStart = false
    private val bufferSize = AudioRecord.getMinBufferSize(
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    private val mAudioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        44100,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize * 2
    )

    /**
     * 销毁线程方法
     */
    private fun destroyThread() {
        try {
            isStart = false
            if (null != recordThread && Thread.State.RUNNABLE == recordThread!!.state) {
                try {
                    Thread.sleep(500)
                    recordThread!!.interrupt()
                } catch (e: Exception) {
                    recordThread = null
                }

            }
            recordThread = null
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            recordThread = null
        }
    }

    /**
     * 启动录音线程
     */
    private fun startThread() {
        destroyThread()
        isStart = true
        if (recordThread == null) {
            recordThread = Thread(recordRunnable)
            recordThread!!.start()
        }
    }

    /**
     * 录音线程
     */
    internal var recordRunnable: Runnable = Runnable {
        try {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO)
            var bytesRecord: Int
            //int bufferSize = 320;
            val tempBuffer = ByteArray(bufferSize)
            if (mAudioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                stopRecord()
                return@Runnable
            }
            mAudioRecord.startRecording()
            //writeToFileHead();
            while (isStart) {
                if (null != mAudioRecord) {
                    bytesRecord = mAudioRecord.read(tempBuffer, 0, bufferSize)
                    if (bytesRecord == AudioRecord.ERROR_INVALID_OPERATION || bytesRecord == AudioRecord.ERROR_BAD_VALUE) {
                        continue
                    }
                    if (bytesRecord != 0 && bytesRecord != -1) {
                        if (mRecorder != null) {
                            mRecorder?.addAudioData(tempBuffer)
                        }
                    } else {
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 启动录音
     *
     */
    fun startRecord() {
        try {
            startThread()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 停止录音
     */
    fun stopRecord() {
        try {
            destroyThread()
            if (mAudioRecord != null) {
                if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                    mAudioRecord.stop()
                }
                if (mAudioRecord != null) {
                    mAudioRecord.release()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun initcamera() {
        //获取设备列表
        CamerasMng.cameraList.forEach {
            Log.d("CamerasMng", "CamerasMng pid ${it.pid}")
            if (it.pid == 3) {//33073
//            if (it.pid != 0) {//33073
                c = it
                c?.openCamera()
                c?.setPreviewSize(cameraColorW, cameraColorH, max_fps = 25)
                c?.setFrameCall(call)
            }
        }

        textureview.setSurfaceTextureListener(object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surfaceTexture: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                surfaceTexture?.hashCode()
                Log.d("----", "--   onSurfaceTextureAvailable$width  $height")
                MatrixView(textureview, width, height)
                c?.startPreview(surfaceTexture)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                Log.d("----", "--   onSurfaceTextureSizeChanged$width  $height")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//                Log.d("----", "--   onSurfaceTextureUpdated")
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                Log.d("----", "--   onSurfaceTextureDestroyed")
                c?.stopPreview()
                return true
            }
        })

    }

    //视图转换  镜像
    private fun MatrixView(textureView: TextureView, w: Int, h: Int) {
        val matrix = Matrix()
        textureView.getTransform(matrix)
        //                matrix.postRotate(90, i / 2, i1 / 2);//绕某个点旋转90度，这里选择的原点是图片的中心点
        //                matrix.setSinCos(1, 0, i / 2, i1 / 2);//把图像旋转90度，那么90度对应的sin和cos分别是1和0。
        matrix.setScale(-1f, 1f)
        matrix.postTranslate(w.toFloat(), 0f)
        textureView.setTransform(matrix)
    }


}
