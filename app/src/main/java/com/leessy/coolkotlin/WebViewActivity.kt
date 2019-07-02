package com.leessy.coolkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val url = intent.getStringExtra("url")

        webview.run {
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            loadUrl("http://39.108.67.218:8082" + url)
            //缩放操作
            settings.setSupportZoom(true)//支持缩放，默认为true。是下面那个的前提。
            settings.setBuiltInZoomControls(true)//设置内置的缩放控件。若为false，则该WebView不可缩放
//            settings.setDisplayZoomControls(false)//隐藏原生的缩放控件
        }
    }
}
