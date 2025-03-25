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
            Spacer(Modifier.height(64.dp))
        }

        Spacer(Modifier.height(16.dp))

        InfoCard(
            icon = painterResource(R.drawable.ic_arrow_upward_20dp),
            category = "空气质量",
            title = "122",
            titleAlt = "不适于敏感人群",
            subtitle = "与昨天同时间类似。"
        ) {
            Spacer(Modifier.height(64.dp))
        }
    }
}