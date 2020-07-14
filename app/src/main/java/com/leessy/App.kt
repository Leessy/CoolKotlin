package com.leessy

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Vibrator
import android.support.multidex.MultiDex
import android.util.Log
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ShellUtils
import com.didichuxing.doraemonkit.DoraemonKit
import com.leessy.coolkotlin.MainActivity
import com.leessy.service.LocationService
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import xcrash.ICrashCallback
import xcrash.TombstoneManager
import xcrash.TombstoneParser
import xcrash.XCrash
import java.io.DataOutputStream
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.charset.Charset

/**
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 */
class App : Application() {
    private val TAG = javaClass.name

    companion object {
        lateinit var app: App
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        app = this
//        initXcrash()
        DoraemonKit.install(this)
    }

    //初始化 scrash
    private fun initXcrash() {
        // The callback when App process crashed.
        val callback = ICrashCallback { logPath, emergency ->
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            android.os.Process.killProcess(android.os.Process.myPid())

            if (emergency != null) {
                Log.d(TAG, "异常了？1111")
                debug(logPath, emergency)

                // Disk is exhausted, send crash report immediately.
                sendThenDeleteCrashLog(logPath, emergency)
            } else {
                Log.d(TAG, "异常了？222")
                // Add some expanded sections. Send crash report at the next time APP startup.
                // OK
                TombstoneManager.appendSection(logPath, "expanded_key_1", "expanded_content")
                TombstoneManager.appendSection(
                    logPath,
                    "expanded_key_2",
                    "expanded_content_row_1\nexpanded_content_row_2"
                )
                // Invalid. (Do NOT include multiple consecutive newline characters ("\n\n") in the content string.)
                // TombstoneManager.appendSection(logPath, "expanded_key_3", "expanded_content_row_1\n\nexpanded_content_row_2");
                debug(logPath, null)

                Log.d(TAG, "重启程序！！！" + Thread.currentThread().name)
                Thread.sleep(1200)
//                restartApp(this)
            }
        }


        // Initialize xCrash.
        XCrash.init(
            this, XCrash.InitParameters()
                .setAppVersion("1.2.3-beta456-patch789")
                .setJavaRethrow(false)
                .setJavaLogCountMax(10)
                .setJavaDumpAllThreadsWhiteList(arrayOf("^main$", "^Binder:.*", ".*Finalizer.*"))
                .setJavaDumpAllThreadsCountMax(10)
                .setJavaCallback(callback)
                .setNativeRethrow(false)
                .setNativeLogCountMax(10)
                .setNativeDumpAllThreadsWhiteList(
                    arrayOf(
                        "^xcrash\\.sample$",
                        "^Signal Catcher$",
                        "^Jit thread pool$",
                        ".*(R|r)ender.*",
                        ".*Chrome.*"
                    )
                )
                .setNativeDumpAllThreadsCountMax(10)
                .setNativeCallback(callback)
                .setPlaceholderCountMax(3)
                .setPlaceholderSizeKb(512)
                .setLogFileMaintainDelayMs(1000)
        )

        // Send all pending crash log files.
        Thread(Runnable {
            Log.d(TAG, "读上次记录数量   ${TombstoneManager.getAllTombstones().size}")
//            for (file in TombstoneManager.getAllTombstones()) {
//                sendThenDeleteCrashLog(file.absolutePath, "")
//            }
        }).start()

    }

    private fun debug(logPath: String?, emergency: String?) {
        Log.d(TAG, "logPath: " + (logPath ?: "(null)") + ", emergency: " + (emergency ?: "(null)"))

        // Parse and save the crash info to a JSON file for debugging.
        var writer: FileWriter? = null
        try {
            val debug = File(applicationContext.filesDir.toString() + "/tombstones/debug.json")
            debug.createNewFile()
            writer = FileWriter(debug, false)
            writer!!.write(JSONObject(TombstoneParser.parse(logPath, emergency)).toString())
        } catch (e: Exception) {
            Log.d(TAG, "debug failed", e)
        } finally {
            if (writer != null) {
                try {
                    writer!!.close()
                } catch (ignored: Exception) {
                }

            }
        }
    }

    //处理异常上传和保持文件
    private fun sendThenDeleteCrashLog(logPath: String, emergency: String) {

        var map = TombstoneParser.parse(logPath, emergency)
        var crashReport = JSONObject(map).toString()

        // Parse
        //Map<String, String> map = TombstoneParser.parse(logPath, emergency);
        //String crashReport = new JSONObject(map).toString();

        // Send the crash report to server-side.
        // ......

        // If the server-side receives successfully, delete the log file.
        //
        // Note: When you use the placeholder file feature,
        //       please always use this method to delete tombstone files.
        //

        Log.d(TAG, "log ---------  : $crashReport")

//        TombstoneManager.deleteTombstone(logPath)
    }


    /**
     * 重新启动--包括服务
     *
     *
     * 程序连续断开 重启机器
     *
     *
     * com.onfacemind.aiface902/com.onfacemind.aiface902.Activity.Home.StartActivity
     */
    fun restartApp(context: Context) {
        val pkg = context.packageName
        val atv = getActivities(context, pkg)
        Schedulers.io().scheduleDirect {
            val command1 = "am force-stop $pkg\n" //force-stop;
            val command2 = "am start -n $pkg/$atv\n" //am start -n 包名/包名.第一个Activity的名称";
//
            println("-------cmd  $command1  ")
            println("-------cmd    $command2")
//            var ret = ShellUtils.execCmd(arrayListOf<String>(command1, command2), false)
//            println("-------cmd  $ret")


            var dataOutputStream: DataOutputStream? = null
            try {
                // 申请su权限
                val process = Runtime.getRuntime().exec("sh")
//                val process = Runtime.getRuntime().exec("su")
                dataOutputStream = DataOutputStream(process.outputStream)
                // 执行pm install命令
                val command1 = "am force-stop $pkg\n" //force-stop;
                val command2 = "am start -n $pkg/$atv\n" //am start -n 包名/包名.第一个Activity的名称";
                //                    String command1 = "am force-stop com.onfacemind.aiface902\n"; //force-stop;
                //                    String command2 = "am start -n com.onfacemind.aiface902/com.onfacemind.aiface902.Activity.Home.StartActivity\n"; //am start -n 包名/包名.第一个Activity的名称";
                dataOutputStream.write(command1.toByteArray(Charset.forName("utf-8")))
                dataOutputStream.write(command2.toByteArray(Charset.forName("utf-8")))
                dataOutputStream.flush()
                dataOutputStream.writeBytes("exit\n")
                dataOutputStream.flush()
                process.waitFor()
            } catch (e: Exception) {
            } finally {
                try {
                    dataOutputStream?.close()
                } catch (e: IOException) {
                }

            }
        }
    }


    /**
     * 获取APP的启动入口
     *
     * @param context
     * @param packageName
     * @return
     */
    private fun getActivities(context: Context, packageName: String): String {
        val localIntent = Intent("android.intent.action.MAIN", null)
        localIntent.addCategory("android.intent.category.LAUNCHER")
        val appList = context.packageManager.queryIntentActivities(localIntent, 0)
        for (i in appList.indices) {
            val resolveInfo = appList[i]
            val packageStr = resolveInfo.activityInfo.packageName
            if (packageStr == packageName) {
                //这个就是你想要的那个Activity
                //                Log.d(TAG, "packageName: ===" + packageName);
                //                Log.d(TAG, "getActivities: ===" + resolveInfo.activityInfo.name);
                return resolveInfo.activityInfo.name
            }
        }
        return ""
    }
}
