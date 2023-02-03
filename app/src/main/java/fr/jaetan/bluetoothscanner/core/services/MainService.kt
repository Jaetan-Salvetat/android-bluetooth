package fr.jaetan.bluetoothscanner.core.services

import android.content.Context

class MainService() {
    companion object {
        val state = StateViewModel()
        lateinit var bluetoothService: BluetoothService

        fun init(context: Context) {
            bluetoothService = BluetoothService(context)
        }
    }
}