package dam_51606.playedit.ui.screens.auth.components

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dam_51606.playedit.R
import kotlin.math.sin

/**
 * Card where the user inserts their login information.
 * @param email account email
 * @param password account password
 * @param error error text, if there is any
 * @param onEmailChange lambda to execute when email text value changes
 * @param onPasswordChange lambda to execute when password text value changes
 */
@Composable
fun LoginCard(
    email: String,
    password: String,
    error: String? = null,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Email field
            OutlinedTextField(
               value = email,
               onValueChange = onEmailChange,
               label = {Text(stringResource(R.string.email_label))},
               singleLine = true,
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
               modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(R.string.password_label)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(), // hides password
                modifier = Modifier.fillMaxWidth()
            )

            // Error presentation
            if (error != null) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}