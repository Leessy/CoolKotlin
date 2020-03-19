//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lzw.qlhsshare;


public class Wlt2bmpShare {
    public Wlt2bmpShare() {
    }

    private static native int kaerunpack(byte[] var0, byte[] var1);

    public static int picUnpack(byte[] var0, byte[] var1) {
        try {
            return kaerunpack(var0, var1);
        } catch (Exception var3) {
            var3.printStackTrace();
//            LogUtils.w("picUnpack " + var3.getMessage());
            return 0;
        }
    }

    static {
        try {
            System.loadLibrary("wlt029bmp");//wlt920bmp--kaer
            System.loadLibrary("wlt2bmpshare");//原iskaershare--kaer
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }
}
