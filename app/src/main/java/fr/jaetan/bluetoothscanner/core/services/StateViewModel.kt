package fr.jaetan.bluetoothscanner.core.services

import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class StateViewModel: ViewModel() {
    val devices = mutableStateListOf<BluetoothDevice>()
    var isSearchEnabled by mutableStateOf(true)
}