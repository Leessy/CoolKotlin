package com.aiface.uvccamera.camera

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.hardware.usb.UsbDevice
import android.util.Log
import com.serenegiant.usb.USBMonitor
import io.reactivex.schedulers.Schedulers


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
    private var classFilter =
        mutableMapOf(Pair(239, 2), Pair(39, 0), Pair(2, 0), Pair(6, -1), Pair(14, 9))

    private var nonePermissionDeviceList = mutableListOf<UsbDevice>()
    private var isSystemApp = false

    fun getNonePermissionDevices() = nonePermissionDeviceList.iterator()

    /**
     * 新增pid过滤设备
     */
    fun addPIDfilters(vararg pid: Int) {
        pidFilter.addAll(pid.toList())
    }

    fun addClassFilter(pair: Pair<Int, Int>) {
        classFilter[pair.first] = pair.second
    }

    private fun isCameraDevice(deviceClass: Int, deviceSubclass: Int): Boolean {
        return classFilter.containsKey(deviceClass) && classFilter[deviceClass] == deviceSubclass
    }

    //判断是否为系统应用
    private fun isSystemApp(pkgName: String, context: Context): Boolean {
        var isSystemApp = false
        var pi: PackageInfo? = null
        try {
            val pm: PackageManager = context.getPackageManager()
            pi = pm.getPackageInfo(pkgName, 0)
        } catch (t: Throwable) {
            Log.w("666", t.message, t)
        }
        if (pi != null) {
            val isSysApp =
                pi.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1
            val isSysUpd =
                pi.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 1

            val isSystemUid = pi.applicationInfo.uid == 1000

            isSystemApp = isSysApp || isSysUpd || isSystemUid
        }
        return isSystemApp
    }

    //申请权限
    fun requestPermission(device: UsbDevice) {
        mUSBMonitor?.requestPermission(device)
    }

    /**
     *初始化相机管理工具,可选传入相机默认分辨率,并确认是否启用自动设置size默认关
     */
    @Synchronized
    fun initCameras(c: Context, w: Int = 640, h: Int = 480, usingDfSize: Boolean = false) {
        isSystemApp = isSystemApp(c.packageName, c)
        if (isInit) return
        isInit = true
        this.usingDfSize = usingDfSize
        defaultPreviewWidth = w
        defaultPreviewHeight = h
        destroy()//防止重复初始化
        mUSBMonitor = USBMonitor(c, object : USBMonitor.OnDeviceConnectListener {
            override fun onAttach(device: UsbDevice?) {
                Log.d(
                    "CamerasMng",
                    "on Attach AnyDev ${device?.deviceName}  ${device?.vendorId}  ${device?.productId} ${device?.deviceClass} ${device?.deviceSubclass}"
                )
                device?.let {
                    if (pidFilter.contains(it.productId)) return//过滤指定pid设备
                    if (isCameraDevice(it.deviceClass, it.deviceSubclass)) {
                        Log.d(
                            "CamerasMng",
                            "on Attach CameraType ${device.deviceName}  ${device.vendorId}  ${device.productId}"
                        )
                        Log.d(
                            "CamerasMng",
                            "on Attach CameraType${mUSBMonitor?.hasPermission(device)!!}"
                        )

                        if (isSystemApp && mUSBMonitor?.hasPermission(device)!!) {
                            mUSBMonitor?.requestPermission(device)
                        } else {
                            nonePermissionDeviceList.add(device)//无权限设备不进行连接操作
                        }
                    }
                }
            }

            override fun onConnect(
                device: UsbDevice?,
                controlBlock: USBMonitor.UsbControlBlock?,
                createNew: Boolean
            ) {
                Log.d(
                    "CamerasMng",
                    "onConnect ${controlBlock?.busNum}    ${controlBlock?.devNum}   $createNew"
                )
                nonePermissionDeviceList.remove(device)//连接成功
                if (!createNew) return
                device?.let {
                    Schedulers.io().scheduleDirect { connectDevice(device, controlBlock!!) }
                }
            }

            override fun onDisconnect(
                device: UsbDevice?,
                controlBlock: USBMonitor.UsbControlBlock?
            ) {
//                Log.d("CamerasMng", "on Disconnect ${device?.deviceName}")
                Schedulers.io().scheduleDirect {
                    disconnect(device!!, controlBlock!!)
                }
            }

            override fun onCancel(device: UsbDevice?) {
//                Log.d("CamerasMng", "on onCancel ${device?.deviceName}")
            }

            override fun onDettach(device: UsbDevice?) {
//                Log.d("CamerasMng", "on onDettach ${device?.deviceName}")
                Schedulers.io().scheduleDirect {
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
    @Synchronized
    private fun connectDevice(device: UsbDevice, controlBlock: USBMonitor.UsbControlBlock) {
        cameraList.add(Camera(controlBlock).apply {
            initmarking()
        })
    }

    //关闭设备连接
    @Synchronized
    private fun disconnect(device: UsbDevice, controlBlock: USBMonitor.UsbControlBlock) {
        var c: Camera? = null
        cameraList.forEach {
            if (it.controlBlock == controlBlock) {
                c = it
            }
        }
        Log.d("CamerasMng", "disconnect ---设备个数   ${cameraList.size}")
//        c?.stopPreview()//释放相机资源
        c?.destroyCamera()//释放相机资源

    }

    //USB设备移除处理
    @Synchronized
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
    @Synchronized
    fun destroyAllCamera() {
        cameraList.forEach {
            it.destroyCamera()
        }
    }

    //关闭相机管理器
    @Synchronized
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
