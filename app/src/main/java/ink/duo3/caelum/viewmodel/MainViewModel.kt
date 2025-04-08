package ink.duo3.caelum.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ink.duo3.caelum.api.CaelumApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import java.time.Clock

private const val TAG = "MainViewModel"

class MainViewModel(
    private val apiClient: CaelumApiClient
): ViewModel() {
    val gpsStatus = mutableStateOf(GpsStatus.Idle)
    val location = mutableStateOf<Location?>(null)

    enum class GpsStatus {
        Idle, Pending, Ok, Error
    }

    data class Location(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val lastUpdateTime: Instant
    )

    fun updateGpsStatus(status: GpsStatus) {
        gpsStatus.value = status
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "updateLocation: lat $latitude, lon: $longitude")
            try {
                val resp = apiClient.weatherModule().getCityByLocation(latitude, longitude)
                if (resp.ok) {
                    val city = resp.data.cities.first()
                    Log.d(TAG, "updateLocation: city: ${city.name}")
                    location.value = Location(city.name, city.latitude.toDouble(), city.longitude.toDouble(), kotlinx.datetime.Clock.System.now())
                } else {
                    updateGpsStatus(GpsStatus.Error)
                }
            } catch (e: Exception) {
                updateGpsStatus(GpsStatus.Error)
            }
        }
    }
}