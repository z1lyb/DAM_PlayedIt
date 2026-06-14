package dam_51606.playedit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dam_51606.playedit.R

/**
 * Component that displays the application icon.
 * Used in login and registration screens.
 */
@Composable
fun AppIcon() {
    val context = LocalContext.current

    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "PlayedIt logo",
        modifier = Modifier.size(120.dp)
    )
}