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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService
import androidx.core.location.LocationManagerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import ink.duo3.caelum.viewmodel.MainViewModel
import org.koin.compose.koinInject
import java.util.concurrent.Executors

private const val TAG = "LocationProvider"

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationProvider(viewModel: MainViewModel = koinInject()) {
    val permission = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(permission.allPermissionsGranted)  {
        if (!permission.allPermissionsGranted) {
            permission.launchMultiplePermissionRequest()
        }
    }

    val context = LocalContext.current
    val locationManager = remember { context.getSystemService<LocationManager>()!! }

    DisposableEffect(permission.allPermissionsGranted) {
        val executor = Executors.newSingleThreadExecutor()
        val signal = CancellationSignal()
        viewModel.updateGpsStatus(MainViewModel.GpsStatus.Pending)

        // Just use network location provider
        LocationManagerCompat.getCurrentLocation(locationManager, LocationManager.NETWORK_PROVIDER, signal, executor) {
            Log.d(TAG, "Location: $it")
            viewModel.updateGpsStatus(MainViewModel.GpsStatus.Ok)
            viewModel.updateLocation(it.latitude, it.longitude)
        }

        onDispose {
        }
    }
    /*if (permission.shouldShowRationale) {
        PermissionRationaleDialog("请授予位置权限", "需要获取当前位置的天气", {
            if (it) {
                permission.launchMultiplePermissionRequest()
            }
        }
        )
    }*/
}

@Composable
private fun PermissionRationaleDialog(
    title: String,
    rationale: String,
    onRationaleReply: (proceed: Boolean) -> Unit
) {
    AlertDialog(onDismissRequest = { onRationaleReply(false) }, title = {
        Text(text = title)
    }, text = {
        Text(text = rationale)
    }, confirmButton = {
        TextButton(onClick = {
            onRationaleReply(true)
        }) {
            Text("Continue")
        }
    }, dismissButton = {
        TextButton(onClick = {
            onRationaleReply(false)
        }) {
            Text("Dismiss")
        }
    })
}