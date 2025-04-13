package ink.duo3.caelum.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ink.duo3.caelum.api.CaelumApiClient
import ink.duo3.caelum.api.module.WeatherModule
import ink.duo3.caelum.ui.componets.DailyWeatherInfo
import ink.duo3.caelum.ui.componets.MultilayerIcon
import ink.duo3.caelum.ui.componets.WeatherIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn
import java.time.format.TextStyle
import java.util.Locale

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

    val dailyWeatherStatus = mutableStateOf(DailyWeatherStatus.Idle)
    val dailyWeather = mutableStateOf<List<DailyWeatherInfo>>(emptyList())

    val aqi = mutableStateOf<WeatherModule.AqiNowResp?>(null)

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

    enum class DailyWeatherStatus {
        Idle, Pending, Ok, Error
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
                fetchCity(latitude, longitude, type)
            } catch (e: Exception) {
                Log.e(TAG, "updateLocation: Failed to get geo info", e)
                cityStatus.value = CityStatus.Error
                return@launch
            }

            launch {
                try {
                    fetchWeather()
                } catch (e: Exception) {
                    Log.w(TAG, "fetchWeather: failed", e)
                    weatherStatus.value = WeatherStatus.Error
                }
            }

            launch {
                try {
                    fetchDailyWeather()
                } catch (e: Exception) {
                    Log.w(TAG, "fetchDailyWeather: failed", e)
                    dailyWeatherStatus.value = DailyWeatherStatus.Error
                }
            }

            launch {
                try {
                    fetchAirQuality()
                } catch (e: Exception) {
                    Log.w(TAG, "fetchAirQuality: failed", e)
                }
            }
        }
    }

    private suspend fun fetchCity(latitude: Double, longitude: Double, type: LocationType) {
        cityStatus.value = CityStatus.Pending
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
            cityStatus.value = CityStatus.Ok
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

    private suspend fun fetchDailyWeather() {
        currentLocation.value?.let {
            dailyWeatherStatus.value = DailyWeatherStatus.Pending
            val resp = apiClient.weatherModule().get10d(it.cityId)
            if (resp.ok) {
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val data = resp.data.daily.map {
                    val date = LocalDate.parse(it.fxDate)
                    val periodDays = today.periodUntil(date).days

                    val dateText = when(periodDays) { // TODO(i18n)
                        0 -> "今天"
                        1 -> "明天"
                        else -> date.dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
                    }
                    DailyWeatherInfo(
                        date = dateText,
                        icon = iconCodeToWeatherIcon(it.iconDay, true),
                        description = it.textDay,
                        probability = null, // TODO: What is this?
                        tempMin = it.tempMin.toInt(),
                        tempMax = it.tempMax.toInt()
                    )
                }
                dailyWeather.value = data
                dailyWeatherStatus.value = DailyWeatherStatus.Ok
            } else {
                dailyWeatherStatus.value = DailyWeatherStatus.Error
            }
        }
    }

    private suspend fun fetchAirQuality() {
        currentLocation.value?.let {
            val resp = apiClient.weatherModule().getAqiNow(it.latitude, it.longitude)
            if (resp.ok) {
                aqi.value = resp.data
            }
        }
    }
}

/**
 * https://dev.qweather.com/docs/resource/icons/
 */
private val dayMaps = mapOf<String, () -> MultilayerIcon>(
    "100" to { WeatherIcons.Clear },
    "101" to { WeatherIcons.MostlyCloudy },
    "102" to { WeatherIcons.PartlyCloudy} ,
    "103" to { WeatherIcons.MostlyClearWithIntermittentClouds },
    "104" to { WeatherIcons.Overcast },
    "300" to { WeatherIcons.Shower },
    "301" to { WeatherIcons.HeavyShower },
    "303" to { WeatherIcons.SevereThunderstorm },
    "304" to { WeatherIcons.ThunderstormWithHail },
    "305" to { WeatherIcons.LightRain },
    "306" to { WeatherIcons.ModerateRain },
    "307" to { WeatherIcons.HeavyRain },
    "308" to { WeatherIcons.ExtremeRain },
    "309" to { WeatherIcons.Drizzle },
    "310" to { WeatherIcons.TorrentialRain },
    "311" to { WeatherIcons.SevereTorrentialRain },
    "312" to { WeatherIcons.ExtremelySevereTorrentialRain },
    "313" to { WeatherIcons.FreezingRain },
    "314" to { WeatherIcons.LightToModerateRain },
    "315" to { WeatherIcons.ModerateToHeavyRain },
    "316" to { WeatherIcons.HeavyToTorrentialRain },
    "317" to { WeatherIcons.TorrentialToSevereTorrentialRain },
    "318" to { WeatherIcons.SevereTorrentialToExtremelySevereTorrentialRain },
    "399" to { WeatherIcons.Rain },
    "400" to { WeatherIcons.LightSnow },
    "401" to { WeatherIcons.ModerateSnow },
    "402" to { WeatherIcons.HeavySnow },
    "403" to { WeatherIcons.Blizzard },
    "404" to { WeatherIcons.RainAndSnowMix },
    "405" to { WeatherIcons.RainAndSnow },
    "406" to { WeatherIcons.ShowerWithSnow },
    "407" to { WeatherIcons.SnowShower },
    "408" to { WeatherIcons.LightToModerateSnow },
    "409" to { WeatherIcons.ModerateToHeavySnow },
    "410" to { WeatherIcons.HeavyToBlizzardSnow },
    "499" to { WeatherIcons.Snow },
    "500" to { WeatherIcons.LightFog },
    "501" to { WeatherIcons.Fog },
    "502" to { WeatherIcons.Haze },
    "503" to { WeatherIcons.DustStorm },
    "504" to { WeatherIcons.FloatingDust },
    "507" to { WeatherIcons.Sandstorm },
    "508" to { WeatherIcons.SevereSandstorm },
    "509" to { WeatherIcons.DenseFog },
    "510" to { WeatherIcons.SevereDenseFog },
    "511" to { WeatherIcons.ModerateHaze },
    "512" to { WeatherIcons.HeavyHaze },
    "513" to { WeatherIcons.SevereHaze },
    "514" to { WeatherIcons.HeavyFog },
    "515" to { WeatherIcons.ExtremelyDenseFog },
    "900" to { WeatherIcons.Hot },
    "901" to { WeatherIcons.Cold },
    "999" to { WeatherIcons.Unknown },
)

