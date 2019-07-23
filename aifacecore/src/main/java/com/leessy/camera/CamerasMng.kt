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

    private val USB4G_PID = 293//相机pid<由于接入系统的类型是uvc设备,默认过滤次设备>
    private var isInit: Boolean = false
    private var mUSBMonitor: USBMonitor? = null
    var cameraList: ArrayList<Camera> = arrayListOf()


    /**
     *初始化相机管理工具,可选传入相机默认分辨率
     */
    fun initCameras(c: Context, w: Int = 640, h: Int = 480) {
        if (isInit) return
        isInit = true
        defaultPreviewWidth = w
        defaultPreviewHeight = h
        mUSBMonitor?.let { it.destroy() }
        mUSBMonitor = USBMonitor(c, object : USBMonitor.OnDeviceConnectListener {
            override fun onAttach(device: UsbDevice?) {
                device?.let {
                    if (it.deviceClass == 239 && it.deviceSubclass == 2 && USB4G_PID != it.productId) {
                        Log.d("CamerasMng", "on Attach ${device.deviceName}  ${device.vendorId}  ${device.productId}")
                        mUSBMonitor?.requestPermission(device)
                    }
                }
            }

            override fun onConnect(device: UsbDevice?, controlBlock: USBMonitor.UsbControlBlock?, createNew: Boolean) {
                Log.d("CamerasMng", "onConnect ${controlBlock?.busNum}    ${controlBlock?.devNum}   $createNew")
                if (!createNew) return
                device?.let {
                    GlobalScope.launch {
                        connectDevice(device, controlBlock!!)
                    }
                }
            }

            override fun onDisconnect(device: UsbDevice?, controlBlock: USBMonitor.UsbControlBlock?) {
                Log.d("CamerasMng", "on Disconnect ${device?.deviceName}")
                GlobalScope.launch {
                    disconnect(device!!, controlBlock!!)
                }
            }

            override fun onCancel(device: UsbDevice?) {
                Log.d("CamerasMng", "on onCancel ${device?.deviceName}")
            }

            override fun onDettach(device: UsbDevice?) {
                Log.d("CamerasMng", "on onDettach ${device?.deviceName}")
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


    //USB设备接入处理
    private fun connectDevice(device: UsbDevice, controlBlock: USBMonitor.UsbControlBlock) {
        cameraList.add(Camera(controlBlock).apply {
            initmarking()
        })
    }

    //关闭设备连接
    private fun disconnect(device: UsbDevice, controlBlock: USBMonitor.UsbControlBlock) {
        var c: Camera? = null
        cameraList.forEach {
            if (it.controlBlock == controlBlock) {
                c = it
            }
        }
        c?.stopPreview()//释放相机资源
        Log.d("CamerasMng", "disconnect ---设备个数   ${cameraList.size}")
    }

    //USB设备移除处理
    private fun dettach(device: UsbDevice) {
        var c: Camera? = null
        cameraList.forEach {
            if (it.controlBlock.device == device) {
                c = it
            }
        }
        c?.destroyCamera()//释放相机资源
        cameraList.remove(c)
        Log.d("CamerasMng", "dettach --- 设备个数   ${cameraList.size}")
    }


    //关闭所有camera
    fun destroyAllCamera() {
        cameraList.forEach {
            it.destroyCamera()
        }
    }

    //关闭相机管理器
    fun destroy() {
        mUSBMonitor?.destroy()
        cameraList.forEach {
            it.destroyCamera()
        }
        mUSBMonitor?.destroy()
        mUSBMonitor = null
        cameraList.clear()
        isInit = false
    }

}