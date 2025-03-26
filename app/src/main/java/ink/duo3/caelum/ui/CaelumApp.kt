package ink.duo3.caelum.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ink.duo3.caelum.R
import ink.duo3.caelum.ui.componets.Banner
import ink.duo3.caelum.ui.componets.InfoCard
import ink.duo3.caelum.ui.theme.aqi1
import ink.duo3.caelum.ui.theme.aqi2
import ink.duo3.caelum.ui.theme.aqi3
import ink.duo3.caelum.ui.theme.aqi4
import ink.duo3.caelum.ui.theme.aqi5
import ink.duo3.caelum.ui.theme.aqi6
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
fun CaelumApp () {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .statusBarsPadding()
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Banner()

        Spacer(Modifier.height(48.dp))

        InfoCard(
            title = "即将停止下雨",
            subtitle = "31 分钟后将停止小雨。"
        ) {
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

        Spacer(Modifier.height(16.dp))

        InfoCard(
            icon = painterResource(R.drawable.ic_arrow_upward_20dp),
            category = "空气质量",
            title = "122",
            titleAlt = "不适于敏感人群",
            subtitle = "与昨天同时间类似。"
        ) {
            Row() {
                Box(
                    Modifier
                        .padding(0.dp, 16.dp)
                        .padding(end = 2.dp)
                        .height(12.dp)
                        .fillMaxWidth(0.09f)
                        .background(
                            color = aqi1.harmonized(),
                            shape = RoundedCornerShape(8.dp, 4.dp, 4.dp, 8.dp)
                        )
                )
                Box(
                    Modifier
                        .padding(2.dp, 16.dp)
                        .height(12.dp)
                        .fillMaxWidth(0.095f)
                        .background(
                            color = aqi2.harmonized(),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    Modifier
                        .padding(2.dp, 16.dp)
                        .height(12.dp)
                        .fillMaxWidth(0.125f)
                        .background(
                            color = aqi3.harmonized(),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    Modifier
                        .padding(2.dp, 16.dp)
                        .height(12.dp)
                        .fillMaxWidth(0.15f)
                        .background(
                            color = aqi4.harmonized(),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    Modifier
                        .padding(2.dp, 16.dp)
                        .height(12.dp)
                        .fillMaxWidth(0.333f)
                        .background(
                            color = aqi5.harmonized(),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    Modifier
                        .padding(0.dp, 16.dp)
                        .padding(start = 2.dp)
                        .height(12.dp)
                        .fillMaxWidth(1f)
                        .background(
                            color = aqi6.harmonized(),
                            shape =  RoundedCornerShape(4.dp, 8.dp, 8.dp, 4.dp)
                        )
                )
            }
        }
    }
}