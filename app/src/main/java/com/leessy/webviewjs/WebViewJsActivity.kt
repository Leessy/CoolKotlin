package com.leessy.webviewjs

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.*
import com.leessy.coolkotlin.R
import kotlinx.android.synthetic.main.activity_web_view_js.*


class WebViewJsActivity : AppCompatActivity() {
    private val TAG = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hookwebview.hookWebView()//系统权限不可以使用webview处理
        setContentView(R.layout.activity_web_view_js)
        webview.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
        }

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webview.addJavascriptInterface(JsObject(), "Android");//AndroidtoJS类对象映射到js的test对象
//        webview.addJavascriptInterface(JsObject(), "test2");//AndroidtoJS类对象映射到js的test对象
        // 加载JS代码
        // 格式规定为:file:///android_asset/文件名.html
//        webview.loadUrl("https://leessy.com");
//        webview.loadUrl("http://192.168.1.2:60338/#/");
        webview.loadUrl("file:///android_asset/index2.html");

        webview.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
//                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        //设置响应js 的Alert()函数
        webview.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {
                val b: AlertDialog.Builder = AlertDialog.Builder(this@WebViewJsActivity)
                b.setTitle("Alert")
                b.setMessage(message)
                b.setPositiveButton(
                    "完成",
                    DialogInterface.OnClickListener { dialog, which -> result!!.confirm() })
                b.setCancelable(false)
                b.create().show()
                return true
//                return super.onJsAlert(view, url, message, result)
            }
        }
    }

    inner class JsObject() {
        @JavascriptInterface
        fun test(s: String) {
            Log.d(TAG, "js 调用Android方法！！！  test:  " + s)
        }

        @JavascriptInterface
        fun test2(s: String, s2: String): String {
            Log.d(TAG, "js 调用Android方法！！！  test2  参数1=$s  参数2=$s2")
            return "调用成功了"
        }
    }

    fun callJS(view: View) {
        // 注意调用的JS方法名要对应上
        // 调用javascript的callJS()方法
        webview.loadUrl("javascript:callJS()");
        val request =
            "{'type':'CHALLENGE_OTP', 'mode':'ONLINE', 'challengeOtp':'" + "123123132" + "'}"
        webview.evaluateJavascript(
            "javascript:callJS(\"$request\")",
            object : ValueCallback<String> {
                override fun onReceiveValue(p0: String?) {

                }
            });
//        webview.evaluateJavascript("javascript:callJS($request)", object : ValueCallback<String> {
//            override fun onReceiveValue(p0: String?) {
//
//            }
//        });
    }
}
