package ink.duo3.caelum.ui.componets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ink.duo3.caelum.ui.theme.CaelumTheme
import ink.duo3.caelum.ui.theme.harmonized
import ink.duo3.caelum.ui.theme.temperature0
import ink.duo3.caelum.ui.theme.temperature10
import ink.duo3.caelum.ui.theme.temperature20
import ink.duo3.caelum.ui.theme.temperature30
import ink.duo3.caelum.ui.theme.temperature40
import ink.duo3.caelum.ui.theme.temperature50
import ink.duo3.caelum.ui.theme.temperatureMinor10
import ink.duo3.caelum.ui.theme.temperatureMinor20
import ink.duo3.caelum.ui.theme.temperatureMinor40

@Composable
fun PrecipitationCard() {
    InfoCard(
        title = "即将停止下雨",
        subtitle = "31 分钟后将停止小雨。"
    ) {
        // TODO: Input data
        Chart(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
    }
}

@Composable
private fun TemperatureIndicator() {
    Box(
        Modifier
            .padding(0.dp, 32.dp)
            .height(4.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        temperatureMinor40.harmonized(),
                        temperatureMinor20.harmonized(),
                        temperatureMinor10.harmonized(),
                        temperature0.harmonized(),
                        temperature10.harmonized(),
                        temperature20.harmonized(),
                        temperature30.harmonized(),
                        temperature40.harmonized(),
                        temperature50.harmonized(),
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
    )
}

private val LineThickness = 1.dp
private val LevelHeight = 21.dp
private val TickHeight = 5.dp
private val BarGap = 8.dp
private val BarThickness = 4.dp

@Composable
private fun Chart(modifier: Modifier) {
    val density = LocalDensity.current
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val lineColor = MaterialTheme.colorScheme.surfaceContainer
    val barColor = Color(0xFF7DB3CF)

    val textMeasurer = rememberTextMeasurer()

    val textStyle = MaterialTheme.typography.labelSmall.copy(color = outlineColor)
    val text1Size = textMeasurer.measure(text = "现在", style = textStyle)
    val text2Size = textMeasurer.measure(text = "10 分后", style = textStyle)
    val text3Size = textMeasurer.measure(text = "30 分后", style = textStyle)

    val textHeight = with(density) {
        maxOf(
            text1Size.size.height,
            text2Size.size.height,
            text3Size.size.height
        ).toDp()
    }
    val height = (LevelHeight * 3) + TickHeight + textHeight

    Canvas(modifier.height(height)) {
        val levelHeight = LevelHeight.toPx()
        val p = LineThickness.toPx() / 2 // For stroke

        // Lines
        repeat(3) {
            drawLine(
                color = lineColor,
                start = Offset(0f, levelHeight * it + p),
                end = Offset(size.width, levelHeight * it + p),
                strokeWidth = LineThickness.toPx()
            )
        }
        val bottomLineY = levelHeight * 3 - p
        val tickHeight = TickHeight.toPx()

        // Bars
        val barGap = BarGap.toPx()
        val barWidth = BarThickness.toPx()
        val capRadius = barWidth / 2

        val maxBarHeight = levelHeight * 3 - 2.dp.toPx()

        repeat(60) { // TODO: How many bars should we show?
            val weight = it / 60f // TODO: Take from data
            val height = maxBarHeight * weight

            val left = barGap * it
            val right = left + barWidth

            if (weight > 0f /* TODO: Should we show bar cap? */ && right <= size.width) {
                val topY = bottomLineY - height
                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                left = left,
                                right = right,
                                top = topY,
                                bottom = bottomLineY
                            ),
                            topLeft = CornerRadius(capRadius),
                            topRight = CornerRadius(capRadius),
                        )
                    )
                }
                drawPath(path, barColor)
            }
        }

        // Tick - Start
        drawLine(
            color = outlineColor,
            start = Offset(p, bottomLineY),
            end = Offset(p, bottomLineY + tickHeight),
            strokeWidth = LineThickness.toPx()
        )

        // Tick - 10 minutes
        val tick10x = barGap * 10 + barWidth / 2
        drawLine(
            color = outlineColor,
            start = Offset(tick10x, bottomLineY),
            end = Offset(tick10x, bottomLineY + tickHeight),
            strokeWidth = LineThickness.toPx()
        )

        // Tick - 30 minutes
        val tick30x = barGap * 30 + barWidth / 2
        drawLine(
            color = outlineColor,
            start = Offset(tick30x, bottomLineY),
            end = Offset(tick30x, bottomLineY + tickHeight),
            strokeWidth = LineThickness.toPx()
        )

        // Bottom line
        drawLine(
            color = outlineColor,
            start = Offset(0f, bottomLineY),
            end = Offset(size.width, bottomLineY),
            strokeWidth = LineThickness.toPx()
        )

        // Text
        val textTop = bottomLineY + TickHeight.toPx()
        drawText(textMeasurer, "现在", style = textStyle, topLeft = Offset(0f, textTop))
        drawText(
            textMeasurer, "10 分后", style = textStyle, topLeft = Offset(
                tick10x - (text2Size.size.width / 2), textTop
            )
        )
        drawText(
            textMeasurer, "30 分后", style = textStyle, topLeft = Offset(
                tick30x - (text3Size.size.width / 2), textTop
            )
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CaelumTheme {
        PrecipitationCard()
    }
}