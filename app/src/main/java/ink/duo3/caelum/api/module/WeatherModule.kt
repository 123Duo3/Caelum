package ink.duo3.caelum.api.module

import ink.duo3.caelum.api.CaelumApiClient
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

    @Serializable
    data class NowResp(
        /// 数据观测时间 "2020-06-30T21:40+08:00",
        val obsTime: String,
        /// 温度，默认单位：摄氏度 "24",
        val temp: String,
        /// 体感温度，默认单位：摄氏度 "26",
        val feelsLike: String,
        /// 天气状况的图标代码 "101",
        val icon: String,
        /// 天气状况的文字描述，包括阴晴雨雪等天气状态的描述 "多云",
        val text: String,
        /// 风向 "123",
        val wind360: String,
        /// 风向 "东南风",
        val windDir: String,
        /// 风力等级 "1",
        val windScale: String,
        /// 风速，公里/小时 "3",
        val windSpeed: String,
        /// 相对湿度，百分比数值 "72",
        val humidity: String,
        /// 过去1小时降水量，默认单位：毫米 "0.0",
        val precip: String,
        /// 大气压强，默认单位：百帕 "1003",
        val pressure: String,
        /// 能见度，默认单位：公里 "16",
        val vis: String,
        /// 云量，百分比数值。可能为空 "10",
        val cloud: String?,
        /// 露点温度 "21"
        val dew: String?,
    )
    /**
     * Get weather now
     */
    suspend fun now(cityId: String): WebResp<NowResp> {
        return client.get("/weather/now") {
            parameter("cityId", cityId)
        }
    }
}