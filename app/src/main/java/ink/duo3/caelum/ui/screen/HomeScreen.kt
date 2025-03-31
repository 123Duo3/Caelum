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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import ink.duo3.caelum.ui.LocalHazeState
import ink.duo3.caelum.ui.componets.AirQualityCard
import ink.duo3.caelum.ui.componets.Banner
import ink.duo3.caelum.ui.componets.BottomBar
import ink.duo3.caelum.ui.componets.InfoCard
import ink.duo3.caelum.ui.componets.MultilayerIcon
import ink.duo3.caelum.ui.componets.PrecipitationCard
import ink.duo3.caelum.ui.componets.WeatherIcons
import ink.duo3.caelum.ui.theme.PreviewThemeWithBg

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen() {
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
            Banner()

            Spacer(Modifier.height(48.dp))

            PrecipitationCard()

            Spacer(Modifier.height(16.dp))

            AirQualityCard(120)

            Spacer(Modifier.height(16.dp))

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
                    MultilayerIcon(WeatherIcons.ModerateRain, "")
                    MultilayerIcon(WeatherIcons.HeavyRain, "")
                    MultilayerIcon(WeatherIcons.ExtremeRain, "")
                    MultilayerIcon(WeatherIcons.Drizzle, "")
                    MultilayerIcon(WeatherIcons.TorrentialRain, "")
                    MultilayerIcon(WeatherIcons.SevereTorrentialRain, "")
                    MultilayerIcon(WeatherIcons.ExtremelySevereTorrentialRain, "")
                }
            }
        }


        CompositionLocalProvider(LocalHazeState provides hazeState) {
            BottomBar(Modifier.align(Alignment.BottomCenter).fillMaxWidth())
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