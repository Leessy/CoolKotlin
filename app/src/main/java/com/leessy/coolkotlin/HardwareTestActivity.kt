package com.leessy.coolkotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.leessy.CardTest.JidaCardTestActivity
import com.leessy.CardTest.YxCardTestActivity
import com.leessy.KotlinExtension.onClick
import kotlinx.android.synthetic.main.activity_hardware_test.*

class HardwareTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hardware_test)
        yxcard.onClick {
            startActivity(Intent(this, YxCardTestActivity::class.java))
        }
        jidacard.onClick {
            startActivity(Intent(this, JidaCardTestActivity::class.java))
        }
        printer.onClick {
            startActivity(Intent(this, PrinterActivity::class.java))
        }

    }
}