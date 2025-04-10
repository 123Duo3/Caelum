package ink.duo3.caelum.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ink.duo3.caelum.api.CaelumApiClient
import ink.duo3.caelum.api.module.WeatherModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

private const val TAG = "MainViewModel"

class MainViewModel(
    private val apiClient: CaelumApiClient
): ViewModel() {
    val neverShowPermissionDialog = mutableStateOf(false)

    val locationStatus = mutableStateOf(LocationStatus.new())
    val currentLocation = mutableStateOf<Location?>(null)

    val cityStatus = mutableStateOf(CityStatus.Idle)

    val weatherStatus = mutableStateOf(WeatherStatus.Init)
    val weather = mutableStateOf<WeatherModule.NowResp?>(null)

    data class LocationStatus(
        val gpsStatus: GpsStatus,
        val netStatus: GpsStatus,
        val cacheStatus: GpsStatus,
        val passiveStatus: GpsStatus,
        val lastKnownStatus: GpsStatus
    ) {
        companion object {
            fun new(): LocationStatus {
                return LocationStatus(
                    gpsStatus = GpsStatus.Idle,
                    netStatus = GpsStatus.Idle,
                    cacheStatus = GpsStatus.Idle,
                    passiveStatus = GpsStatus.Idle,
                    lastKnownStatus = GpsStatus.Idle,
                )
            }
        }

        fun anyOk(): Boolean {
            return gpsStatus == GpsStatus.Ok ||
                    netStatus == GpsStatus.Ok ||
                    cacheStatus == GpsStatus.Ok ||
                    passiveStatus == GpsStatus.Ok ||
                    lastKnownStatus == GpsStatus.Ok
        }

        fun anyError(): Boolean {
            return gpsStatus == GpsStatus.Error ||
                    netStatus == GpsStatus.Error ||
                    cacheStatus == GpsStatus.Error ||
                    passiveStatus == GpsStatus.Error ||
                    lastKnownStatus == GpsStatus.Error
        }

        fun allError(): Boolean {
            return gpsStatus == GpsStatus.Error &&
                    netStatus == GpsStatus.Error &&
                    cacheStatus == GpsStatus.Error &&
                    passiveStatus == GpsStatus.Error &&
                    lastKnownStatus == GpsStatus.Error
        }
    }

    enum class GpsStatus {
        Idle, Pending, Ok, Error,
        PermissionDenied
    }

    enum class CityStatus {
        Idle, Pending, Ok, Error
    }

    enum class WeatherStatus {
        Init, Requesting, Ok, Error
    }

    data class Location(
        val name: String,
        val cityId: String,
        val latitude: Double,
        val longitude: Double,
        val lastUpdateTime: Instant,
        val type: LocationType
    )

    enum class LocationType {
        Cache, LastKnow, Passive, Network, GPS
    }

    fun updateLocationStatus(status: GpsStatus, locationType: LocationType) {
        when (locationType) {
            LocationType.GPS -> {
                locationStatus.value = locationStatus.value.copy(gpsStatus = status)
            }
            LocationType.Network -> {
                locationStatus.value = locationStatus.value.copy(netStatus = status)
            }
            LocationType.Passive -> {
                locationStatus.value = locationStatus.value.copy(passiveStatus = status)
            }
            LocationType.LastKnow -> {
                locationStatus.value = locationStatus.value.copy(lastKnownStatus = status)
            }
            LocationType.Cache -> {
                locationStatus.value = locationStatus.value.copy(cacheStatus = status)
            }
        }
    }

    private fun LocationType.betterThan(type: LocationType): Boolean {
        return this.ordinal > type.ordinal
    }

    fun updateLocationAndRefresh(latitude: Double, longitude: Double, type: LocationType) {
        viewModelScope.launch(Dispatchers.IO) {
            currentLocation.value?.let {
                if (!type.betterThan(it.type)) {
                    return@launch
                }
            }

            Log.d(TAG, "updateLocation: lat $latitude, lon: $longitude")
            try {
                cityStatus.value = CityStatus.Pending
                fetchCity(latitude, longitude, type)
                cityStatus.value = CityStatus.Error
            } catch (e: Exception) {
                Log.e(TAG, "updateLocation: Failed to get geo info", e)
                cityStatus.value = CityStatus.Error
            }
            try {
                weatherStatus.value = WeatherStatus.Requesting
                fetchWeather()
                weatherStatus.value = WeatherStatus.Ok
            } catch (e: Exception) {
                Log.e(TAG, "updateLocation: Failed to get weather", e)
                weatherStatus.value = WeatherStatus.Error
            }
        }
    }

    private suspend fun fetchCity(latitude: Double, longitude: Double, type: LocationType) {
        val resp = apiClient.weatherModule().getCityByLocation(latitude, longitude)
        if (resp.ok) {
            val city = resp.data.cities.first()
            Log.d(TAG, "updateLocation: city: ${city.name}")
            currentLocation.value = Location(
                name = city.name,
                cityId = city.cityId,
                latitude = city.latitude.toDouble(),
                longitude = city.longitude.toDouble(),
                lastUpdateTime = Clock.System.now(),
                type = type
            )
        } else {
            error("Error response}")
        }
    }

    private suspend fun fetchWeather() {
        try {
            currentLocation.value?.let {
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