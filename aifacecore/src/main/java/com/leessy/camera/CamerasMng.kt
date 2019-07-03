package com.leessy.camera

import android.content.Context
import android.hardware.usb.UsbDevice
import android.util.Log
import com.serenegiant.usb.USBMonitor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *
 * @author Created by 刘承. on 2019/7/3
 * business@onfacemind.com
 */
object CamerasMng {
    internal var defaultPreviewWidth = 640
    internal var defaultPreviewHeight = 480

    private var isInit: Boolean = false
    private var mUSBMonitor: USBMonitor? = null
    var cameraList: ArrayList<Camera> = arrayListOf()


    fun initCameras(c: Context, w: Int = 640, h: Int = 480) {
        if (isInit) return
        isInit = true
        defaultPreviewWidth = w
        defaultPreviewHeight = h
        mUSBMonitor = USBMonitor(c, object : USBMonitor.OnDeviceConnectListener {
            override fun onAttach(device: UsbDevice?) {
                device?.let {
                    Log.d("----", "on Attach ${device}")
                    if (it.deviceClass == 239 && it.deviceSubclass == 2) {
                        mUSBMonitor?.requestPermission(device)
                    }
                }
            }

            override fun onConnect(device: UsbDevice?, controlBlock: USBMonitor.UsbControlBlock?, createNew: Boolean) {
                Log.d("----", "on onConnect ${controlBlock?.busNum}    ${controlBlock?.devNum}   $createNew")
                if (!createNew) return
                device?.let {
                    GlobalScope.launch {
                        connectDevice(device, controlBlock!!)
                    }
                }
            }

            override fun onDisconnect(device: UsbDevice?, p1: USBMonitor.UsbControlBlock?) {
                Log.d("----", "on onDisconnect ${device?.deviceName}")
                GlobalScope.launch {
                    disconnect(device!!, p1!!)
                }
            }

            override fun onCancel(device: UsbDevice?) {
                Log.d("----", "on onCancel ${device?.deviceName}")
            }

            override fun onDettach(device: UsbDevice?) {
                Log.d("----", "on onDettach ${device?.deviceName}")
//                closeCamera(device)
            }
        }).apply {
            register()
        }
    }

    private fun connectDevice(device: UsbDevice, controlBlock: USBMonitor.UsbControlBlock) {
        cameraList.add(Camera(controlBlock).apply {
            devName = device.deviceName
        })
    }

    private fun disconnect(device: UsbDevice, controlBlock: USBMonitor.UsbControlBlock) {
        var c: Camera? = null
        cameraList.forEach {
            if (it.controlBlock == controlBlock) {
                c = it
            }
        }
        c?.destroyCamera()//释放相机资源
        cameraList.remove(c)
        Log.d("----", "disconnect  ${cameraList.size}")
    }

}