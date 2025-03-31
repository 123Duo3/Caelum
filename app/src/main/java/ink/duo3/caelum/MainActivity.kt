package ink.duo3.caelum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ink.duo3.caelum.ui.CaelumApp
import ink.duo3.caelum.ui.theme.CaelumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CaelumTheme {
                CaelumApp()
            }
        }
    }
}