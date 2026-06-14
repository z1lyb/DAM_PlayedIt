package dam_51606.playedit.ui.screens.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam_51606.playedit.R
import dam_51606.playedit.ui.components.AppIcon
import dam_51606.playedit.ui.components.auth.RegisterCard
import dam_51606.playedit.viewmodel.AuthUiState
import dam_51606.playedit.viewmodel.AuthViewModel

@Composable
fun RegisterScreenUI(
    authViewModel: AuthViewModel = viewModel(),
    onLoginButtonClick: () -> Unit,
    onRegistered: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) onRegistered()
    }

    val screenConfig = LocalConfiguration.current
    if (screenConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitRegisterUI(
            uiState = uiState,
            onRegister = { username, email, password ->
                authViewModel.register(email, password, username)
            },
            onSelectLogin = onLoginButtonClick
        )
    } else {
        LandscapeRegisterUI(
            uiState = uiState,
            onRegister = { username, email, password ->
                authViewModel.register(email, password, username)
            },
            onSelectLogin = onLoginButtonClick
        )
    }
}

@Composable
fun PortraitRegisterUI(
    uiState: AuthUiState,
    onRegister: (String, String, String) -> Unit,
    onSelectLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppIcon()

        Text(
            text = stringResource(R.string.register_title),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        RegisterCard(
            username = username,
            email = email,
            password = password,
            error = uiState.error,
            onUsernameChange = { username = it },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onRegister(username, email, password) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.register_label))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSelectLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.login_label))
        }
    }
}

@Composable
fun LandscapeRegisterUI(
    uiState: AuthUiState,
    onRegister: (String, String, String) -> Unit,
    onSelectLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppIcon()

            Text(
                text = stringResource(R.string.register_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = { onRegister(username, email, password) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.register_label))
                }
            }

            Button(
                onClick = onSelectLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.login_label))
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            RegisterCard(
                username = username,
                email = email,
                password = password,
                error = uiState.error,
                onUsernameChange = { username = it },
                onEmailChange = { email = it },
                onPasswordChange = { password = it }
            )
        }
    }
}