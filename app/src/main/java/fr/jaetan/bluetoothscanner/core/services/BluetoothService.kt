package fr.jaetan.bluetoothscanner.core.services

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BluetoothService(context: Context) {
    private val bluetoothManager = getSystemService(context, BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

    fun scan(activity: Activity, coroutineScope: CoroutineScope) {
        if (
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
            || !MainService.state.isSearchEnabled
        ) { return }

        MainService.state.isSearchEnabled = false
        MainService.state.devices.clear()

        coroutineScope.launch {
            bluetoothAdapter?.startDiscovery()
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                addDevice(device)
            }

            delay(5000)
            bluetoothAdapter?.cancelDiscovery()
            MainService.state.isSearchEnabled = true
        }
    }

    companion object {

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when(intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        addDevice(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE))
                    }
                }
            }
        }

        private fun addDevice(device: BluetoothDevice?) {
            if (!MainService.state.devices.contains(device) && device != null) {
                MainService.state.devices.add(device)
            }
        }
    }
}