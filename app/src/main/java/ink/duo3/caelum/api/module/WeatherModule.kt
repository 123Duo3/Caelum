package ink.duo3.caelum.api.module

import ink.duo3.caelum.api.CaelumApiClient
import ink.duo3.caelum.api.model.WebResp
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import kotlinx.datetime.Instant
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

    @Serializable
    data class DailyWeatherResp(
        val daily: List<Item>
    ) {
        @Serializable
        data class Item(
            /**
             * 预报日期
             */
            val fxDate: String,
            /**
             * 日出时间，在高纬度地区可能为空
             */
            val sunrise: String?,
            /**
             * 日落时间，在高纬度地区可能为空
             */
            val sunset: String?,
            /**
             * 当天月升时间，可能为空
             */
            val moonrise: String?,
            /**
             * 当天月落时间，可能为空
             */
            val moonset: String?,
            /**
             * 月相名称
             */
            val moonPhase: String,
            /**
             * 月相图标代码，另请参考天气图标项目
             */
            val moonPhaseIcon: String,
            /**
             * 预报当天最高温度
             */
            val tempMax: String,
            /**
             * 预报当天最低温度
             */
            val tempMin: String,
            /**
             * 预报白天天气状况的图标代码，另请参考天气图标项目
             */
            val iconDay: String,
            /**
             * 预报白天天气状况文字描述，包括阴晴雨雪等天气状态的描述
             */
            val textDay: String,
            /**
             * 预报夜间天气状况的图标代码，另请参考天气图标项目
             */
            val iconNight: String,
            /**
             * 预报晚间天气状况文字描述，包括阴晴雨雪等天气状态的描述
             */
            val textNight: String,
            /**
             * 预报白天风向360角度
             */
            val wind360Day: String,
            /**
             * 预报白天风向
             */
            val windDirDay: String,
            /**
             * 预报白天风力等级
             */
            val windScaleDay: String,
            /**
             * 预报白天风速，公里/小时
             */
            val windSpeedDay: String,
            /**
             * 预报夜间风向360角度
             */
            val wind360Night: String,
            /**
             * 预报夜间当天风向
             */
            val windDirNight: String,
            /**
             * 预报夜间风力等级
             */
            val windScaleNight: String,
            /**
             * 预报夜间风速，公里/小时
             */
            val windSpeedNight: String,
            /**
             * 预报当天总降水量，默认单位：毫米
             */
            val precip: String,
            /**
             * 紫外线强度指数
             */
            val uvIndex: String,
            /**
             * 相对湿度，百分比数值
             */
            val humidity: String,
            /**
             * 大气压强，默认单位：百帕
             */
            val pressure: String,
            /**
             * 能见度，默认单位：公里
             */
            val vis: String,
            /**
             * 云量，百分比数值。可能为空
             */
            val cloud: String?,
        )
    }

    suspend fun get10d(cityId: String): WebResp<DailyWeatherResp> {
        return client.get("/weather/10d") {
            parameter("cityId", cityId)
        }
    }

    @Serializable
    data class AqiNowResp(
        val aqi: Int,
        val level: Int,
        val effect: String
    )

    suspend fun getAqiNow(latitude: Double, longitude: Double): WebResp<AqiNowResp> {
        return client.get("/weather/aqiNow") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
        }
    }

    @Serializable
    data class HourlyWeatherResp(
        val hourly: List<Item>
    ) {
        @Serializable
        data class Item(
            val time: Instant,
            val temp: Int,
            val icon: String,
            val text: String
        )
    }

    suspend fun get24h(cityId: String): WebResp<HourlyWeatherResp> {
        return client.get("/weather/24h") {
            parameter("cityId", cityId)
        }
    }
}