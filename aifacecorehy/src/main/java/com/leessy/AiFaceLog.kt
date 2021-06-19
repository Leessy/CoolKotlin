package com.leessy

import android.util.Log
import com.leessy.aifacecore.BuildConfig

/**
 *
 * @author Created by 刘承. on 2019/9/12
 *
 * --深圳市尚美欣辰科技有限公司.
 */

var showLog = BuildConfig.DEBUG//log开关

const val TAG = "AiFaceCore ---"


internal fun Any.logd(tag: String, s: String) {
    if (!showLog) return
    Log.d(TAG + tag, s)
}

internal fun Any.logd(s: String) {
    if (!showLog) return
    Log.d(TAG, s)
}
