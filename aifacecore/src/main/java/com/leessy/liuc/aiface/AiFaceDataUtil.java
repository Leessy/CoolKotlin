package com.leessy.liuc.aiface;

import android.text.TextUtils;
import android.util.Log;
import com.AiChlFace.AiChlFace;
import com.AiChlIrFace.AiChlIrFace;
import com.AiFace.AiFace;
import com.AiIrFace.AiIrFace;

public class AiFaceDataUtil {
    private static final String TAG = "AiFaceDataUtil";
    /**
     * 读取存储数据最大长度
     */
    public static final int MAX_LENGTH = 64;
    public static final int SN_MAX_LENGTH = 16;
    public static final int SERIAL_NUMBER_MAX_LENGTH = 16;

    /**
     * 读出全部数据
     * <p>
     * 根据 调用init方法的结果来进行准确调用
     *
     * @param bytes 读取数据内容
     * @return 读取成功 返回 0,失败返回其他
     */
    public static int readData64(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 48;
        }
        int ret = -1;
        if (bytes == null || bytes.length != MAX_LENGTH) {
            return ret;
        }
        if (AiFace.inits == 0 && AiFace.AiDogReadData(bytes, MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlFace.inits == 0 && AiChlFace.AiDogReadData(bytes, MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiIrFace.inits == 0 && AiIrFace.AiDogReadData(bytes, MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlIrFace.inits == 0 && AiChlIrFace.AiDogReadData(bytes, MAX_LENGTH) == 0) {
            ret = 0;
        }
        return ret;
    }

    /**
     * 写入设备编码数据
     *
     * @param sn 设备sn
     * @return 返回0成功  其他失败
     */
    public static int writeSnData(String sn) {
        int ret = -1;
        if (TextUtils.isEmpty(sn) || sn.length() != SN_MAX_LENGTH) {
            return ret;
        }
        if (AiFace.inits == 0 && AiFace.AiDogWriteData(sn.getBytes(), SN_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlFace.inits == 0 && AiChlFace.AiDogWriteData(sn.getBytes(), SN_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiIrFace.inits == 0 && AiIrFace.AiDogWriteData(sn.getBytes(), SN_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlIrFace.inits == 0 && AiChlIrFace.AiDogWriteData(sn.getBytes(), SN_MAX_LENGTH) == 0) {
            ret = 0;
        }
        return ret;
    }

    /**
     * 写入 设备编码数据
     *
     * @param SerialNumber 设备编码
     * @return 返回0成功  其他失败
     */
    public static int writeSerialNumber(String SerialNumber) {
        int ret = -1;
        if (TextUtils.isEmpty(SerialNumber) || SerialNumber.length() != SERIAL_NUMBER_MAX_LENGTH) {
            return ret;
        }

        byte[] bytes = new byte[MAX_LENGTH];
        int i = readData64(bytes);
        if (i != 0) {
            return ret;
        }
        //获取 UUID 拼接数据重新写入
        String uuid = getUUID(bytes);
        if (TextUtils.isEmpty(uuid) || uuid.length() != SN_MAX_LENGTH) {
            uuid = "0000000000000000";//UUID 为空时默认数据
        }
        String data = uuid + SerialNumber;
        Log.d(TAG, "writeSerialNumber: 开始写入 data=" + data);
        if (AiFace.inits == 0 && AiFace.AiDogWriteData(data.getBytes(), SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiIrFace.inits == 0 && AiIrFace.AiDogWriteData(data.getBytes(), SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlFace.inits == 0 && AiChlFace.AiDogWriteData(data.getBytes(), SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH) == 0) {
            ret = 0;
        } else if (AiChlIrFace.inits == 0 && AiChlIrFace.AiDogWriteData(data.getBytes(), SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH) == 0) {
            ret = 0;
        }
        return ret;
    }

    /**
     * 截取内容中 UUID 0-16
     *
     * @param bytes
     * @return
     */
    public static String getUUID(byte[] bytes) {
        if (bytes == null || bytes.length != MAX_LENGTH || !checkChars(bytes, SN_MAX_LENGTH)) {
            return "";
        }
        String trim = new String(bytes, 0, SN_MAX_LENGTH).trim();
        if (trim.equals("0000000000000000") || trim.length() != 16) {
            return "";
        }
        return trim;
    }

    /**
     * 截取 设备编号 16-32
     *
     * @param bytes
     * @return
     */
    public static String getSerialNumber(byte[] bytes) {
        if (bytes == null || bytes.length != MAX_LENGTH || !checkCharsSerialNumber(bytes, SN_MAX_LENGTH, SN_MAX_LENGTH + SERIAL_NUMBER_MAX_LENGTH)) {
            return "";
        }
        String trim = new String(bytes, SN_MAX_LENGTH, SERIAL_NUMBER_MAX_LENGTH).trim();
        if (trim.equals("0000000000000000")) {
            return "";
        }
        return trim;
    }

    /**
     * 检验非法字符
     *
     * @param bytes
     * @return 含有非法字符 false
     */
    private static boolean checkChars(byte[] bytes, int length) {
        for (int i = 0; i < length; i++) {
            if (bytes[i] < 48 || bytes[i] > 122) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkCharsSerialNumber(byte[] bytes, int start, int length) {
        for (int i = start; i < length; i++) {
            if (bytes[i] < 48 || bytes[i] > 122) {
                return false;
            }
        }
        return true;
    }
}
