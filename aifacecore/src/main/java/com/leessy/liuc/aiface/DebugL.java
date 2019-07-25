package com.leessy.liuc.aiface;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;

/**
 * @author Created by 刘承. on 2018/9/3
 * business@onfacemind.com
 */
public class DebugL {
    private static final String LCSP = "CheckLicense";//sp文件
    private static final String DebugTime = "Debug_lastTime";
    public static final String EXPIRY_DATE = "2018/09/30 00:00:00";

    static Calendar calendar;
    private static int year = 0;
    private static int month = 0;
    private static int day = 0;

    public static void start(Context context) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        long lastTime = getLastTime(context);
        long l = System.currentTimeMillis();
//        if (lastTime - l > 0) {
//        }
        deletChace(context);
        TimeSetUtil.setSysDate(context, 2018, 11, 15);//2018/9/1有效期内日期
    }

    private static void deletChace(Context context) {
        String strCacheDir = context.getCacheDir().getAbsolutePath();
        final String cache = strCacheDir + "/cache";
        final String temp = strCacheDir + "/temp";
        Log.d(LCSP, "deletChace:= " + strCacheDir);

        DataOutputStream dataOutputStream = null;

        try {
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            String command1 = "rm " + cache + "\n";
            String command2 = "rm " + temp + "\n";
            dataOutputStream.write(command1.getBytes(Charset.forName("utf-8")));
            dataOutputStream.write(command2.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
        } catch (Exception var17) {
            android.util.Log.e("deletChace debug", var17.getMessage());
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException var16) {
                android.util.Log.e("deletChace debug", var16.getMessage(), var16);
            }

        }
    }

    public static void finished(Context context) {
        TimeSetUtil.setSysDate(context, year, month, day);//2018/9/1
        setLastTime(context);
    }

    private static long getLastTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(LCSP, Context.MODE_PRIVATE);
        long aLong = preferences.getLong(DebugTime, 0);
        return aLong;
    }

    private static void setLastTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(LCSP, Context.MODE_PRIVATE);
        preferences.edit().putLong(DebugTime, System.currentTimeMillis()).apply();
    }
}
