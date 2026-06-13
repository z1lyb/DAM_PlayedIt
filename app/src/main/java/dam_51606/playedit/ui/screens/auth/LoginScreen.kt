package dam_51606.playedit.ui.screens.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import dam_51606.playedit.R
import dam_51606.playedit.ui.screens.auth.components.AppIcon
import dam_51606.playedit.ui.screens.auth.components.LoginCard
import dam_51606.playedit.viewmodel.AuthUiState
import dam_51606.playedit.viewmodel.AuthViewModel

/**
 * Login screen UI scheme.
 * @param authViewModel authentication view model layer for data access
 * @param onRegisterButtonClick action to execute when clicking registration button
 */
@Composable
fun LoginScreenUI(
    authViewModel: AuthViewModel = viewModel(),
    onRegisterButtonClick: () -> Unit,
    onLoggedIn: () -> Unit
    ) {

    val loginUiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(loginUiState.isAuthenticated) {
        if (loginUiState.isAuthenticated) onLoggedIn()
    }

    val screenConfig = LocalConfiguration.current
    if(screenConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitLoginUI(
            uiState = loginUiState,
            onLogin = { email, password -> authViewModel.login(email, password) },
            onSelectRegister = onRegisterButtonClick
        )
    } else {
        LandscapeLoginUI(
            uiState = loginUiState,
            onLogin = { email, password -> authViewModel.login(email, password) },
            onSelectRegister = onRegisterButtonClick
        )
    }
}

@Composable
fun PortraitLoginUI(
    uiState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onSelectRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        AppIcon()

        Text(
            text = stringResource(R.string.login_welcome),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Login card
        LoginCard(
            email = email,
            password = password,
            error = uiState.error,
            onEmailChange = { email = it },
            onPasswordChange = { password = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Login button
        Button(
            onClick = { onLogin(email, password) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if(uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.login_label))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Register button
        Button(
            onClick = onSelectRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.account_create_label))
        }
    }
}


@Composable
fun LandscapeLoginUI(
    uiState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onSelectRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App icon + welcome text and buttons
        Column(
            modifier = Modifier.weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AppIcon()

            Text(
                text = stringResource(R.string.login_welcome),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = onSelectRegister,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.account_create_label))
            }
        }

        // Login info card

        Column(
            modifier = Modifier.weight(0.6f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LoginCard(
                email = email,
                password = password,
                error = uiState.error,
                onEmailChange = { email = it },
                onPasswordChange = { password = it }
            )

            Button(
                onClick = { onLogin(email, password) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.login_label))
                }
            }
        }
    }
}