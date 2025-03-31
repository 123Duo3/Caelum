package ink.duo3.caelum.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.HazeState
import ink.duo3.caelum.ui.screen.HomeScreen

val LocalHazeState = compositionLocalOf { HazeState() }

@Composable
fun CaelumApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        HomeScreen()
    }
}