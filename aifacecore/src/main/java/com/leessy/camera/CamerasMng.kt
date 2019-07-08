package com.leessy.camera

import android.content.Context
import android.hardware.usb.UsbDevice
import android.util.Log
import com.leessy.aifacecore.R
import com.serenegiant.usb.DeviceFilter
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
                Log.d("----", "onConnect ${controlBlock?.busNum}    ${controlBlock?.devNum}   $createNew")
                if (!createNew) return
                device?.let {
                    GlobalScope.launch {
                        connectDevice(device, controlBlock!!)
                    }
                }
            }

            override fun onDisconnect(device: UsbDevice?, controlBlock: USBMonitor.UsbControlBlock?) {
                Log.d("----", "onDisconnect ${device?.deviceName}")
                GlobalScope.launch {
                    disconnect(device!!, controlBlock!!)
                }
            }

            override fun onCancel(device: UsbDevice?) {
                Log.d("----", "on onCancel ${device?.deviceName}")
            }

            override fun onDettach(device: UsbDevice?) {
                Log.d("----", "on onDettach ${device?.deviceName}")
                GlobalScope.launch {
                    dettach(device!!)
                }
            }
        }).apply {
            val filters = DeviceFilter.getDeviceFilters(c, R.xml.device_filter)
            setDeviceFilter(filters)
            register()
        }
    }

    private fun connectDevice(device: UsbDevice, controlBlock: USBMonitor.UsbControlBlock) {
        cameraList.add(Camera(controlBlock).apply {
            initmarking()
        })
    }

    //关闭设备
    private fun disconnect(device: UsbDevice, controlBlock: USBMonitor.UsbControlBlock) {
//        var c: Camera? = null
//        cameraList.forEach {
//            if (it.controlBlock == controlBlock) {
//                c = it
//            }
//        }
//        c?.stopPreview()//释放相机资源
        Log.d("----", "disconnect ---设备个数   ${cameraList.size}")
    }

    //移除设备
    private fun dettach(device: UsbDevice) {
        var c: Camera? = null
        cameraList.forEach {
            if (it.controlBlock.device == device) {
                c = it
            }
        }
        c?.destroyCamera()//释放相机资源
        cameraList.remove(c)
        Log.d("----", "dettach --- 设备个数   ${cameraList.size}")
    }

    fun destroy() {
        Log.d("----", "--  ** destroy")
        mUSBMonitor?.destroy()
    }

}