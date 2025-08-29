package com.champouillon.clement.ebrosensors

import android.bluetooth.BluetoothManager
import android.content.Context
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "Sensors")
class SensorsPlugin : Plugin() {
    @PluginMethod
    fun scan(call: PluginCall) {
        val bluetoothManager: BluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            call.reject("Bluetooth is not available")
            return
        }
        call.resolve()
    }
}