private val nightMap = mapOf<String, () -> MultilayerIcon>(
    "150" to { WeatherIcons.ClearNight },
    "151" to { WeatherIcons.MostlyCloudyNight },
    "152" to { WeatherIcons.PartlyCloudyNight} ,
    "153" to { WeatherIcons.MostlyClearWithIntermittentCloudsNight },
    "104" to { WeatherIcons.Overcast },
    "350" to { WeatherIcons.ShowerNight },
    "351" to { WeatherIcons.HeavyShowerNight },
    "302" to { WeatherIcons.Thunderstorm },
    "303" to { WeatherIcons.SevereThunderstorm },
    "304" to { WeatherIcons.ThunderstormWithHail },
    "305" to { WeatherIcons.LightRain },
    "306" to { WeatherIcons.ModerateRain },
    "307" to { WeatherIcons.HeavyRain },
    "308" to { WeatherIcons.ExtremeRain },
    "309" to { WeatherIcons.Drizzle },
    "310" to { WeatherIcons.TorrentialRain },
    "311" to { WeatherIcons.SevereTorrentialRain },
    "312" to { WeatherIcons.ExtremelySevereTorrentialRain },
    "313" to { WeatherIcons.FreezingRain },
    "314" to { WeatherIcons.LightToModerateRain },
    "315" to { WeatherIcons.ModerateToHeavyRain },
    "316" to { WeatherIcons.HeavyToTorrentialRain },
    "317" to { WeatherIcons.TorrentialToSevereTorrentialRain },
    "318" to { WeatherIcons.SevereTorrentialToExtremelySevereTorrentialRain },
    "400" to { WeatherIcons.LightSnow },
    "401" to { WeatherIcons.ModerateSnow },
    "402" to { WeatherIcons.HeavySnow },
    "403" to { WeatherIcons.Blizzard },
    "404" to { WeatherIcons.RainAndSnowMix },
    "405" to { WeatherIcons.RainAndSnow },
    "456" to { WeatherIcons.ShowerWithSnowNight },
    "457" to { WeatherIcons.SnowShowerNight },
    "408" to { WeatherIcons.LightToModerateSnow },
    "409" to { WeatherIcons.ModerateToHeavySnow },
    "410" to { WeatherIcons.HeavyToBlizzardSnow },
    "499" to { WeatherIcons.Snow },
    "500" to { WeatherIcons.LightFog },
    "501" to { WeatherIcons.Fog },
    "502" to { WeatherIcons.Haze },
    "503" to { WeatherIcons.DustStorm },
    "504" to { WeatherIcons.FloatingDust },
    "507" to { WeatherIcons.Sandstorm },
    "508" to { WeatherIcons.SevereSandstorm },
    "509" to { WeatherIcons.DenseFog },
    "510" to { WeatherIcons.SevereDenseFog },
    "511" to { WeatherIcons.ModerateHaze },
    "512" to { WeatherIcons.HeavyHaze },
    "513" to { WeatherIcons.SevereHaze },
    "514" to { WeatherIcons.HeavyFog },
    "515" to { WeatherIcons.ExtremelyDenseFog },
    "900" to { WeatherIcons.Hot },
    "901" to { WeatherIcons.Cold },
    "999" to { WeatherIcons.Unknown }
)

private fun iconCodeToWeatherIcon(code: String, isDay: Boolean): MultilayerIcon {
    return (if (isDay) dayMaps else nightMap)[code]?.invoke() ?: WeatherIcons.Unknown
}