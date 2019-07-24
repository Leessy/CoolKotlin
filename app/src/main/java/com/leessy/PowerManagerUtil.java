package com.leessy;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Created by 刘承. on 2019/7/24
 * <p>
 * --深圳市尚美欣辰科技有限公司.
 */
public class PowerManagerUtil {

    public static void goToSleep(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        try {
            powerManager.getClass().getMethod("goToSleep", new Class[]{long.class}).invoke(powerManager, SystemClock.uptimeMillis());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    /**
     * 唤醒屏幕
     *
     * @param context
     */
    public static void wakeUp(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        try {
            powerManager.getClass().getMethod("wakeUp", new Class[]{long.class}).invoke(powerManager, SystemClock.uptimeMillis());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
