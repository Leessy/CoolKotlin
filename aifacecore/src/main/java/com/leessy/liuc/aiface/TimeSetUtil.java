package com.leessy.liuc.aiface;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by 刘承. on 2018/1/6.
 */

public class TimeSetUtil {
    public static boolean isDateTimeAuto(Context context) {
        try {
            return android.provider.Settings.Global.getInt(context.getContentResolver(),
                    android.provider.Settings.Global.AUTO_TIME) > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    //    设置系统的时间是否需要自动获取
    //1==自动获取
    //0==手动设置
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void setAutoDateTime(Context context, int checked) {
        android.provider.Settings.Global.putInt(context.getContentResolver(),
                android.provider.Settings.Global.AUTO_TIME, checked);
    }

    //    参考系统Settings中的源码
    public static void setSysDate(Context context, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) context.getSystemService(ALARM_SERVICE)).setTime(when);
        }
    }

    //    参考系统Settings中的源码
    public static void setSysTime(Context context, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) context.getSystemService(ALARM_SERVICE)).setTime(when);
        }
    }
}
