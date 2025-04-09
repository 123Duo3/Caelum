package ink.duo3.caelum.ui.componets

import android.Manifest
import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.CancellationSignal
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import ink.duo3.caelum.viewmodel.MainViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import java.util.concurrent.Executors

private const val TAG = "LocationProvider"

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationProvider(viewModel: MainViewModel = koinViewModel()) {
    val permissions =
        listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

    val permission = rememberMultiplePermissionsState(permissions)
    val context = LocalContext.current

    DisposableEffect(permission.revokedPermissions) {
        val allRevoked = permission.revokedPermissions.size == permissions.size

        if (allRevoked) {
            permission.launchMultiplePermissionRequest()
            viewModel.updateLocationStatus(MainViewModel.GpsStatus.PermissionDenied)
        } else {
            val fineRevoked = permission.revokedPermissions.any {
                it.permission == Manifest.permission.ACCESS_FINE_LOCATION
            }

            val provider = if (fineRevoked) {
                LocationManager.NETWORK_PROVIDER
            } else {
                LocationManager.GPS_PROVIDER
            }

            val locationManager = context.getSystemService<LocationManager>()!!
            val executor = Executors.newSingleThreadExecutor()
            val signal = CancellationSignal()
            viewModel.updateLocationStatus(MainViewModel.GpsStatus.Pending)

            Log.d(TAG, "Get current location")
            LocationManagerCompat.getCurrentLocation(
                locationManager, provider, signal, executor
            ) { location ->
                if (location != null) {
                    Log.d(TAG, "Location: $location")
                    viewModel.updateLocationStatus(MainViewModel.GpsStatus.Ok)
                    viewModel.updateLocation(location.latitude, location.longitude, provider)
                } else {
                    Log.w(TAG, "Failed to get location")
                    viewModel.updateLocationStatus(MainViewModel.GpsStatus.Error)
                }
            }
        }

        onDispose {
        }
    }

    if (permission.shouldShowRationale) {
        PermissionRationaleDialog("请授予位置权限", "需要获取当前位置的天气", onConfirmClick = {
            permission.launchMultiplePermissionRequest()
        }, onDismissClick = {
            viewModel.updateLocationStatus(MainViewModel.GpsStatus.PermissionDenied)
        })
    }
}

@Composable
private fun PermissionRationaleDialog(
    title: String,
    rationale: String,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissClick, title = {
        Text(text = title)
    }, text = {
        Text(text = rationale)
    }, confirmButton = {
        TextButton(onClick = onConfirmClick) {
            Text("授权")
        }
    }, dismissButton = {
        TextButton(onClick = onDismissClick) {
            Text("拒绝")
        }
    })
}

@Composable
private fun PermissionDeniedDialog(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val showDialog = remember { mutableStateOf(true) }
    if (showDialog.value) AlertDialog(
        title = {
            Text("请开启定位权限")
        }, text = {
            Text("为了获取您当前所在位置的天气，请您开启定位权限")
        }, onDismissRequest = {

        }, confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text("前往设置")
            }
        }, dismissButton = {
            TextButton(onClick = {
                showDialog.value = false
            }) {
                Text("取消")
            }
        }
    )
}