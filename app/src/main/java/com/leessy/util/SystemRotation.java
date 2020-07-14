package com.leessy.util;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @author Created by 刘承. on 2020/3/31
 * <p>
 * --深圳市尚美欣辰科技有限公司.
 */
public class SystemRotation {
    public static final int ROTATION_180 = 3;
    public static final int ROTATION_270 = 2;
    public static final int ROTATION_90 = 1;
    public static final int ROTATION_0 = 0;

    public static void setRotation(int rotation, Context context) {
        if (getSystemRotation(context) == rotation) {
            //设置方向与系统当前方向一致
            return;
        }
        try {
            Method getServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
            Object ServiceManager = getServiceMethod.invoke(null, new Object[]{"window"});
            Class<?> cStub = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = cStub.getMethod("asInterface", IBinder.class);
            Object iWindowManager = asInterface.invoke(null, ServiceManager);
            Method freezeRotation = iWindowManager.getClass().getMethod("freezeRotation", int.class);
            freezeRotation.invoke(iWindowManager, rotation);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("设置系统方向异常", e.toString());
        }
    }

    //检测系统当前方向
    public static int getSystemRotation(Context c) {
        int angle = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        return angle;
    }



//    public static void setRotation(int i) {
//        try {
//            Method getServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
//            Object ServiceManager = getServiceMethod.invoke(null, new Object[]{"window"});
//            Class<?> cStub = Class.forName("android.view.IWindowManager$Stub");
//            Method asInterface = cStub.getMethod("asInterface", IBinder.class);
//            Object iWindowManager = asInterface.invoke(null, ServiceManager);
////            setforcedDisplaySize = iWindowManager.getClass().getMethod("setForcedDisplaySize", int.class, int.class, int.class);
////            Object clearForcedDisplaySize = iWindowManager.getClass().getMethod("clearForcedDisplaySize", int.class);
//            Method freezeRotation = iWindowManager.getClass().getMethod("freezeRotation", int.class);
//            freezeRotation.invoke(iWindowManager, i);
////            iWindowManager.getClass().getMethod("freezeRotation", int.class).invoke(i);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }

//
//    private void rotation0() {
//        try {
//            IWindowManager wm = WindowManagerGlobal.getWindowManagerService();
//            wm.freezeRotation(Surface.ROTATION_0);
//        } catch (RemoteException e) {
//            Log.e(TAG, "Failed to set ROTATION_0:" + e);
//        }
//    }
//
//
//    private void rotation90() {
//        try {
//            IWindowManager wm = WindowManagerGlobal.getWindowManagerService();
//            wm.freezeRotation(Surface.ROTATION_90);
//        } catch (RemoteException e) {
//            Log.e(TAG, "Failed to set ROTATION_90:" + e);
//        }
//    }
//
//    private void rotation180() {
//        try {
//            IWindowManager wm = WindowManagerGlobal.getWindowManagerService();
//            wm.freezeRotation(Surface.ROTATION_180);
//        } catch (RemoteException e) {
//            Log.e(TAG, "Failed to set ROTATION_180:" + e);
//        }
//    }
//
//    private void rotation270() {
//        try {
//            IWindowManager wm = WindowManagerGlobal.getWindowManagerService();
//            wm.freezeRotation(Surface.ROTATION_270);
//        } catch (RemoteException e) {
//            Log.e(TAG, "Failed to set ROTATION_270:" + e);
//        }
//    }

}
