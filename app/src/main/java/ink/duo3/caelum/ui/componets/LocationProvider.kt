package ink.duo3.caelum.ui.componets

import android.Manifest
import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.CancellationSignal
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import ink.duo3.caelum.viewmodel.MainViewModel.LocationType
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
        val signal = CancellationSignal()
        val executor = Executors.newCachedThreadPool()

        if (allRevoked) {
            permission.launchMultiplePermissionRequest()
            viewModel.updateLocationStatus(
                MainViewModel.GpsStatus.PermissionDenied,
                LocationType.GPS
            )
            viewModel.updateLocationStatus(
                MainViewModel.GpsStatus.PermissionDenied,
                LocationType.Network
            )
        } else {
            val fineRevoked = permission.revokedPermissions.any {
                it.permission == Manifest.permission.ACCESS_FINE_LOCATION
            }

            val locationManager = context.getSystemService<LocationManager>()!!

            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            Log.d(TAG, "LocationProvider: last known = $lastKnownLocation")

            if (lastKnownLocation != null) {
                viewModel.updateLocationStatus(MainViewModel.GpsStatus.Ok, LocationType.LastKnow)
                viewModel.updateLocationAndRefresh(
                    lastKnownLocation.latitude,
                    lastKnownLocation.longitude,
                    LocationType.LastKnow
                )
            }

            viewModel.updateLocationStatus(MainViewModel.GpsStatus.Pending, LocationType.Network)
            Log.d(TAG, "Getting location from network")
            LocationManagerCompat.getCurrentLocation(
                locationManager, LocationManager.NETWORK_PROVIDER, signal, executor
            ) { location ->
                if (location != null) {
                    Log.d(TAG, "Network location: $location")
                    viewModel.updateLocationStatus(MainViewModel.GpsStatus.Ok, LocationType.Network)
                    viewModel.updateLocationAndRefresh(
                        location.latitude,
                        location.longitude,
                        LocationType.Network
                    )
                } else {
                    viewModel.updateLocationStatus(
                        MainViewModel.GpsStatus.Error,
                        LocationType.Network
                    )
                }
            }

            if (!fineRevoked) {
                viewModel.updateLocationStatus(MainViewModel.GpsStatus.Pending, LocationType.GPS)
                Log.d(TAG, "Getting location from gps")

                LocationManagerCompat.getCurrentLocation(
                    locationManager, LocationManager.GPS_PROVIDER, signal, executor
                ) { location ->
                    Log.d(TAG, "Gps location: $location")
                    if (location != null) {
                        viewModel.updateLocationStatus(MainViewModel.GpsStatus.Ok, LocationType.GPS)
                        viewModel.updateLocationAndRefresh(
                            location.latitude,
                            location.longitude,
                            LocationType.GPS
                        )
                    } else {
                        viewModel.updateLocationStatus(
                            MainViewModel.GpsStatus.Error,
                            LocationType.GPS
                        )
                    }
                }
            }

            viewModel.updateLocationStatus(MainViewModel.GpsStatus.Pending, LocationType.Passive)
            Log.d(TAG, "Getting location from passive")

            LocationManagerCompat.getCurrentLocation(
                locationManager, LocationManager.PASSIVE_PROVIDER, signal, executor
            ) { location ->
                Log.d(TAG, "Passive location: $location")
                if (location != null) {
                    viewModel.updateLocationStatus(MainViewModel.GpsStatus.Ok, LocationType.Passive)
                    viewModel.updateLocationAndRefresh(
                        location.latitude,
                        location.longitude,
                        LocationType.Passive
                    )
                } else {
                    viewModel.updateLocationStatus(
                        MainViewModel.GpsStatus.Error,
                        LocationType.Passive
                    )
                }
            }
        }

        onDispose {
            signal.cancel()
            executor.shutdown()
        }
    }

    if (permission.shouldShowRationale && !viewModel.neverShowPermissionDialog.value) {
        PermissionRationaleDialog("请授予位置权限", "需要获取当前位置的天气", onConfirmClick = {
            permission.launchMultiplePermissionRequest()
        }, onDismissClick = {
            viewModel.updateLocationStatus(
                MainViewModel.GpsStatus.PermissionDenied,
                LocationType.GPS
            )
            viewModel.updateLocationStatus(
                MainViewModel.GpsStatus.PermissionDenied,
                LocationType.Network
            )
            viewModel.neverShowPermissionDialog.value = true
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
        Button(onClick = onConfirmClick) {
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