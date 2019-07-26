package com.leessy.KotlinExtension

import android.arch.lifecycle.Lifecycle
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.trello.rxlifecycle2.components.support.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * @author Created by 刘承. on 2019/7/25
 *
 * --深圳市尚美欣辰科技有限公司.
 */

fun <T> Observable<T>.toMain(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.toComputation(): Observable<T> {
    return this.observeOn(Schedulers.computation())
}

fun <T> Observable<T>.toIo(): Observable<T> {
    return this.observeOn(Schedulers.io())
}

fun <T> Observable<T>.toNewThread(): Observable<T> {
    return this.observeOn(Schedulers.newThread())
}

/**在子线程执行一些逻辑*/
fun <T> T.toIo(action: suspend (T) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        action(this@toIo)
    }
}

/**在main线程执行一些逻辑*/
fun <T> T.toMain(action: suspend (T) -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        action(this@toMain)
    }
}


/**Observable 绑定 Compose */
fun <T> Observable<T>.toCompose(action: (Observable<T>) -> Observable<T>): Observable<T> {
    return action(this)
}


/**在io线程执行一些逻辑，并在main线程处理结果*/
fun <T, R> T.ioToMain(
    actionIO: suspend (T) -> R,
    actionMain: suspend (R) -> Unit
) {
    GlobalScope.launch(Dispatchers.Main) {
        actionMain(withContext(Dispatchers.Default) { actionIO(this@ioToMain) })
    }
}

/**使用RxJava实现*** 在io线程执行一些逻辑，并在main线程处理结果,如果使用的 Lifecycle类型自动绑定 bindToLifecycle*/
fun <T, R> T.RxIo_Main(
    actionIO: (T) -> R,
    actionMain: (R) -> Unit
) {
    val o = Observable.just(0)
        .toIo().map { actionIO(this@RxIo_Main) }
    when (this) {
        is RxAppCompatActivity -> o.compose(this.bindToLifecycle()).toMain()
        is RxAppCompatDialogFragment -> o.compose(this.bindToLifecycle()).toMain()
        is RxFragment -> o.compose(this.bindToLifecycle()).toMain()
        is RxFragmentActivity -> o.compose(this.bindToLifecycle()).toMain()
        is RxDialogFragment -> o.compose(this.bindToLifecycle()).toMain()
        else -> o.toMain()
    }.subscribe({
        actionMain(it)
    }, {})
}

/**使用RxJava实现*** 在io线程执行一些逻辑，并在main线程处理结果*/
fun <T, R> T.RxIo_Main(
    compose: (Observable<T>) -> Observable<T> = { it },
    actionIO: (T) -> R,
    actionMain: (R) -> Unit
) {
    compose(Observable.just(this))
        .toIo()
        .map {
            actionIO(this@RxIo_Main)
        }
        .toMain()
        .subscribe {
            actionMain(it)
        }
}

