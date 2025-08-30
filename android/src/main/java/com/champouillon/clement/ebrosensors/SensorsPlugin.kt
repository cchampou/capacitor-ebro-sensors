package com.champouillon.clement.ebrosensors

import android.Manifest
import android.util.Log
import androidx.annotation.RequiresPermission
import com.getcapacitor.JSObject
import com.getcapacitor.PermissionState
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback

@CapacitorPlugin(
    name = "Sensors",
    permissions = [
        Permission(
            alias = "bluetooth-scan",
            strings = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION]
        )
    ]
)
class SensorsPlugin : Plugin() {

    private val bluetoothService: BluetoothService = BluetoothService()

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    @PluginMethod
    fun scan(call: PluginCall) {
        Log.d("BluetoothService", "Plugin call scan")
        try {
            bluetoothService.init(context)
            if (getPermissionState("bluetooth-scan") != PermissionState.GRANTED) {
                requestPermissionForAlias("bluetooth-scan", call, "launchScan")
            } else {
                launchScan(call)
            }
        } catch (exception: Exception) {
            call.reject(exception.message)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    @PermissionCallback
    fun launchScan(call: PluginCall) {
        Log.d("BluetoothService", "Launching scan...")
        if (getPermissionState("bluetooth-scan") == PermissionState.GRANTED) {
            val callback = { temperature: Float, probeType: String ->
                val result = JSObject()
                result.put("value", temperature)
                result.put("input", probeType)
                notifyListeners("temperature", result)
            }
            bluetoothService.scanForDevices(10000, callback)
            call.resolve()
        } else {
            call.reject("Permission for scanning bluetooth devices not granted")
        }
    }
}
