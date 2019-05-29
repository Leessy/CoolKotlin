package com.leessy.coolkotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        toolbar_layout.title = "ScrollingDemo"


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar.contentInsetStartWithNavigation
        toolbar.setNavigationOnClickListener {
            Snackbar.make(it, "返回键", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}
