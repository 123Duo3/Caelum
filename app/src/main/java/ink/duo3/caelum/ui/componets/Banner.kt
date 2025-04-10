package ink.duo3.caelum.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ink.duo3.caelum.R
import ink.duo3.caelum.ui.theme.PreviewTheme

@Composable
fun Banner(
    temperature: String,
    modifier: Modifier = Modifier,
    text: String,
    feelsLike: String,
    maxTemp: String,
    minTemp: String
) {
    Box(
        modifier
            .height(360.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        val size = remember { mutableStateOf(Size.Unspecified) }

        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxSize(),
            shape = BannerShape(size.value)
        ) { }

        Column(
            Modifier.onGloballyPositioned { size.value = Size(it.size.width.toFloat(), it.size.height.toFloat()) }
                // This is a workaround because the shape is only available after the onGloballyPositioned is called (the second frame)
                .alpha(if (size.value == Size.Unspecified) 0f else 1f)
                .padding(end = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(81.dp)
                    .offset(y = 8.dp)
            ) {
                Text(
                    text = "$temperature°",
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.displayLarge
                        .copy(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Bottom,
                                trim = LineHeightStyle.Trim.None
                            )
                        ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = text,
                    modifier = Modifier,
                    style = MaterialTheme.typography.displayLarge
                        .copy(
                            fontSize = 50.sp,
                            fontWeight = FontWeight(500),
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = true
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Bottom,
                                trim = LineHeightStyle.Trim.None
                            )
                        ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "体感",
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(end = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    style = subtitleSmall
                )
                Text(
                    text = "$feelsLike°",
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(end = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    style = subtitleSmall
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_downward_20dp),
                    modifier = Modifier.padding(end = 4.dp),
                    contentDescription = "最低温度",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$minTemp°",
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(end = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = subtitleSmall
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_upward_20dp),
                    modifier = Modifier.padding(end = 4.dp),
                    contentDescription = "最高温度",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$maxTemp°",
                    modifier = Modifier.alignByBaseline(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = subtitleSmall
                )
            }
        }
    }
}

private class BannerShape(val cutoutSize: Size) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        if (cutoutSize == Size.Unspecified) return Outline.Generic(Path())

        val cornerRadiusPx = with(density) { 24.dp.toPx() }
        val cornerSizePx = with(density) { 48.dp.toPx() }
        val cornerSize = Size(cornerSizePx, cornerSizePx)

        val path = Path().apply {
            val width = size.width
            val height = size.height

            // Top left
            moveTo(0f, cornerRadiusPx)

            arcTo(
                rect = Rect(0f, 0f, 2 * cornerRadiusPx, 2 * cornerRadiusPx),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            lineTo(width - cornerRadiusPx, 0f)
            arcTo(
                rect = Rect(width - 2 * cornerRadiusPx, 0f, width, 2 * cornerRadiusPx),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            lineTo(width, height - cornerRadiusPx)
            arcTo(
                rect = Rect(width - 2 * cornerRadiusPx, height - 2 * cornerRadiusPx, width, height),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            lineTo(cutoutSize.width.toFloat(), height)
            arcTo(
                rect = Rect(
                    offset = Offset(cutoutSize.width.toFloat(), height - cornerSizePx),
                    size = cornerSize
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            lineTo(cutoutSize.width.toFloat(), height - cutoutSize.height + cornerRadiusPx)
            arcTo(
                rect = Rect(
                    offset = Offset(
                        cutoutSize.width - cornerSizePx,
                        height - cutoutSize.height
                    ),
                    size = cornerSize
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = -90f,
                forceMoveTo = false
            )

            lineTo(cornerRadiusPx, height - cutoutSize.height)
            arcTo(
                rect = Rect(
                    0f,
                    height - cutoutSize.height - 2 * cornerRadiusPx,
                    2 * cornerRadiusPx,
                    height - cutoutSize.height
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        return Outline.Generic(path)
    }
}

private val subtitleSmall = TextStyle(
    fontSize = 16.sp,
    lineHeight = 20.sp,
    fontWeight = FontWeight(600),
    platformStyle = PlatformTextStyle(
        includeFontPadding = true
    ),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Bottom,
        trim = LineHeightStyle.Trim.None
    )
)

@Preview
@Composable
private fun Preview() {
    PreviewTheme {
        Banner("24", text = "小雨", feelsLike = "26", maxTemp = "26", minTemp = "8")
    }
}