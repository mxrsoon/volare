package com.mxrsoon.volare.login.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mxrsoon.volare.common.ui.button.PrimaryButton
import com.mxrsoon.volare.common.ui.button.SecondaryButton
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.login.LoginErrorDialog
import com.mxrsoon.volare.resources.Res
import com.mxrsoon.volare.resources.app_name
import com.mxrsoon.volare.resources.create_account_label
import com.mxrsoon.volare.resources.sign_in_with_email_label
import com.mxrsoon.volare.resources.sign_in_with_google_label
import com.mxrsoon.volare.resources.welcome_to_format
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoginOptionsScreen(
    enableGoogleSignIn: Boolean,
    googleAuthToken: String?,
    onGoogleSignInRequest: () -> Unit,
    onEmailSignInClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onSignIn: () -> Unit,
    viewModel: LoginOptionsViewModel = viewModel { LoginOptionsViewModel() }
) {
    LaunchedEffect(googleAuthToken) {
        googleAuthToken?.let { viewModel.signInWithGoogle(it) }
    }

    LoginOptionsScreen(
        uiState = viewModel.uiState,
        enableGoogleSignIn = enableGoogleSignIn,
        onGoogleSignInRequest = onGoogleSignInRequest,
        onEmailSignInClick = onEmailSignInClick,
        onRegisterClick = onRegisterClick,
        onDismissErrorRequest = { viewModel.dismissError() },
        onSignIn = {
            viewModel.resetLoggedIn()
            onSignIn()
        }
    )
}

@Composable
fun LoginOptionsScreen(
    uiState: LoginOptionsUiState,
    enableGoogleSignIn: Boolean,
    onGoogleSignInRequest: () -> Unit,
    onEmailSignInClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onDismissErrorRequest: () -> Unit,
    onSignIn: () -> Unit
) {
    LaunchedEffect(uiState.loggedIn) {
        if (uiState.loggedIn) {
            onSignIn()
        }
    }

    Scaffold(Modifier.imePadding()) { paddingValues ->
        val scrollState = rememberScrollState()

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val compact = maxWidth < 480.dp
            val maxFormWidth = if (compact) Dp.Unspecified else 480.dp

            Column(
                modifier = Modifier
                    .widthIn(max = maxFormWidth)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .consumeWindowInsets(PaddingValues(horizontal = 24.dp))
                    .consumeWindowInsets(paddingValues)
                    .padding(horizontal = 24.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (compact) Spacer(Modifier.weight(1f))

                LoginOptionsTitle(Modifier.padding(top = 24.dp))

                if (compact) Spacer(Modifier.weight(1f))

                LoginOptionsButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    enableGoogleSignIn = enableGoogleSignIn,
                    onGoogleSignInClick = onGoogleSignInRequest,
                    onEmailSignInClick = onEmailSignInClick,
                    onRegisterClick = onRegisterClick,
                    signingInWithGoogle = uiState.signingInWithGoogle,
                    enabled = uiState.enableOptions
                )
            }
        }
    }

    if (uiState.showError) {
        LoginErrorDialog(onDismissRequest = onDismissErrorRequest)
    }
}

@Composable
private fun LoginOptionsTitle(modifier: Modifier = Modifier) {
    val appName = stringResource(Res.string.app_name)
    val title = stringResource(Res.string.welcome_to_format, appName)

    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append(title)

            addStyle(
                style = SpanStyle(color = MaterialTheme.colorScheme.primary),
                start = title.indexOf(appName),
                end = title.indexOf(appName) + appName.length
            )
        },
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun LoginOptionsButtons(
    enableGoogleSignIn: Boolean,
    onGoogleSignInClick: () -> Unit,
    onEmailSignInClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
    signingInWithGoogle: Boolean = false,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.create_account_label),
            onClick = onRegisterClick,
            enabled = enabled
        )

        if (enableGoogleSignIn) {
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.sign_in_with_email_label),
                onClick = onEmailSignInClick,
                enabled = enabled
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.sign_in_with_google_label),
                onClick = onGoogleSignInClick,
                loading = signingInWithGoogle,
                enabled = enabled
            )
        } else {
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.sign_in_with_email_label),
                onClick = onEmailSignInClick,
                enabled = enabled
            )
        }
    }
}

@Preview
@Composable
private fun LoginOptionsScreenDarkPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        LoginOptionsScreen(
            uiState = LoginOptionsUiState(),
            enableGoogleSignIn = false,
            onGoogleSignInRequest = {},
            onEmailSignInClick = {},
            onRegisterClick = {},
            onDismissErrorRequest = {},
            onSignIn = {}
        )
    }
}

@Preview
@Composable
private fun LoginScreenLightPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = false
    ) {
        LoginOptionsScreen(
            uiState = LoginOptionsUiState(),
            enableGoogleSignIn = true,
            onGoogleSignInRequest = {},
            onEmailSignInClick = {},
            onRegisterClick = {},
            onDismissErrorRequest = {},
            onSignIn = {}
        )
    }
}