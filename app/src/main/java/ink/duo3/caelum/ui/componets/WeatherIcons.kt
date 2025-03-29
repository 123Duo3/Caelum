package ink.duo3.caelum.ui.componets

import androidx.compose.ui.graphics.Color
import ink.duo3.caelum.R

object WeatherIcons {
    val Example = multilayerIcon {
        layer(R.drawable.ic_launcher_foreground, Color.Red)
        layerHarmonize(R.drawable.ic_launcher_foreground, Color.Red)
    }
}
