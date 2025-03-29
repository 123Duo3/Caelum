package ink.duo3.caelum.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ink.duo3.caelum.R
import ink.duo3.caelum.ui.componets.AirQualityCard
import ink.duo3.caelum.ui.componets.PrecipitationCard
import ink.duo3.caelum.ui.componets.Banner
import ink.duo3.caelum.ui.componets.InfoCard
import ink.duo3.caelum.ui.theme.CaelumTheme
import ink.duo3.caelum.ui.theme.aqi1
import ink.duo3.caelum.ui.theme.aqi2
import ink.duo3.caelum.ui.theme.aqi3
import ink.duo3.caelum.ui.theme.aqi4
import ink.duo3.caelum.ui.theme.aqi5
import ink.duo3.caelum.ui.theme.aqi6
import ink.duo3.caelum.ui.theme.harmonized

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .statusBarsPadding()
            .padding(16.dp)
            .wrapContentHeight()
    ) {
        Banner()

        Spacer(Modifier.height(48.dp))

        PrecipitationCard()

        Spacer(Modifier.height(16.dp))

        AirQualityCard(120)
    }
}

@Preview
@Composable
private fun Preview() {
    CaelumTheme {
        HomeScreen()
    }
}