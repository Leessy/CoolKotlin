package com.leessy.KotlinExtension

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

fun <T> Observable<T>.observeOnMain(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.observeOnCpt(): Observable<T> {
    return this.observeOn(Schedulers.computation())
}

fun <T> Observable<T>.observeOnIo(): Observable<T> {
    return this.observeOn(Schedulers.io())
}

fun <T> Observable<T>.observeOnNew(): Observable<T> {
    return this.observeOn(Schedulers.newThread())
}

fun <T> Observable<T>.subscribeOnMain(): Observable<T> {
    return this.subscribeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.subscribeOnCpt(): Observable<T> {
    return this.subscribeOn(Schedulers.computation())
}

fun <T> Observable<T>.subscribeOnIo(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
}

fun <T> Observable<T>.subscribeOnNew(): Observable<T> {
    return this.subscribeOn(Schedulers.newThread())
}

/**************************************************************************************/

/**在子线程执行一些逻辑*/
fun <T> T.runIo(action: suspend (T) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        action(this@runIo)
    }
}

/**在main线程执行一些逻辑*/
fun <T> T.runMain(action: suspend (T) -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        action(this@runMain)
    }
}

/**************************************************************************************/


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
        .observeOnIo().map { actionIO(this@RxIo_Main) }
    when (this) {
        is RxAppCompatActivity -> o.compose(this.bindToLifecycle()).observeOnMain()
        is RxAppCompatDialogFragment -> o.compose(this.bindToLifecycle()).observeOnMain()
        is RxFragment -> o.compose(this.bindToLifecycle()).observeOnMain()
        is RxFragmentActivity -> o.compose(this.bindToLifecycle()).observeOnMain()
        is RxDialogFragment -> o.compose(this.bindToLifecycle()).observeOnMain()
        else -> o.observeOnMain()
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
        .observeOnIo()
        .map {
            actionIO(this@RxIo_Main)
        }
        .observeOnMain()
        .subscribe {
            actionMain(it)
        }
}

