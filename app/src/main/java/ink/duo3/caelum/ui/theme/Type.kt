package ink.duo3.caelum.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ink.duo3.caelum.R

val interFamily = FontFamily(
    Font(
        R.font.inter_variable
    ),
    Font(
        R.font.inter_variable_italic,
        style = FontStyle.Italic
    )
)

val Typography = Typography()//.copy(displayLarge = Typography().displayLarge.copy(fontFamily = interFamily))