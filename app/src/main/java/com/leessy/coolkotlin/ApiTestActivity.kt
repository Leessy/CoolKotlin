package com.leessy.coolkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.ofm1000test.ofm1000ServerTest
import io.reactivex.android.schedulers.AndroidSchedulers

class ApiTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_test)
    }

    fun ofm1000test(view: View) {
        startActivity(Intent(this, ofm1000ServerTest::class.java))
    }
}