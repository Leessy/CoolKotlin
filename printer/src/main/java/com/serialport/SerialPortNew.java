package com.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class SerialPortNew {

    static {
        System.loadLibrary("SerialPortNew");
    }

    private static final String TAG = SerialPortNew.class.getSimpleName();

    /**
     * 文件设置最高权限 777 可读 可写 可执行
     *
     * @param file 文件
     * @return 权限修改是否成功
     */
    boolean chmod777(File file) {
        if (null == file || !file.exists()) {
            // 文件不存在
            return false;
        }
        try {
            // 获取ROOT权限
            Process su = Runtime.getRuntime().exec("/system/bin/su");
            // 修改文件属性为 [可读 可写 可执行]
            String cmd = "chmod 777 " + file.getAbsolutePath() + "\n" + "exit\n";
            su.getOutputStream().write(cmd.getBytes());
            if (0 == su.waitFor() && file.canRead() && file.canWrite() && file.canExecute()) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            // 没有ROOT权限
            Log.i(TAG, "openSerialPort: 没有ROOT权限 777");
            e.printStackTrace();
        }
        return false;
    }

    // 打开串口
    public native FileDescriptor open(String path, int baudRate, int flags);

    // 关闭串口
    public native void close();

//    ////开启485电频控制
//    public native int openIO();
//
//    ////关闭485电频控制
//    public native void closeIO();
//
//    ////485电频控制  0 or 1
//    public native void ioctl(int isopen);
//
//    ////702红外灯控制  0 or 1
//    public native void LEDioctl(int isopen);

}