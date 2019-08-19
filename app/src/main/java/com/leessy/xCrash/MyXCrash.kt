package com.leessy.xCrash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import com.leessy.coolkotlin.BuildConfig
import com.leessy.coolkotlin.MainActivity
import xcrash.ICrashCallback
import xcrash.XCrash

/**
 * @Author: 陈博
 * @create time:  2019/7/30  9:53
 * --深圳市尚美欣辰科技有限公司.
 */
@SuppressLint("StaticFieldLeak")
object MyXCrash {
    lateinit var context: Context

    val callback = ICrashCallback { logPath, emergency ->
        Log.i("cbo", "logPath: $logPath")
        Log.i("cbo", "emergency: $emergency")
//        if (!TextUtils.isEmpty(logPath)) {
        //                Schedulers.io().scheduleDirect(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        HttpManage.getHttpManage().postLogcat(logPath);
        //                    }
        //                });
//        }

        // rebootApplication();
        reStartApp()
    }

    fun XCrash(context: Context) {
        this.context = context
        try {
            val params = XCrash.InitParameters().apply {
                setAppVersion(BuildConfig.VERSION_NAME)
                setJavaCallback(callback)
                setJavaRethrow(false)
                setJavaDumpAllThreadsWhiteList(arrayOf("^main$", "^Binder:.*", ".*Finalizer.*"))
                setJavaLogCountMax(5)
                setJavaDumpAllThreadsCountMax(5)
                setNativeRethrow(false)
                setNativeDumpAllThreadsWhiteList(
                    arrayOf(
                        "^xcrash\\.sample$",
                        "^Signal Catcher$",
                        "^Jit thread pool$",
                        ".*(R|r)ender.*",
                        ".*Chrome.*"
                    )
                )
                setNativeDumpAllThreadsCountMax(5)
                setNativeCallback(callback)
                setPlaceholderCountMax(3)
                setPlaceholderSizeKb(512)
                setLogFileMaintainDelayMs(1000)
            }
            XCrash.init(context, params)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    private fun reStartApp() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context!!.startActivity(intent)
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    @Throws(InterruptedException::class)
    private fun rebootApplication() {

        //        Thread.sleep(500);
        //        DataOutputStream dataOutputStream = null;
        //        BufferedReader errorStream = null;
        //        try {
        //            String cmd1 = " am force-stop com.onfacemind.f602\n";
        //            String cmd2 = " am start -n com.onfacemind.f602/com.onfacemind.f602.activity.InitActivity\n";
        //
        //            // 申请su权限
        //            Process process = Runtime.getRuntime().exec(new String[]{cmd1,cmd2});
        //            dataOutputStream = new DataOutputStream(process.getOutputStream());
        //            dataOutputStream.write(cmd1.getBytes(Charset.forName("utf-8")));
        //            dataOutputStream.write(cmd2.getBytes(Charset.forName("utf-8")));
        //            dataOutputStream.flush();
        //            dataOutputStream.writeBytes("exit\n");
        //            dataOutputStream.flush();
        //            process.waitFor();
        //
        //        } catch (Exception e) {
        //            Log.e("TAG", e.getMessage(), e);
        //        } finally {
        //            try {
        //                if (dataOutputStream != null) {
        //                    dataOutputStream.close();
        //                }
        //                if (errorStream != null) {
        //                    errorStream.close();
        //                }
        //            } catch (IOException e) {
        //                Log.e("TAG", e.getMessage(), e);
        //            }
        //        }
    }
}
