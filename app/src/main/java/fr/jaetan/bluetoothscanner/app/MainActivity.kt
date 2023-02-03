package fr.jaetan.bluetoothscanner.app

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import fr.jaetan.bluetoothscanner.app.ui.theme.BluetoothScannerTheme
import fr.jaetan.bluetoothscanner.core.services.BluetoothService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckBluetoothPermission()

            BluetoothScannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) { HomeScreen(this) }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(BluetoothService.receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(BluetoothService.receiver)
    }

    @Composable
    private fun CheckBluetoothPermission() {
        val context = LocalContext.current
        val activity = this
        val coroutineScope = rememberCoroutineScope()
        val permissions = mutableListOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            BluetoothService.init(context)
            BluetoothService.instance.scan(activity, coroutineScope)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.addAll(listOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
            ))
        }

        SideEffect {
            launcher.launch(permissions.toTypedArray())
        }
    }
}