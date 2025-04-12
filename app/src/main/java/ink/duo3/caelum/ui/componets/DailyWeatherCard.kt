package ink.duo3.caelum.ui.componets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import ink.duo3.caelum.ui.theme.PreviewTheme
import ink.duo3.caelum.ui.theme.PreviewThemeWithBg

data class DailyWeatherInfo(
    val date: String,
    val icon: MultilayerIcon,
    val description: String,
    val probability: String?,
    val tempMin: Int,
    val tempMax: Int,
)

@Composable
fun DailyWeatherCard(modifier: Modifier, data: List<DailyWeatherInfo>) {
    InfoCard(
        modifier = modifier,
        icon = rememberVectorPainter(Icons.Default.CalendarMonth),
        category = "10 日天气预报",
    ) {
        Column(Modifier.padding(top = 12.dp)) {
            val maxTemp = remember(data) { data.maxOf { it.tempMax } }
            val minTemp = remember(data) { data.minOf { it.tempMin } }

            data.fastForEachIndexed { index, it ->
                DailyWeatherItem(
                    label = it.date,
                    weatherIcon = it.icon,
                    weatherDescription = "TorrentialRain",
                    tempRangeMin = minTemp,
                    tempRangeMax = maxTemp,
                    tempMin = it.tempMin,
                    tempMax = it.tempMax,
                    highlight = index == 0,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}


@Composable
private fun DailyWeatherItem(
    label: String,
    weatherIcon: MultilayerIcon,
    weatherDescription: String,
    tempRangeMin: Int,
    tempRangeMax: Int,
    tempMin: Int,
    tempMax: Int,
    highlight: Boolean,
    modifier: Modifier = Modifier,
    probability: String? = null
) {
    Row(
        modifier
            .then(
                if (highlight) Modifier.background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium
                ) else Modifier
            )
            .padding(horizontal = 6.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tempTextStyle = MaterialTheme.typography.titleSmall.copy(fontFeatureSettings = "tnum")
        // For fixed width
        val tempTextWidth = measureTempTextWidth(tempTextStyle)

        Text(label, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.width(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MultilayerIcon(weatherIcon, weatherDescription)
            probability?.let {
                Text(
                    text = it,
                    color = Color(0xFF7DB3CF),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Text(
            modifier = Modifier.width(tempTextWidth),
            text = "$tempMin°",
            color = LocalContentColor.current.copy(0.4f),
            style = tempTextStyle,
            textAlign = TextAlign.End
        )
        Spacer(Modifier.width(16.dp))
        TempIndicator(tempRangeMin, tempRangeMax, tempMin, tempMax, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(16.dp))
        Text(
            modifier = Modifier.width(tempTextWidth),
            text = "$tempMax°",
            style = tempTextStyle,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun measureTempTextWidth(style: TextStyle): Dp {
    val measurer = rememberTextMeasurer()
    val measureResult =
        measurer.measure(text = "-00°", style = style)
    return with(LocalDensity.current) { measureResult.size.width.toDp() }
}

@Composable
private fun TempIndicator(
    rangeMin: Int,
    rangeMax: Int,
    currentMin: Int,
    currentMax: Int,
    modifier: Modifier
) {
    val trackColor = MaterialTheme.colorScheme.surfaceContainer
    val indicator = Brush.horizontalGradient(
        0f to Color(0xFF98D6DF),
        0.8f to Color(0xFFE6B62F),
        1f to Color(0xFFE69D2F)
    )
    Canvas(modifier.requiredHeight(4.dp)) {
        drawRoundRect(trackColor, size = size, cornerRadius = CornerRadius(size.height / 2))
        val startX =
            (currentMin - rangeMin).toFloat() / (rangeMax - rangeMin).toFloat() * size.width
        val width =
            (currentMax - currentMin).toFloat() / (rangeMax - rangeMin).toFloat() * size.width
        drawRoundRect(
            indicator,
            size = Size(width, size.height),
            topLeft = Offset(startX, 0f),
            cornerRadius = CornerRadius(size.height / 2)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewTheme {
        val testData = remember {
            listOf(
                DailyWeatherInfo(
                    date = "今天",
                    icon = WeatherIcons.Rain,
                    description = "Rain",
                    probability = null,
                    tempMin = 8,
                    tempMax = 24
                ),
                DailyWeatherInfo(
                    date = "明天",
                    icon = WeatherIcons.TorrentialRain,
                    description = "Clear",
                    probability = "40%",
                    tempMin = 11,
                    tempMax = 23
                ),
                DailyWeatherInfo(
                    date = "周二",
                    icon = WeatherIcons.Clear,
                    description = "Clear",
                    probability = null,
                    tempMin = 12,
                    tempMax = 16
                ),
            )
        }
        DailyWeatherCard(modifier = Modifier.fillMaxWidth(), testData)
    }
}

@Composable
@Preview
private fun PreviewDailyTempItemCommon() {
    PreviewThemeWithBg {
        DailyWeatherItem(
            "今天", WeatherIcons.Rain, "TorrentialRain",
            10, 30,
            14, 24,
            false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun PreviewDailyTempItemHighlight() {
    PreviewTheme {
        DailyWeatherItem(
            "明天", WeatherIcons.TorrentialRain, "TorrentialRain",
            10, 30,
            14, 24,
            true,
            modifier = Modifier.fillMaxWidth(),
            "40%"
        )
    }
}

@Composable
@Preview
private fun PreviewIndicator() {
    PreviewTheme {
        TempIndicator(9, 42, 15, 35, Modifier.fillMaxWidth())
    }
}