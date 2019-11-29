package com.aiface.jidacard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pboc.TransLib
import vpos.apipackage.IDCard
import vpos.apipackage.Picc
import vpos.apipackage.Sys

class MainCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        Sys.initEnv(applicationContext)
        Sys.Lib_SetComPath("/dev/ttyS4")
        TransLib.TransLibSetOnCardholderAction(TransLib.cardholderAction)

        var ret = Picc.Lib_PiccOpen()

        Log.d("****1", "    1  ret $ret")
        ret = IDCard.Lib_IDCardOpen()
        Log.d("****1", "    3 ret $ret")
    }


    override fun onDestroy() {
        super.onDestroy()
        Picc.Lib_PiccClose()
        IDCard.Lib_IDCardClose()
    }
}
