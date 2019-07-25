package com.AiIrFace;

import android.util.Log;
import com.kaer.sdk.otg.OtgReadClient;
import com.kaer.sdk.serial.SerialReadClient;

// SerialReadClient 在卡尔SDK包中的SerialReadClient.class里定义
// 因应用还需要读取身份证信息等，因此卡尔SDK包也由应用程序集成，并由应用程序打开串口

public class ReadWriteCryptIC {
    private static OtgReadClient otgReadClient;
    private static SerialReadClient mSerialPortReadClient;

    private static boolean mDeviceOpened = false;

    public static void SetOtgReadPort(OtgReadClient serialport) {
        otgReadClient = serialport;
        mDeviceOpened = true;
    }

    public static void SetSerialPortObj(SerialReadClient serialport) {
        mSerialPortReadClient = serialport;
        mDeviceOpened = true;
    }

    public static int ReadCryptIC(byte[] bData, int nLen) {
        int ret = -1;
        if (mDeviceOpened == true) {
            if (otgReadClient != null)
                ret = otgReadClient.I2C_ReadCrypt(bData, nLen);
            else if (mSerialPortReadClient != null)
                ret = mSerialPortReadClient.I2C_ReadCrypt(bData, nLen);
            if (ret == 0 || ret == 2) ret = 0;
        }

        if (ret == 0) {
            String str = "Data:";
            for (int i = 0; i < nLen; i++) {
                String s = Integer.toHexString(bData[i] & 0xFF);
                if (s.length() == 1) str += " 0" + s;
                else str += " " + s;
            }
            Log.d("ReadCryptIC", "Read " + str + " OK.");
        } else Log.d("ReadCryptIC", "Read fail. return " + ret);

        return ret;
    }

    public static int WriteCryptIC(byte[] bData, int nLen) {
        int ret = -1;
        if (mDeviceOpened == true) {
            if (otgReadClient != null)
                ret = otgReadClient.I2C_WriteCrypt(bData, nLen);
            else if (mSerialPortReadClient != null) {
                ret = mSerialPortReadClient.I2C_WriteCrypt(bData, nLen);
                if (ret == 0 || ret == 2) ret = 0;
            }
        }

        if (ret == 0) {
            String str = "Data:";
            for (int i = 0; i < nLen; i++) {
                String s = Integer.toHexString(bData[i] & 0xFF);
                if (s.length() == 1) str += " 0" + s;
                else str += " " + s;
            }
            Log.d("WriteCryptIC", "Write " + str + " OK.");
        } else Log.d("WriteCryptIC", "Write fail.return " + ret);

        return ret;
    }
}
