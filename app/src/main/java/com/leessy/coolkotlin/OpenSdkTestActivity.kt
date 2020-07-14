package com.leessy.coolkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.KotlinExtension.onClick
import com.leessy.webviewjs.WebViewJsActivity
import com.leessy.xCrash.XcrashTestActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_open_sdk_test.*

class OpenSdkTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_sdk_test)
        webviewjs.onClick { startActivity(Intent(this, WebViewJsActivity::class.java)) }
        xcrash.onClick { startActivity(Intent(this, XcrashTestActivity::class.java)) }
        coroutine.onClick { startActivity(Intent(this, CoroutineActivity::class.java)) }
        MySQLBt.onClick { startActivity(Intent(this, MySQLActivity::class.java)) }
    }
}