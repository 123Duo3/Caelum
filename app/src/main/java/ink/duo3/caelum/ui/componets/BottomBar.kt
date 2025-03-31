package ink.duo3.caelum.ui.componets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import ink.duo3.caelum.ui.LocalHazeState
import ink.duo3.caelum.ui.theme.PreviewThemeWithBg

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun BottomBar(
    modifier: Modifier = Modifier
) {
    Surface(modifier, color = MaterialTheme.colorScheme.surfaceDim.copy(0.3f)) {
        Row(
            modifier = Modifier
                .hazeEffect(LocalHazeState.current, HazeMaterials.ultraThin(MaterialTheme.colorScheme.surfaceDim.copy(0.3f)))
                .navigationBarsPadding()
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Outlined.Map, contentDescription = "选择地区")
            }

            val items = remember { listOf("地区1", "地区2", "地区3", "地区4", "地区5", "地区6", "地区7", "地区7") }
            val state = rememberCarouselState { items.size }

            HorizontalUncontainedCarousel(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                state = state,
                itemWidth = 100.dp,
                itemSpacing = 8.dp,
                contentPadding = PaddingValues(16.dp),
                flingBehavior = CarouselDefaults.singleAdvanceFlingBehavior(state)
            ) {
                Surface(
                    modifier = Modifier.maskClip(MaterialTheme.shapes.medium),
                    color = MaterialTheme.colorScheme.secondary.copy(0.10f),
                ) {
                    Row(modifier = Modifier.fillMaxHeight().padding(start = 16.dp, end = 24.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NearMe, "Near Me")
                        Spacer(Modifier.width(6.dp))
                        Text(items[it], maxLines = 1, style = MaterialTheme.typography.titleSmall)
                    }
                }
            }

            IconButton(onClick = {}) {
                Icon(imageVector = Icons.AutoMirrored.Default.List, contentDescription = "菜单")
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewThemeWithBg {
        BottomBar(Modifier.fillMaxWidth())
    }
}