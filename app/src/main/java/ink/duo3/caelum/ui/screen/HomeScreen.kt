package ink.duo3.caelum.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import ink.duo3.caelum.R
import ink.duo3.caelum.ui.LocalHazeState
import ink.duo3.caelum.ui.componets.AirQualityCard
import ink.duo3.caelum.ui.componets.Banner
import ink.duo3.caelum.ui.componets.BottomBar
import ink.duo3.caelum.ui.componets.DailyWeatherCard
import ink.duo3.caelum.ui.componets.HourlyWeatherCard
import ink.duo3.caelum.ui.componets.InfoCard
import ink.duo3.caelum.ui.componets.LocationItem
import ink.duo3.caelum.ui.componets.MultilayerIcon
import ink.duo3.caelum.ui.componets.PrecipitationCard
import ink.duo3.caelum.ui.componets.WeatherIcons
import ink.duo3.caelum.ui.theme.PreviewTheme
import ink.duo3.caelum.ui.theme.PreviewThemeWithBg
import ink.duo3.caelum.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(vm: MainViewModel = koinViewModel()) {
    Box {
        val hazeState = remember { HazeState() }
        Column(
            modifier = Modifier
                .hazeSource(hazeState)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 106.dp)
                .navigationBarsPadding()
        ) {
            vm.weather.value?.let { weather ->
                Banner(
                    weather.temp,
                    text = weather.text,
                    feelsLike = weather.feelsLike,
                    maxTemp = vm.dailyWeather.value.firstOrNull()?.tempMax?.toString() ?: "--",
                    minTemp = vm.dailyWeather.value.firstOrNull()?.tempMin?.toString() ?: "--"
                )

                Spacer(Modifier.height(48.dp))

                PrecipitationCard()

                Spacer(Modifier.height(16.dp))

                vm.aqi.value?.let {
                    AirQualityCard(it.aqi, it.level, it.effect)
                }

                Spacer(Modifier.height(16.dp))

                if(vm.hourlyWeather.value.isNotEmpty()) {
                    HourlyWeatherCard(Modifier.fillMaxWidth(), vm.hourlyWeather.value)
                }

                Spacer(Modifier.height(16.dp))

                if (vm.dailyWeather.value.isNotEmpty()) { // TODO: Loading animation
                    DailyWeatherCard(Modifier.fillMaxWidth(), vm.dailyWeather.value)
                }

                Spacer(Modifier.height(16.dp))
            }
        }

        if (vm.locationStatus.value.gpsStatus == MainViewModel.GpsStatus.PermissionDenied) {
            // TODO: Show message to user
        }

        CompositionLocalProvider(LocalHazeState provides hazeState) {
            BottomBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                gpsStatus = vm.locationStatus.value.gpsStatus,
                locations = vm.currentLocation.value?.let { listOf(LocationItem(it.name, it.cityId)) } ?: listOf(
                    LocationItem("定位中", "")
                    /*when {
                        vm.locationStatus.value.a() -> {}
                        vm.locationStatus.value.anyPending() -> {}
                        MainViewModel.GpsStatus.Idle, MainViewModel.GpsStatus.Pending -> LocationItem("定位中", "")
                        MainViewModel.GpsStatus.Ok -> LocationItem("请求中", "")
                        MainViewModel.GpsStatus.Error -> LocationItem("定位失败", "")
                        MainViewModel.GpsStatus.PermissionDenied -> LocationItem("权限被拒", "")
                    }*/
                ), // TODO: Show lists when location list is implemented
                selected = 0,
                onSelectionChange = {}
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewThemeWithBg {
        HomeScreen()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun IconsPreview() {
    PreviewTheme {
        Column {
            InfoCard {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MultilayerIcon(WeatherIcons.Clear, "")
                    MultilayerIcon(WeatherIcons.PartlyCloudy, "")
                    MultilayerIcon(WeatherIcons.MostlyClearWithIntermittentClouds, "")
                    MultilayerIcon(WeatherIcons.MostlyCloudy, "")
                    MultilayerIcon(WeatherIcons.Overcast, "")
                    MultilayerIcon(WeatherIcons.ClearNight, "")
                    MultilayerIcon(WeatherIcons.PartlyCloudyNight, "")
                    MultilayerIcon(WeatherIcons.MostlyClearWithIntermittentCloudsNight, "")
                    MultilayerIcon(WeatherIcons.MostlyCloudyNight, "")
                    MultilayerIcon(WeatherIcons.Shower, "")
                    MultilayerIcon(WeatherIcons.HeavyShower, "")
                    MultilayerIcon(WeatherIcons.ShowerNight, "")
                    MultilayerIcon(WeatherIcons.HeavyShowerNight, "")
                    MultilayerIcon(WeatherIcons.Thunderstorm, "")
                    MultilayerIcon(WeatherIcons.SevereThunderstorm, "")
                    MultilayerIcon(WeatherIcons.ThunderstormWithHail, "")
                    MultilayerIcon(WeatherIcons.LightRain, "")
                    MultilayerIcon(WeatherIcons.LightToModerateRain, "")
                    MultilayerIcon(WeatherIcons.ModerateRain, "")
                    MultilayerIcon(WeatherIcons.ModerateToHeavyRain, "")
                    MultilayerIcon(WeatherIcons.HeavyRain, "")
                    MultilayerIcon(WeatherIcons.HeavyToTorrentialRain, "")
                    MultilayerIcon(WeatherIcons.TorrentialRain, "")
                    MultilayerIcon(WeatherIcons.TorrentialToSevereTorrentialRain, "")
                    MultilayerIcon(WeatherIcons.SevereTorrentialRain, "")
                    MultilayerIcon(
                        WeatherIcons.SevereTorrentialToExtremelySevereTorrentialRain,
                        ""
                    )
                    MultilayerIcon(WeatherIcons.ExtremelySevereTorrentialRain, "")
                    MultilayerIcon(WeatherIcons.ExtremeRain, "")
                    MultilayerIcon(WeatherIcons.Drizzle, "")
                    MultilayerIcon(WeatherIcons.FreezingRain, "")
                    MultilayerIcon(WeatherIcons.Rain, "")
                    MultilayerIcon(WeatherIcons.LightSnow, "")
                    MultilayerIcon(WeatherIcons.LightToModerateSnow, "")
                    MultilayerIcon(WeatherIcons.ModerateSnow, "")
                    MultilayerIcon(WeatherIcons.ModerateToHeavySnow, "")
                    MultilayerIcon(WeatherIcons.HeavySnow, "")
                    MultilayerIcon(WeatherIcons.HeavyToBlizzardSnow, "")
                    MultilayerIcon(WeatherIcons.Blizzard, "")
                    MultilayerIcon(WeatherIcons.RainAndSnowMix, "")
                    MultilayerIcon(WeatherIcons.RainAndSnow, "")
                    MultilayerIcon(WeatherIcons.ShowerWithSnow, "")
                    MultilayerIcon(WeatherIcons.SnowShower, "")
                    MultilayerIcon(WeatherIcons.ShowerWithSnowNight, "")
                    MultilayerIcon(WeatherIcons.SnowShowerNight, "")
                    MultilayerIcon(WeatherIcons.Snow, "")
                    MultilayerIcon(WeatherIcons.LightFog, "")
                    MultilayerIcon(WeatherIcons.Fog, "")
                    MultilayerIcon(WeatherIcons.HeavyFog, "")
                    MultilayerIcon(WeatherIcons.DenseFog, "")
                    MultilayerIcon(WeatherIcons.SevereDenseFog, "")
                    MultilayerIcon(WeatherIcons.ExtremelyDenseFog, "")
                    MultilayerIcon(WeatherIcons.Haze, "")
                    MultilayerIcon(WeatherIcons.ModerateHaze, "")
                    MultilayerIcon(WeatherIcons.HeavyHaze, "")
                    MultilayerIcon(WeatherIcons.SevereHaze, "")
                    MultilayerIcon(WeatherIcons.FloatingDust, "")
                    MultilayerIcon(WeatherIcons.DustStorm, "")
                    MultilayerIcon(WeatherIcons.Sandstorm, "")
                    MultilayerIcon(WeatherIcons.SevereSandstorm, "")
                    MultilayerIcon(WeatherIcons.Hot, "")
                    MultilayerIcon(WeatherIcons.Cold, "")
                    MultilayerIcon(WeatherIcons.Unknown, "")
                }
            }

            Spacer(Modifier.height(12.dp))

            InfoCard {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.warning_typhoon_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_tornado_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_rainstorm_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_snow_storm_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_cold_wave_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_gale_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_heat_wave_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_downburst_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_avalanche_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_lightning_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_hail_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_frost_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        painterResource(R.drawable.warning_heavy_fog_24dp),
                        "",
                        Modifier,
                        MaterialTheme.colorScheme.onSurface
                    )

                }
            }
        }
    }
}