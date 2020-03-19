//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lzw.qlhsshare;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class Tool {
    public Tool() {
    }

    public static int convertByteToInt(byte var0) {
        int var1 = var0 >> 4 & 15;
        int var2 = 15 & var0;
        return var1 * 16 + var2;
    }

    public static int[] convertByteToColor(byte[] var0, int var1, int var2) {
        int var3 = var0.length;
        if (var3 == 0) {
            return null;
        } else {
            int[] var4 = new int[var3 / 3];

            for(int var8 = 0; var8 < var2; ++var8) {
                int var9 = var8 * var1;

                for(int var10 = 0; var10 < var1; ++var10) {
                    int var11 = (var9 + var10) * 3;
                    int var5 = convertByteToInt(var0[var11]);
                    int var6 = convertByteToInt(var0[var11 + 1]);
                    int var7 = convertByteToInt(var0[var11 + 2]);
                    var4[(var2 - var8 - 1) * var1 + var10] = var5 << 16 | var6 << 8 | var7 | -16777216;
                }
            }

            return var4;
        }
    }

    public static Bitmap createRgbBitmap(byte[] var0, int var1, int var2) {
        int[] var3 = convertByteToColor(var0, var1, var2);
        if (var3 == null) {
            return null;
        } else {
            Bitmap var4 = Bitmap.createBitmap(var3, 0, var1, var1, var2, Config.ARGB_8888);
            return var4;
        }
    }
}
