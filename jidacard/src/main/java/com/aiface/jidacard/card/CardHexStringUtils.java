package com.aiface.jidacard.card;

import android.text.TextUtils;

/**
 * @author Created by 刘承. on 2019/3/20
 * business@onfacemind.com
 */
public class CardHexStringUtils {

    //目前的数据保存读卡数据的16进制字符--------转int  目前全部使用正数 ，负数默认为相反数
    public static int cardHexStringToInt(String s) {
        if (TextUtils.isEmpty(s) || s.length() != 8) {
            return 0;
        }
        return byteArr2Int(decodeHex(s.toCharArray()));
//        return Math.abs(byteArr2Int(decodeHex(s.toCharArray())));
    }

    /**
     * 将字节数组转换成整数
     * 高位在后---目前读卡模块基本使用高位在前的格式
     */
    public static int byteArr2Int(byte[] arr) {
        return (arr[3] & 0xff) << 24
                | (arr[2] & 0xff) << 16
                | (arr[1] & 0xff) << 8
                | (arr[0] & 0xff);
    }


    //把10位卡号转回bytes 高位在后
    public static byte[] intTobytes(int ret) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (ret & 0xff);
        bytes[1] = (byte) (ret >> 8 & 0xff);
        bytes[2] = (byte) (ret >> 16 & 0xff);
        bytes[3] = (byte) (ret >> 24 & 0xff);
        return bytes;
    }

    //byte[] 转回String存储数据库
    public static String byteArrayToHexstring(byte[] bytes, int start, int end) {
        StringBuilder hexString = new StringBuilder();
        if (bytes.length > 0 && bytes != null) {
            for (int i = start; i < end; ++i) {
                int v = bytes[i] & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    hexString.append(0);
                }

                hexString.append(hv);
            }

            return hexString.toString().toUpperCase();
        } else {
            return null;
        }
    }

    /**
     * 转8位卡号
     *
     * @param s
     * @return
     */
    public static String cardHexStringTo8Int(String s) {
        if (TextUtils.isEmpty(s) || s.length() != 8) {
            return "";
        }
        byte[] bytes = chars2Bytes(s.toCharArray());

        //高位在前
        String s2 = (bytes[2] & 0xff) + "";
        String s3s4 = ((bytes[1] & 0xff) << 8 | (bytes[0] & 0xff)) + "";
        return s2 + s3s4;
    }
//    /**
//     * 将字节数组转换成整数
//     * 高位在前
//     */
//    public static int byteArr2Int(byte[] arr) {
//        return (arr[0] & 0xff) << 24
//                | (arr[1] & 0xff) << 16
//                | (arr[2] & 0xff) << 8
//                | (arr[3] & 0xff) << 0;
//
//    }

    //16进制字符串转byte[]
    protected static byte[] decodeHex(char[] data) {
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("字符个数应该为偶数");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public static byte[] chars2Bytes(char[] bs) {
        return decodeHex(bs);
    }
}
