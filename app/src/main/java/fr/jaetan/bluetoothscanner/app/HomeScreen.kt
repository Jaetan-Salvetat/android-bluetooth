package fr.jaetan.bluetoothscanner.app

import android.annotation.SuppressLint
import android.app.Activity
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fr.jaetan.bluetoothscanner.core.services.BluetoothService
import fr.jaetan.bluetoothscanner.core.services.MainService

@RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(activity: Activity) {
    Scaffold(
        content =  { DevicesList() },
        floatingActionButton = { Fab(activity) }
    )
}

@RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
@Composable
private fun DevicesList() {
    LazyColumn(Modifier.padding(10.dp)) {
        items(MainService.state.devices) {
            if (it.name != null && it.address != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {}
                        .padding(8.dp)
                ) {
                    Text(it.name)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "(${it.address})",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline)
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun Fab(activity: Activity) {
    val coroutineScope = rememberCoroutineScope()

    ElevatedButton(
        onClick = { BluetoothService.instance.scan(activity, coroutineScope) },
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.size(60.dp),
        contentPadding = PaddingValues(0.dp),
        enabled = MainService.state.isSearchEnabled
    ) {
        if (MainService.state.isSearchEnabled) {
            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
        } else {
            CircularProgressIndicator(Modifier.size(24.dp))
        }
    }

    /*FloatingActionButton(
        onClick = { BluetoothService.instance.scan(activity, coroutineScope) },
    ) {
        if (MainService.state.isSearchEnabled) {
            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
        } else {
            CircularProgressIndicator(Modifier.size(24.dp))
        }
    }*/
}