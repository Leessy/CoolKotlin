package com.leessy.xCrash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.leessy.coolkotlin.R
import xcrash.TombstoneManager
import xcrash.ICrashCallback
import xcrash.TombstoneParser
import org.json.JSONObject
import android.util.Log
import java.io.File
import java.io.FileWriter
import xcrash.XCrash
import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.LogUtils


class XcrashTestActivity : AppCompatActivity() {
    val TAG = "XcrashTestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xcrash_test)

        initXcrash()


    }

    fun testNativeCrashInMainThread_onClick(view: View) {
        XCrash.testNativeCrash(false)
    }

    fun testNativeCrashInAnotherJavaThread_onClick(view: View) {
        Thread(Runnable { XCrash.testNativeCrash(false) }, "java_thread_with_a_very_long_name").start()
    }

    fun testNativeCrashInAnotherNativeThread_onClick(view: View) {
        XCrash.testNativeCrash(true)
    }

    fun testNativeCrashInAnotherProcess_onClick(view: View) {
        startService(Intent(this, MyService::class.java).putExtra("type", "native"))
    }

    fun testJavaCrashInMainThread_onClick(view: View) {
        XCrash.testJavaCrash(false)
    }

    fun testJavaCrashInAnotherThread_onClick(view: View) {
        XCrash.testJavaCrash(true)
    }

    fun testJavaCrashInAnotherProcess_onClick(view: View) {
        startService(Intent(this, MyService::class.java).putExtra("type", "java"))
    }

    //初始化 scrash
    private fun initXcrash() {

        // The callback when App process crashed.
        val callback = ICrashCallback { logPath, emergency ->
            if (emergency != null) {
                debug(logPath, emergency)

                // Disk is exhausted, send crash report immediately.
                sendThenDeleteCrashLog(logPath, emergency)
            } else {
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
            }
        }


        // Initialize xCrash.
        XCrash.init(
            this, XCrash.InitParameters()
                .setAppVersion("1.2.3-beta456-patch789")
                .setJavaRethrow(true)
                .setJavaLogCountMax(10)
                .setJavaDumpAllThreadsWhiteList(arrayOf("^main$", "^Binder:.*", ".*Finalizer.*"))
                .setJavaDumpAllThreadsCountMax(10)
                .setJavaCallback(callback)
                .setNativeRethrow(true)
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
            for (file in TombstoneManager.getAllTombstones()) {
                sendThenDeleteCrashLog(file.absolutePath, "")
            }
        }).start()

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

        LogUtils.d(TAG, "log ---------  : $crashReport")

        TombstoneManager.deleteTombstone(logPath)
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
}
