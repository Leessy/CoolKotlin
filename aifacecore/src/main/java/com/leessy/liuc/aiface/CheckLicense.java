package com.leessy.liuc.aiface;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.leessy.aifacecore.BuildConfig;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class CheckLicense {
    private static final String LCSP = "CheckLicense";//sp文件
    private static final String CARD = "CARD";
    private static final String DM2016 = "DM2016";

    /**
     * 授权类型 权限区分判断
     *
     * @param mContext
     * @param strCacheDir
     * @return
     */
    public static boolean UpDateLicense(Context mContext, String strCacheDir, int nAuthType) {
        if (isLicense(mContext, nAuthType)) {
            android.util.Log.d(LCSP, "already write License!!!");
            return false;
        }
        String s = mContext.getApplicationInfo().nativeLibraryDir;
        if (TextUtils.isEmpty(s)) {
            String pkg = mContext.getPackageName();
            s = "/data/data/" + pkg + "/lib";///data/data/com.onfacemind.aiface902
        }
        return cmd_copy(s + "/libUpdateLicense.so", strCacheDir + "/libUpdateLicense.so");
    }

    /**
     * 是否已写入授权
     *
     * @param context
     * @return
     */
    private static boolean isLicense(Context context, int nAuthType) {
        String key = null;
        if (nAuthType == 2) {
            dm2016();
            key = BuildConfig.VERSION_NAME + CARD;
        } else if (nAuthType == 3) {
            key = BuildConfig.VERSION_NAME + DM2016;
        }
        SharedPreferences preferences = context.getSharedPreferences(LCSP, Context.MODE_PRIVATE);
        boolean aBoolean = preferences.getBoolean(key, false);
        if (aBoolean) {
            return true;
        } else {
            preferences.edit().putBoolean(key, true).apply();
            return false;
        }
    }

    //拷贝文件
    private static boolean cmd_copy(String dr1, String dr2) {
        android.util.Log.d(LCSP, "cmd_copy: start" + dr1 + "--" + dr2);
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command1 = "chmod 777 " + dr1 + "\n";
            String command = "cp " + dr1 + " " + dr2 + "\n";//有效 -2文件无效
//            String command = "dd if=" + dr1 + " of=" + dr2;//有效
            String command2 = "chmod 777 " + dr2 + "\n";
            dataOutputStream.write(command1.getBytes(Charset.forName("utf-8")));
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.write(command2.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            android.util.Log.d(LCSP, "cmd_copy: end");
            result = true;
        } catch (Exception e) {
            android.util.Log.e(LCSP, e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                android.util.Log.e(LCSP, e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * dm2016
     */
    private static void dm2016() {
        DataOutputStream dataOutputStream = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            String command1 = "chmod 777 dev/dm2016\n";
            dataOutputStream.write(command1.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
        } catch (Exception var17) {
            android.util.Log.e("CheckLicense dm2016", var17.getMessage(), var17);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException var16) {
                android.util.Log.e("CheckLicense dm2016", var16.getMessage(), var16);
            }

        }
    }
}
