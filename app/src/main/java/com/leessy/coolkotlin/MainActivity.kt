package com.leessy.coolkotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.xCrash.XcrashTestActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RxView.clicks(functionList1).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, ScrollingActivity::class.java)) }

        RxView.clicks(fullscreenactivity).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, FullscreenActivity::class.java)) }

        RxView.clicks(basec).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, BaseActivity::class.java)) }

        RxView.clicks(ListDemo).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, ListDemoActivity::class.java)) }

        RxView.clicks(MySQLBt).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, MySQLActivity::class.java)) }


        RxView.clicks(coroutine).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, CoroutineActivity::class.java)) }

        RxView.clicks(xcrash).observeOn(AndroidSchedulers.mainThread())
            .subscribe { startActivity(Intent(this, XcrashTestActivity::class.java)) }


        startActivity(Intent(this, XcrashTestActivity::class.java))
    }
}
