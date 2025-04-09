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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
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
    val subtitleSmall = TextStyle(
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

    Box(
        modifier
            .height(360.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp)
        ) { }
        Column {
            Icon(
                painter = painterResource(R.drawable.shape_inner_cut),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.surfaceContainer
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Column(
                    Modifier
                        .clip(RoundedCornerShape(0, 24, 0, 0))
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(end = 24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(81.dp).offset(y = 8.dp)
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
                            modifier = Modifier.alignByBaseline().padding(end = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            style = subtitleSmall
                        )
                        Text(
                            text = "$feelsLike°",
                            modifier = Modifier.alignByBaseline().padding(end = 8.dp),
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
                            modifier = Modifier.alignByBaseline().padding(end = 8.dp),
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
                Icon(
                    painter = painterResource(R.drawable.shape_inner_cut),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.surfaceContainer
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewTheme {
        Banner("24", text = "小雨", feelsLike = "26", maxTemp = "26", minTemp = "8")
    }
}