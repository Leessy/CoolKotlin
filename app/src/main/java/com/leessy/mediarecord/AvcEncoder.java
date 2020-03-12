package com.leessy.mediarecord;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static android.media.MediaCodec.BUFFER_FLAG_CODEC_CONFIG;
import static android.media.MediaCodec.BUFFER_FLAG_KEY_FRAME;

/**
 * 视频编码  h264
 * 录制视频
 */
public class AvcEncoder {
    private int TIMEOUT_USEC = 12000;
    private MediaCodec mediaCodec;//编码
    private int m_width;//宽
    private int m_height;//高
    private int m_framerate;//帧率
    private long generateIndex = 0;//当前帧； 为了做每帧视频的流畅
    public boolean isStop = true;//是否停止录像； 由于编码视频延时，故先设置标志，不在进行编码录制
    private MediaFormat mediaFormat;//录制格式
    private byte[] configbyte;

    private BufferedOutputStream outputStream;

    public AvcEncoder(int width, int height, int framerate) {//framerate帧率
        m_width = width;
        m_height = height;
        m_framerate = framerate;
        mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);//yuv42sp
//        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);//yuv42sp
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, m_framerate);//帧率大一点，可优化视频卡顿问题
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);//关键帧时间大一点，可优化视频卡顿问题

    }

    /**
     * 创建保存编码后数据的文件
     */
    public void createfile(String path, String name) {
        try {
//            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //配置编码器参数
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mediaCodec.start();//启动编码器

        isStop = false;
        try {
            File f = new File(path);
            if (!f.exists()) {//不存在文件夹，先创建
                f.mkdirs();
            }
            File file = new File(path + "/" + name);//保存的视频的文件
            FileOutputStream fout1 = new FileOutputStream(file);
            outputStream = new BufferedOutputStream(fout1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void StopEncoder() {
        generateIndex = 0;
        try {
            if (mediaCodec == null) {
                return;
            }
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录像
     */
    public synchronized void StopRecord() {
        isStop = true;
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            StopEncoder();
            if (outputStream == null) {
                return;
            }
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录像
     *
     * @param imgbyte
     */
    public synchronized void StartRecord(byte[] imgbyte) {
        if (isStop) {
            return;
        }
        long pts = 0;
        if (imgbyte != null) {
            long n = System.currentTimeMillis();
            try {
                byte[] yuv420sp = new byte[m_width*m_height*3/2];
                //把待编码的视频帧转换为YUV420格式
                NV21ToNV12(imgbyte,yuv420sp,m_width,m_height);
                imgbyte=yuv420sp;

                //编码器输入缓冲区
                ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
                //编码器输出缓冲区
                ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
                int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
                if (inputBufferIndex >= 0) {
                    pts = computePresentationTime(generateIndex);
                    ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                    inputBuffer.clear();
                    //把转换后的YUV420格式的视频帧放到编码器输入缓冲区中
                    inputBuffer.put(imgbyte);
                    mediaCodec.queueInputBuffer(inputBufferIndex, 0, imgbyte.length, pts, 0);
                    generateIndex++;
                }
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                while (outputBufferIndex >= 0) {
                    //Log.i("AvcEncoder", "Get H264 Buffer Success! flag = "+bufferInfo.flags+",pts = "+bufferInfo.presentationTimeUs+"");
                    ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                    byte[] outData = new byte[bufferInfo.size];
                    outputBuffer.get(outData);
                    if (bufferInfo.flags == BUFFER_FLAG_CODEC_CONFIG) {
                        configbyte = new byte[bufferInfo.size];
                        configbyte = outData;
                    } else if (bufferInfo.flags == BUFFER_FLAG_KEY_FRAME) {
                        byte[] keyframe = new byte[bufferInfo.size + configbyte.length];
                        System.arraycopy(configbyte, 0, keyframe, 0, configbyte.length);
                        //把编码后的视频帧从编码器输出缓冲区中拷贝出来
                        System.arraycopy(outData, 0, keyframe, configbyte.length, outData.length);
                        outputStream.write(keyframe, 0, keyframe.length);
                    } else {
                        //写到文件中
                        outputStream.write(outData, 0, outData.length);
                    }
                    mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                    outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                }
            } catch (Throwable t) {
                Log.e("xxxxxx", "写入异常" + t.toString());
                t.printStackTrace();
            }
            long y = System.currentTimeMillis() - n;
            //Log.e("xxxxxx","每针大小="+imgbyte.length+"   第几针="+generateIndex+"   解码和存储时间="+y);
        }
    }

    private void NV21ToNV12(byte[] nv21, byte[] nv12, int width, int height) {
        if (nv21 == null || nv12 == null) return;
        int framesize = width * height;
        int i = 0, j = 0;
        System.arraycopy(nv21, 0, nv12, 0, framesize);
        for (i = 0; i < framesize; i++) {
            nv12[i] = nv21[i];
        }
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j - 1] = nv21[j + framesize];
        }
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j] = nv21[j + framesize - 1];
        }
    }

    /**
     * Generates the presentation time for frame N, in microseconds.
     */
    private long computePresentationTime(long frameIndex) {
        return 132 + frameIndex * 1000000 / m_framerate;
    }
}
