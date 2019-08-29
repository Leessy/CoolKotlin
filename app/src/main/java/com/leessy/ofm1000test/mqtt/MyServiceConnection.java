package com.leessy.ofm1000test.mqtt;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MyServiceConnection implements ServiceConnection {

    private MQTTService mqttService;
    private IGetMessageCallBack IGetMessageCallBack;
    private ConnectStatus connectStatus;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mqttService = ((MQTTService.CustomBinder) iBinder).getService();
        mqttService.setIGetMessageCallBack(IGetMessageCallBack);
        if (connectStatus != null) {
            connectStatus.onConnected();
        }
    }


    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public MQTTService getMqttService() {
        return mqttService;
    }

    public void setConnectStatus(ConnectStatus c) {
        this.connectStatus = c;
    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack) {
        this.IGetMessageCallBack = IGetMessageCallBack;
    }

    interface ConnectStatus {
        void onConnected();
    }
}