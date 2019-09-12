package com.leessy.ofm1000test

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import java.net.NetworkInterface
import java.util.*
import java.lang.reflect.AccessibleObject.setAccessible
import android.bluetooth.BluetoothAdapter
import java.lang.reflect.InvocationTargetException
import android.os.Build
import java.net.SocketException


/**
 *
 * @author Created by 刘承. on 2019/9/11
 *
 * --深圳市尚美欣辰科技有限公司.
 */
object DevUtil {

    /**
     * 获取手机IMEI号
     */
    @SuppressLint("MissingPermission")
    fun getIMEI(context: Context): String? {
        var telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var imei = telephonyManager.getDeviceId()
        return imei
    }


    /**
     * 获取手机MSISDN号
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getIMSI(context: Context): String? {
        var telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var IMSI = telephonyManager.subscriberId
        return IMSI
    }

    /**
     * 获取手机getICCID号
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getICCID(context: Context): String? {
        var telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var ICCID = telephonyManager.simSerialNumber
        return ICCID
    }

    /**
     * 获取手机MSISDN号MSISDN
     */
    @SuppressLint("MissingPermission")
    fun getMSISDN(context: Context): String? {
        var telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var msisdn = telephonyManager.line1Number
        return msisdn
    }

    /**
     * 通过网络接口取
     * 获取wifiMac地址
     *
     * @return
     */
    fun getWIFIMac(context: Context): String? {
        try {
            var all = Collections.list(NetworkInterface.getNetworkInterfaces())
            all.forEach {
                if (it.name.equals("wlan0", ignoreCase = true)) {
                    var macBytes = it.getHardwareAddress();
                    if (macBytes == null) {
                        return null;
                    }

                    var res1 = StringBuilder();
                    macBytes.forEach {
                        res1.append(String.format("%02X:", it))

                    }
                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
    fun getETH0Mac(context: Context): String? {
        try {
            var all = Collections.list(NetworkInterface.getNetworkInterfaces())
            all.forEach {
                if (it.name.equals("eth", ignoreCase = true)) {
                    var macBytes = it.getHardwareAddress()
                    if (macBytes == null) {
                        return null;
                    }

                    var res1 = StringBuilder();
                    macBytes.forEach {
                        res1.append(String.format("%02X:", it))

                    }
                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    /**
     * getUniquePsuedoID
     */
    fun getUniquePsuedoID(): String {
        val m_szDevIDShort =
            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        var serial: String? = null
        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()

            // Go ahead and return the serial for api => 9
            return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (e: Exception) {
            // String needs to be initialized
            serial = "serial" // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
    }


    /**
     * 获取设备MAC 地址 由于 6.0 以后 WifiManager 得到的 MacAddress得到都是 相同的没有意义的内容
     * 所以采用以下方法获取Mac地址
     * @param context
     * @return
     */
    private fun getLocalMac(context: Context): String {
        //        WifiManager wifi = (WifiManager) context
        //                .getSystemService(Context.WIFI_SERVICE);
        //        WifiInfo info = wifi.getConnectionInfo();
        //        return info.getMacAddress();
        var macAddress: String? = null
        val buf = StringBuffer()
        var networkInterface: NetworkInterface? = null
        try {
            networkInterface = NetworkInterface.getByName("eth1")
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0")
            }
            if (networkInterface == null) {
                return ""
            }
            val addr = networkInterface.hardwareAddress


            for (b in addr) {
                buf.append(String.format("%02X:", b))
            }
            if (buf.length > 0) {
                buf.deleteCharAt(buf.length - 1)
            }
            macAddress = buf.toString()
        } catch (e: SocketException) {
            e.printStackTrace()
            return ""
        }
        return macAddress
    }


    /**
     * 蓝牙获取不到
     */
    fun getBluetoothMacAddress(): String {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        var bluetoothMacAddress = ""
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                val mServiceField = bluetoothAdapter.javaClass.getDeclaredField("mService")
                mServiceField.isAccessible = true

                val btManagerService = mServiceField.get(bluetoothAdapter)

                if (btManagerService != null) {
                    bluetoothMacAddress =
                        btManagerService.javaClass.getMethod("getAddress").invoke(btManagerService) as String
                }
            } catch (e: NoSuchFieldException) {

            } catch (e: NoSuchMethodException) {

            } catch (e: IllegalAccessException) {

            } catch (e: InvocationTargetException) {

            }

        } else {
            bluetoothMacAddress = bluetoothAdapter.address
        }
        return bluetoothMacAddress
    }
}