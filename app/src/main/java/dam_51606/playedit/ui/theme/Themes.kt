package dam_51606.playedit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Dark and Light themes

private val darkColorScheme = darkColorScheme(
    // TODO
)

private val lightColorScheme = lightColorScheme(
    // TODO
)

/**
 * Selects the app theme to be used, depending on the device's theme
 * @param darkTheme
 * @param content
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme else lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}