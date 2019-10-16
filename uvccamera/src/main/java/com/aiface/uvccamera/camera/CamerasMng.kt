package com.aiface.uvccamera.camera

import android.content.Context
import android.hardware.usb.UsbDevice
import android.util.Log
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
    internal var usingDfSize: Boolean = false
    private var mUSBMonitor: USBMonitor? = null
    var cameraList: ArrayList<Camera> = arrayListOf()

    //PID:293为602的4G模块
    private var pidFilter = arrayListOf(293)//相机pid

    /**
     * 新增pid过滤设备
     */
    fun addPIDfilters(vararg pid: Int) {
        pidFilter.addAll(pid.toList())
    }

    /**
     *初始化相机管理工具,可选传入相机默认分辨率
     */
    fun initCameras(c: Context, w: Int = 640, h: Int = 480, usingDfSize: Boolean = false) {
        if (isInit) return
        isInit = true
        this.usingDfSize = usingDfSize
        defaultPreviewWidth = w
        defaultPreviewHeight = h
        destroy()//防止重复初始化
        mUSBMonitor = USBMonitor(c, object : USBMonitor.OnDeviceConnectListener {
            override fun onAttach(device: UsbDevice?) {
                device?.let {
                    if (pidFilter.contains(it.productId)) return//过滤指定pid设备
                    if (it.deviceClass == 239 && it.deviceSubclass == 2) {
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
//                Log.d("CamerasMng", "on Disconnect ${device?.deviceName}")
                GlobalScope.launch {
                    disconnect(device!!, controlBlock!!)
                }
            }

            override fun onCancel(device: UsbDevice?) {
//                Log.d("CamerasMng", "on onCancel ${device?.deviceName}")
            }

            override fun onDettach(device: UsbDevice?) {
//                Log.d("CamerasMng", "on onDettach ${device?.deviceName}")
                GlobalScope.launch {
                    dettach(device!!)
                }
            }
        }).apply {
            //            val filters = DeviceFilter.getDeviceFilters(c, R.xml.device_filter)
//            setDeviceFilter(filters)
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
        Log.d("CamerasMng", "disconnect ---设备个数   ${cameraList.size}")
        c?.stopPreview()//释放相机资源
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
//        Log.d("CamerasMng", "dettach --- 设备个数   ${cameraList.size}")
    }


    //关闭所有camera
    fun destroyAllCamera() {
        cameraList.forEach {
            it.destroyCamera()
        }
    }

    //关闭相机管理器
    fun destroy() {
        cameraList.forEach {
            it.destroyCamera()
        }
        mUSBMonitor?.destroy()
        mUSBMonitor = null
        cameraList.clear()
        isInit = false
    }

}