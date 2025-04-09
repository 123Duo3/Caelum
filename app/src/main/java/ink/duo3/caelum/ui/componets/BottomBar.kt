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
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.hazeEffect
import ink.duo3.caelum.ui.LocalHazeState
import ink.duo3.caelum.ui.theme.CaelumHazeStyle
import ink.duo3.caelum.ui.theme.PreviewThemeWithBg

data class LocationItem(
    val name: String,
    val id: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeApi::class)
@Composable
fun BottomBar(
    locations: List<LocationItem>,
    selected: Int,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier) {
        Row(
            modifier = Modifier
                .hazeEffect(LocalHazeState.current, CaelumHazeStyle()) {
                    inputScale = HazeInputScale.Fixed(0.5f)
                }
                .navigationBarsPadding()
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Outlined.Map, contentDescription = "选择地区")
            }

            val state = rememberCarouselState { locations.size }

            HorizontalUncontainedCarousel(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                state = state,
                itemWidth = 100.dp,
                itemSpacing = 8.dp,
                contentPadding = PaddingValues(16.dp),
                flingBehavior = CarouselDefaults.singleAdvanceFlingBehavior(state)
            ) { index ->
                Surface(
                    modifier = Modifier.maskClip(MaterialTheme.shapes.medium),
                    color = MaterialTheme.colorScheme.secondary.copy(0.10f),
                    onClick = {
                        onSelectionChange(index)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 16.dp, end = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selected == index) {
                            Icon(Icons.Default.NearMe, "Near Me")
                            Spacer(Modifier.width(6.dp))
                        }
                        Text(
                            locations[index].name,
                            maxLines = 1,
                            style = MaterialTheme.typography.titleSmall
                        )
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
        val locations = remember { (1..10).map { LocationItem("地区$it", it.toString()) } }
        BottomBar(
            locations = locations,
            selected = 0,
            onSelectionChange = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}