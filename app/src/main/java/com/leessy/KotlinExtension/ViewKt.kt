package com.leessy.KotlinExtension

import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch


/**
 * View的onclick扩展函数   封装协程处理
 */
fun View.onClick(action: suspend (View) -> Unit) {
    setOnClickListener {
        GlobalScope.launch(Dispatchers.Main) {
            println("View 1 $it")
            action(it)
            println("View 2 $it")
        }
    }
}


/**
 * 多次点击最多只有一个执行
 */
fun View.onClickMostOne(action: suspend (v: View) -> Unit) {
    // launch one actor to handle all events on this node
    val eventActor = GlobalScope.actor<View>(Dispatchers.Main) {
        println("View 2 $channel")
        for (event in channel) action(event) // pass event to action
        println("View 3 $channel")

    }

    // install a listener to activate this actor
    setOnClickListener {
        eventActor.offer(it)
    }
    // install a listener to offer events to this actor
//    onMouseClicked = EventHandler { event ->
//        eventActor.offer(event)
//    }
}

fun View.onClick3(action: suspend (View) -> Unit) {
    // launch one actor to handle all events on this node
    val eventActor = GlobalScope.actor<View>(Dispatchers.Main, capacity = Channel.CONFLATED) {
        // <--- Changed here
        for (event in channel) action(event) // pass event to action
    }
    // install a listener to offer events to this actor
//    onMouseClicked = EventHandler { event ->
//        eventActor.offer(event)
//    }
}