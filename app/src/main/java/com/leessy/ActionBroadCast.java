package com.leessy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ActionBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String s = intent.getStringExtra("msg");
        Log.d("闹钟广播", "-------msg:" + s);
        Toast.makeText(context, "闹钟----msg:" + s, Toast.LENGTH_LONG).show();

//        Intent intent1 = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
//        intent1.putExtra("android.intent.extra.KEY_CONFIRM", false);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent1);

    }
}
