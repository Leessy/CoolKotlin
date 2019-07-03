package com.leessy.coolkotlin

import android.os.Bundle
import com.leessy.KotlinExtension.onClick
import com.leessy.KotlinExtension.onClickMostOne
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.coroutines.*

class CoroutineActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)
        var jobs = setup("")

        text.onClickMostOne { showIOData() }

//        text.text = runBlocking {
//            async {
//                println("测试 runBlocking1 " + Thread.currentThread().name)
//                delay(3 * 1000)
//                " 132123132"
//            }.await()
//        }
//        println("测试 runBlocking2 " + Thread.currentThread().name)


        var j = getData()
        showtest2()

        var s = 0
        bt.onClick {
            text.text = "123${s++}"
        }

//        Observable.timer(2, TimeUnit.SECONDS).subscribe {
//            j.cancel()
//        }
    }

    fun getData() = launch {

        //        var v1 = async {
//            println("开始线程1  " + Thread.currentThread().name)
//            delay(3000)
//            "11111"
//        }
//
//        var v2 = async {
//            println("开始线程2  " + Thread.currentThread().name)
//            delay(5000)
//            "222222"
//        }
//        println("开始线程 3 " + v1.await() + v2.await())

//        showIOData()
    }

    val range1: IntRange = 0..2000//全闭区间
    val range2: IntRange = 0..2000//全闭区间


    fun showtest2() = launch {
        println("-----1   ${Thread.currentThread().name}")

        var s0 = GlobalScope.async(Dispatchers.IO) {
            range1.forEach {
                //                delay(100)
                println("0-----$it      ${Thread.currentThread().name}")
            }
        }
        var s1 = async {
            range1.forEach {
                //                delay(500)
                println("1-----$it      ${Thread.currentThread().name}")
            }
        }
        println("-----2   ${Thread.currentThread().name}")

        var s2 = async(Dispatchers.Main) {
            range2.forEach {
                //                delay(500)
                println("2-----$it      ${Thread.currentThread().name}")
            }
        }

//        println("-----3   ${Thread.currentThread().name}")
//        println("-----4  $s1  $s2   ${Thread.currentThread().name}")
//        println("-----5  ${s1.await()}  ${s2.await()}   ${Thread.currentThread().name}")


    }

    suspend fun showIOData() {
        val deferred = async(Dispatchers.Default) {
            // impl
            println("开始线程  " + Thread.currentThread().name)
            delay(5 * 1000)
//            delay(1 * 1000)
            return@async 10010
        }
        var s = async(Dispatchers.IO) {
            //            deferred.getCompleted()
            println("结果线程 " + Thread.currentThread().name)
            delay(4 * 1000)

            val data = deferred.await()
            println("结果线程2 " + Thread.currentThread().name)
            // Show data in UI
            println("结果 $data")
            data
        }
        println("结果???? 1111")
        delay(3 * 1000)
        text.append("----${s.await()}")
        println("结果 ???" + Thread.currentThread().name)

    }


    @Synchronized
    fun setup(hello: String): Job = GlobalScope.launch(Dispatchers.Main) {
        // launch coroutine in the main thread
        for (i in 100 downTo 1) { // countdown from 10 to 1
            text.text = "Countdown $i ..." // update text
            println("Countdown $i...${Thread.currentThread().name}")
            delay(1000) // wait half a second
        }
        text.text = "Done!"
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

