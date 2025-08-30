package com.champouillon.clement.ebrosensors

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import java.util.UUID

private var CLIENT_CHARACTERISTIC_CONFIG_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

class BluetoothService {

    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var mainLooper: Looper = Looper.getMainLooper()
    private var context: Context? = null
    private var tempMeasurementDescriptorUUID: UUID = UUID.fromString("2e4dd79f-0e10-49c8-9f81-c885d0f33b53")
    private var bluetoothGatt: BluetoothGatt? = null

    private var onTemperatureRead: (Float, String) -> Unit = { temperature, probeType ->
        Log.d("BluetoothService", "Temperature: $temperature with probe type: $probeType")
    }

    var onDeviceConnected: (() -> Unit)? = null
    var onDeviceDisconnected: (() -> Unit)? = null

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
    fun disconnect() {
        Log.d("BluetoothService", "Disconnecting from device...")
        this.stopScan()
        bluetoothGatt?.disconnect()
        Log.d("BluetoothService", "Disconnected from device")
    }

    fun init(context: Context) {
        this.context = context
        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager!!.adapter
        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            throw Exception("Bluetooth is not enabled")
        }
        return
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            checkDeviceCompatibility(result)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun scanForDevices(timeout: Long, callback: (Float, String) -> Unit) {
        onTemperatureRead = callback
        Log.d("BluetoothService", "Scanning for devices...")
        bluetoothAdapter!!.bluetoothLeScanner.startScan(leScanCallback)
        Log.d("BluetoothService", "Scan started")
        Handler(mainLooper).postDelayed({
            bluetoothAdapter!!.bluetoothLeScanner.stopScan(leScanCallback)
            Log.d("BluetoothService", "Scan stopped")
        }, timeout)

    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun stopScan() {
        bluetoothAdapter!!.bluetoothLeScanner.stopScan(leScanCallback)
        Log.d("BluetoothService", "Scan stopped")
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
    fun checkDeviceCompatibility(scanResult: ScanResult) {
        if (scanResult.device.name == "TLC750") {
            Log.d("BluetoothService", "Device is compatible, connecting...")
            stopScan()
            val gattCallback = object : BluetoothGattCallback() {
                override fun onCharacteristicChanged(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic,
                    value: ByteArray
                ) {
                    Log.d("BluetoothService", "Characteristic changed: ${characteristic.uuid}")
                    if (characteristic.uuid == tempMeasurementDescriptorUUID) {
                        val temperature = parseTemperature(value)
                        onTemperatureRead(temperature.first, temperature.second)
                        Log.d("BluetoothService", "Temperature: ${temperature.first} with probe type: ${temperature.second}")
                    }
                    super.onCharacteristicChanged(gatt, characteristic, value)
                }

                @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
                override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                    super.onConnectionStateChange(gatt, status, newState)
                    if (newState == BluetoothGatt.STATE_CONNECTED) {
                        onDeviceConnected?.invoke()
                        bluetoothGatt = gatt
                        Log.d("BluetoothService", "Connected to device")
                        gatt.discoverServices()
                    } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                        onDeviceDisconnected?.invoke()
                        bluetoothGatt?.close()
                        bluetoothGatt = null
                        Log.d("BluetoothService", "Disconnected from device")
                    }
                }
                @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
                override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                    super.onServicesDiscovered(gatt, status)
                    Log.d("BluetoothService", "Services discovered")
                    gatt.services.forEach { service ->
                        val serviceUUID = service.uuid
                        Log.d("BluetoothService", "Service UUID: $serviceUUID")
                        service.characteristics.forEach { characteristic ->
                            val characteristicUUID = characteristic.uuid
                            Log.d("BluetoothService", "Characteristic UUID: $characteristicUUID")
                            subscribeToGattNotifications(gatt, characteristic)
                            characteristic.descriptors.forEach { descriptor ->
                                val descriptorUUID = descriptor.uuid
                                Log.d("BluetoothService", "Descriptor UUID: $descriptorUUID")
                            }
                        }
                    }
                }

            }
            scanResult.device.connectGatt(this.context, false, gattCallback)
        } else {
            Log.d("BluetoothService", "Device is not compatible")
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun subscribeToGattNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        if ((characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
            gatt.setCharacteristicNotification(characteristic, true)
            val descriptor: BluetoothGattDescriptor? = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
            descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
            Log.d("BluetoothService", "Subscribed to notifications for service ${characteristic.service.uuid} and characteristic ${characteristic.uuid}")
        } else {
            Log.d("BluetoothService", "Characteristic ${characteristic.uuid} does not support notifications")
        }
    }

    private fun parseTemperature(data: ByteArray): Pair<Float, String> {
        val probeType = if ((data[13].toInt() and 0xFF) == 0x01) "infrared" else "penetration"
        // Temperature is 2 bytes at positions 14-15 (little-endian)
        val tempLowByte = data[14].toInt() and 0xFF  // 0x0D
        val tempHighByte = data[15].toInt() and 0xFF // 0x01
        val tempCombined = (tempHighByte shl 8) + tempLowByte // 269
        val temperature = tempCombined / 10.0f // 26.9°C
        return Pair(temperature, probeType)
    }
}