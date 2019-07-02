package com.leessy

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

/**
 * @author Created by 刘承. on 2019/7/2
 * business@onfacemind.com
 */
class app : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}
