package ink.duo3.caelum.ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ink.duo3.caelum.R
import ink.duo3.caelum.ui.theme.PreviewTheme

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    title: String?=null,
    subtitle: String?=null,
    content: @Composable () -> Unit
) {
    Column(
        modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp, 12.dp)
    ) {
        title?.let {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        subtitle?.let {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
        content.invoke()
    }
}

@Composable
fun InfoCard(
    icon: Painter,
    category: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    titleAlt: String? = null,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, "", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            Text(
                text = category,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        Row {
            title?.let {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp).alignByBaseline()
                )
            }
            titleAlt?.let {
                Text(
                    text = titleAlt,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.alignByBaseline()
                )
            }
        }

        content.invoke()

        subtitle?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Preview
@Composable
fun InfoCardPreview() {
    PreviewTheme {
        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            icon = painterResource(R.drawable.ic_arrow_upward_20dp),
            category = "空气质量",
            title = "122",
            titleAlt = "不适于敏感人群",
            subtitle = "与昨天同时间类似。"
        ) {
            Spacer(Modifier.height(128.dp))
        }
    }
}