package com.leessy.xCrash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.leessy.coolkotlin.R
import xcrash.XCrash


class XcrashTestActivity : AppCompatActivity() {
    val TAG = "XcrashTestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xcrash_test)
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

    fun test(view: View) {
        Log.d("-----", " android.os.Process.myPid()  =" + android.os.Process.myPid())
        android.os.Process.killProcess(android.os.Process.myPid())
    }


}
