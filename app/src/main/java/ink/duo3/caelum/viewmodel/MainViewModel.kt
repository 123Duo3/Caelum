package ink.duo3.caelum.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ink.duo3.caelum.api.CaelumApiClient
import ink.duo3.caelum.api.module.WeatherModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

private const val TAG = "MainViewModel"

class MainViewModel(
    private val apiClient: CaelumApiClient
): ViewModel() {
    val locationStatus = mutableStateOf(GpsStatus.Idle)
    val location = mutableStateOf<Location?>(null)
    val weather = mutableStateOf<WeatherModule.NowResp?>(null)

    enum class GpsStatus {
        Idle, Pending, Ok, Error,
        PermissionDenied
    }

    data class Location(
        val name: String,
        val cityId: String,
        val latitude: Double,
        val longitude: Double,
        val lastUpdateTime: Instant
    )

    fun updateLocationStatus(status: GpsStatus) {
        locationStatus.value = status
    }

    fun updateLocation(latitude: Double, longitude: Double, provider: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "updateLocation: lat $latitude, lon: $longitude")
            try {
                val resp = apiClient.weatherModule().getCityByLocation(latitude, longitude)
                if (resp.ok) {
                    val city = resp.data.cities.first()
                    Log.d(TAG, "updateLocation: city: ${city.name}")
                    location.value = Location(
                        name = city.name,
                        cityId = city.cityId,
                        latitude = city.latitude.toDouble(),
                        longitude = city.longitude.toDouble(),
                        lastUpdateTime = kotlinx.datetime.Clock.System.now()
                    )
                    getWeather()
                } else {
                    updateLocationStatus(GpsStatus.Error)
                }
            } catch (e: Exception) {
                updateLocationStatus(GpsStatus.Error)
            }
        }
    }

    private suspend fun getWeather() {
        try {
            location.value?.let {
                Log.d(TAG, "getWeather: getting weather")
                val resp = apiClient.weatherModule().now(it.cityId)
                if (resp.ok) {
                    Log.d(TAG, "getWeather: get weather successfully")
                    weather.value = resp.data
                }
            }
        } catch (e: Exception) {
            // TODO: Show error message
            Log.e(TAG, "getWeather: Failed to get weather", e)
        }
    }
}