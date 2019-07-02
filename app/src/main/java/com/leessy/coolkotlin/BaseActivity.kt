package com.leessy.coolkotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

open class BaseActivity : RxAppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy ???")
        cancel()
    }

//    suspend fun showIOData() {
//        val deferred = async(Dispatchers.IO) {
//            // impl
//            println(Thread.currentThread().name)
//            return@async 1
//        }
//        withContext(Dispatchers.Main) {
//            val data = deferred.await()
//            // Show data in UI
//        }
//    }
}
