package com.leessy.KotlinExtension

import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 * @author Created by 刘承. on 2019/7/1
 * business@onfacemind.com
 */


/**
 * 更新一个倒计时控件 文字显示demo
 */
fun setup(hello: TextView) {
    GlobalScope.launch(Dispatchers.Main) {
        // launch coroutine in the main thread
        for (i in 10 downTo 1) { // countdown from 10 to 1
            hello.text = "Countdown $i ..." // update text
            delay(500) // wait half a second
        }
        hello.text = "Done!"
    }
}