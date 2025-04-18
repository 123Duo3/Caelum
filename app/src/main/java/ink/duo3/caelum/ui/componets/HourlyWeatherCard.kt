package ink.duo3.caelum.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import ink.duo3.caelum.ui.theme.PreviewTheme

data class HourlyWeatherInfo(
    val time: String,
    val temp: Int,
    val icon: MultilayerIcon,
    val description: String
)

@Composable
fun HourlyWeatherCard(modifier: Modifier = Modifier, data: List<HourlyWeatherInfo>) {
    InfoCard(modifier) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            data.fastForEachIndexed { i, item ->
                HourlyWeatherItem(
                    item.time,
                    item.temp,
                    item.icon,
                    item.description,
                    highlight = i == 0
                )
            }
        }
    }
}

@Composable
private fun HourlyWeatherItem(
    time: String,
    temp: Int,
    weatherIcon: MultilayerIcon,
    weatherLabel: String,
    highlight: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .then(
                if (highlight) Modifier.background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.small
                ) else Modifier
            )
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("$temp°", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(16.dp))
        MultilayerIcon(weatherIcon, weatherLabel)
        Spacer(Modifier.height(16.dp))
        Text(
            time,
            style = MaterialTheme.typography.titleSmall,
            color = LocalContentColor.current.copy(0.4f)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewTheme {
        HourlyWeatherCard(
            data = listOf(
                HourlyWeatherInfo("现在", 24, WeatherIcons.Rain, "Rain"),
                HourlyWeatherInfo("10 时", 24, WeatherIcons.TorrentialRain, "TorrentialRain"),
                HourlyWeatherInfo("11 时", 25, WeatherIcons.Clear, "Clear"),
                HourlyWeatherInfo("12 时", 25, WeatherIcons.Clear, "Clear"),
                HourlyWeatherInfo("下午 1 时", 26, WeatherIcons.Clear, "Clear")
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun PreviewItem() {
    PreviewTheme {
        HourlyWeatherItem(
            "现在",
            24,
            WeatherIcons.Clear,
            "Clear",
            true
        )
    }
}