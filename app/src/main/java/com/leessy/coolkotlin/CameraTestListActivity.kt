package com.leessy.coolkotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.leessy.F501ATest.F501ATestActivity
import com.leessy.KotlinExtension.onClick
import com.leessy.mediarecord.MediaRecordActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_camera_test_list.*
import kotlinx.android.synthetic.main.activity_main.*

//相机测试相关demo
class CameraTestListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_test_list)
        RxView.clicks(F501Test).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, F501ATestActivity::class.java))
            }
        RxView.clicks(presentationCamera).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(Intent(this, PresentationCameraActivity::class.java))
            }
        records.onClick {
            startActivity(Intent(this, MediaRecordActivity::class.java))
        }
    }
}