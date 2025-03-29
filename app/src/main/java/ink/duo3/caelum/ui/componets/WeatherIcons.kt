package ink.duo3.caelum.ui.componets

import ink.duo3.caelum.R
import ink.duo3.caelum.ui.componets.MultilayerIcon.ColorTag

object WeatherIcons {
    val Clear = multilayerIcon {
        layerTag(R.drawable.weather_clear_24dp, ColorTag.SUN)
    }
    val PartlyCloudy = multilayerIcon {
        layerTag(R.drawable.weather_partly_cloudy_1_24dp, ColorTag.SUN)
        layerTag(R.drawable.weather_partly_cloudy_2_24dp, ColorTag.CLOUD)
    }
    val MostlyCloudy = multilayerIcon {
        layerTag(R.drawable.weather_mostly_cloudy_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_mostly_cloudy_2_24dp, ColorTag.SUN)
    }
    val MostlyClearWithIntermittentClouds = multilayerIcon {
        layerTag(R.drawable.weather_mostly_clear_with_intermittent_clouds_1_24dp, ColorTag.SUN)
        layerTag(R.drawable.weather_mostly_clear_with_intermittent_clouds_2_24dp, ColorTag.CLOUD)
    }
    val Overcast = multilayerIcon {
        layerTag(R.drawable.weather_overcast_24dp, ColorTag.CLOUD)
    }
    val ClearNight = multilayerIcon {
        layerTag(R.drawable.weather_clear_night_24dp, ColorTag.MOON)
    }
    val PartlyCloudyNight = multilayerIcon {
        layerTag(R.drawable.weather_partly_cloudy_1_night_24dp, ColorTag.MOON)
        layerTag(R.drawable.weather_partly_cloudy_2_24dp, ColorTag.CLOUD)
    }
    val MostlyCloudyNight = multilayerIcon {
        layerTag(R.drawable.weather_mostly_cloudy_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_mostly_cloudy_2_night_24dp, ColorTag.MOON)
    }
    val MostlyClearWithIntermittentCloudsNight = multilayerIcon {
        layerTag(R.drawable.weather_mostly_clear_with_intermittent_clouds_1_night_24dp, ColorTag.MOON)
        layerTag(R.drawable.weather_mostly_clear_with_intermittent_clouds_2_24dp, ColorTag.CLOUD)
    }
    val Shower = multilayerIcon {
        layerTag(R.drawable.weather_shower_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_shower_2_24dp, ColorTag.SUN)
        layerTag(R.drawable.weather_shower_3_24dp, ColorTag.RAIN)
    }
    val HeavyShower = multilayerIcon {
        layerTag(R.drawable.weather_shower_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_shower_2_24dp, ColorTag.SUN)
        layerTag(R.drawable.weather_heavy_shower_3_24dp, ColorTag.RAIN)
    }
    val Thunderstorm = multilayerIcon {
        layerTag(R.drawable.weather_thunderstorm_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_thunderstorm_2_24dp, ColorTag.SUN)
    }
    val SevereThunderstorm = multilayerIcon {
        layerTag(R.drawable.weather_thunderstorm_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_thunderstorm_2_24dp, ColorTag.SUN)
        layerTag(R.drawable.weather_severe_thunderstorm_3_24dp, ColorTag.RAIN)
    }
    val ThunderstormWithHail = multilayerIcon {
        layerTag(R.drawable.weather_thunderstorm_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_thunderstorm_2_24dp, ColorTag.SUN)
        layerTag(R.drawable.weather_thunderstorm_with_hail_3_24dp, ColorTag.CLOUD)
    }
    val LightRain = multilayerIcon {
        layerTag(R.drawable.weather_thunderstorm_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_shower_3_24dp, ColorTag.RAIN)
    }
    val ModerateRain = multilayerIcon {
        layerTag(R.drawable.weather_thunderstorm_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_heavy_shower_3_24dp, ColorTag.RAIN)
    }
    val HeavyRain = multilayerIcon {
        layerTag(R.drawable.weather_thunderstorm_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_heavy_rain_2_24dp, ColorTag.RAIN)
    }
    val ExtremeRain = multilayerIcon {
        layerTag(R.drawable.weather_thunderstorm_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_extreme_rain_2_24dp, ColorTag.RAIN)
    }
    val Drizzle = multilayerIcon {
        layerTag(R.drawable.weather_thunderstorm_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_drizzle_2_24dp, ColorTag.RAIN)
    }
    val TorrentialRain = multilayerIcon {
        layerTag(R.drawable.weather_torrential_rain_24dp, ColorTag.RAIN)
    }
    val SevereTorrentialRain = multilayerIcon {
        layerTag(R.drawable.weather_severe_torrential_rain_24dp, ColorTag.RAIN)
    }
    val ExtremelySevereTorrentialRain = multilayerIcon {
        layerTag(R.drawable.weather_extremely_severe_torrential_rain_24dp, ColorTag.RAIN)
    }

    val ShowerNight = multilayerIcon {
        layerTag(R.drawable.weather_shower_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_shower_2_night_24dp, ColorTag.MOON)
        layerTag(R.drawable.weather_shower_3_24dp, ColorTag.RAIN)
    }
    val HeavyShowerNight = multilayerIcon {
        layerTag(R.drawable.weather_shower_1_24dp, ColorTag.CLOUD)
        layerTag(R.drawable.weather_shower_2_night_24dp, ColorTag.MOON)
        layerTag(R.drawable.weather_heavy_shower_3_24dp, ColorTag.RAIN)
    }
}
