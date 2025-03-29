package ink.duo3.caelum.ui.componets

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import ink.duo3.caelum.R

object WeatherIcons {
    val Example = multilayerIcon {
        layer(R.drawable.ic_launcher_foreground, Color.Red)
        layer(R.drawable.ic_launcher_foreground) { MaterialTheme.colorScheme.primary }
        layerHarmonize(R.drawable.ic_launcher_foreground, Color.Red)
    }
}
