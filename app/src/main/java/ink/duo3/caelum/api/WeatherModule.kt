package ink.duo3.caelum.api

import ink.duo3.caelum.api.model.WebResp
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

class WeatherModule internal constructor(
    private val client: CaelumApiClient
) {
    @Serializable
    data class CityResp(val cities: List<CityItem>) {
        @Serializable
        data class CityItem(
            val cityId: String,
            val name: String,
            val latitude: String,
            val longitude: String,
            val city: String,
            val province: String,
            val country: String,
        )
    }

    suspend fun getCityByLocation(latitude: Double, longitude: Double): WebResp<CityResp> {
        return client.get("/weather/getCityByLocation") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
        }
    }
}