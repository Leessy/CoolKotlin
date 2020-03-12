package com.leessy.mediarecord

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.leessy.mediarecord.encodemp4.VideoRecorder
import java.util.*

/**
 *
 * @author Created by 刘承. on 2019/12/6
 *
 * --深圳市尚美欣辰科技有限公司.
 *
 * 快捷录制视频封装
 */
object MeadiaRecordMng {
    private var mRecorder: VideoRecorder? = null
//    private var path: String? = null

    private var isrecording = false

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

    //视频数据YUV  NV12
    fun addDatas(bytes: ByteArray) {
        if (isrecording) {
            mRecorder?.addVideoData(bytes)
        }
    }

    //开始录制，保存路劲
    fun startRecordVideo(
        file: String,
        frameWidth: Int,
        frameHeight: Int,
        bitRate: Int = 600000
    ) {
        if (isrecording) return
        isrecording = true
        startRecordAudio()
        mRecorder = VideoRecorder(file, frameWidth, frameHeight, bitRate)
        mRecorder?.start()
    }

    //停止录制
    fun stopRecordVideo() {
        isrecording = false
        stopRecordAudio()
        mRecorder?.stop()
        mRecorder = null
    }


    /**
     * 销毁线程方法
     */
    private fun destroyThread() {
        try {
            isStart = false
            if (null != recordThread && Thread.State.RUNNABLE == recordThread!!.state) {
                try {
                    Thread.sleep(200)
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

    var iis = 0
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
                stopRecordAudio()
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
                        if (mRecorder != null && isStart) {
                                mRecorder?.addAudioData(tempBuffer)
                            if (iis++ == 0) {
                            }
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
    private fun startRecordAudio() {
        try {
            startThread()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 停止录音
     */
    private fun stopRecordAudio() {
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

}