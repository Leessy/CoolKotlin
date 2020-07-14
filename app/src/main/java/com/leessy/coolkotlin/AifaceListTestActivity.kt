package com.leessy.coolkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.KotlinExtension.onClick
import com.leessy.OCR.OcrActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_aiface_list_test.*
import java.util.concurrent.TimeUnit

class AifaceListTestActivity : RxAppCompatActivity() {
    var isAuto = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aiface_list_test)

        RxView.clicks(AiFaceCoreTest).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isAuto = true
                startActivity(Intent(this, AiFaceCoreTestActivity::class.java))
            }

        ocr.onClick {
            startActivity(Intent(this, OcrActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAuto) {
            Observable.timer(2, TimeUnit.SECONDS, Schedulers.io())
                .compose(this.bindToLifecycle())
                .subscribe {
                    startActivity(Intent(this, AiFaceCoreTestActivity::class.java))
                }
        }
    }
}