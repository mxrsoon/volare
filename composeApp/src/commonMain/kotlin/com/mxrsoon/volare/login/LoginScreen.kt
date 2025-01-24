package com.mxrsoon.volare.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mxrsoon.volare.auth.Credentials
import com.mxrsoon.volare.common.ui.button.PrimaryButton
import com.mxrsoon.volare.common.ui.button.SecondaryButton
import com.mxrsoon.volare.common.ui.dialog.ErrorDialog
import com.mxrsoon.volare.common.ui.dialog.onEnterKeyDown
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Login screen.
 * @param presetEmail Email to pre-fill the email field.
 * @param presetPassword Password to pre-fill the password field.
 * @param onRegisterClick Called when the user clicks the register button. Receives the currently
 * entered credentials as a parameter.
 * @param onSignIn Called when the user successfully logs in.
 * @param viewModel The view model for this screen.
 */
@Composable
fun LoginScreen(
    presetEmail: String? = null,
    presetPassword: String? = null,
    onRegisterClick: (Credentials) -> Unit,
    onSignIn: () -> Unit,
    viewModel: LoginViewModel = viewModel { LoginViewModel() }
) {
    LaunchedEffect(Unit) {
        presetEmail?.let { viewModel.setEmail(it) }
        presetPassword?.let { viewModel.setPassword(it) }
    }

    LoginScreen(
        uiState = viewModel.uiState,
        onEmailChange = { viewModel.setEmail(it) },
        onPasswordChange = { viewModel.setPassword(it) },
        onRegisterClick = onRegisterClick,
        onSignInRequest = { viewModel.login() },
        onSignIn = {
            viewModel.resetLoggedIn()
            onSignIn()
        },
        onDismissErrorRequest = { viewModel.dismissError() }
    )
}

@Composable
private fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: (Credentials) -> Unit,
    onSignInRequest: () -> Unit,
    onSignIn: () -> Unit,
    onDismissErrorRequest: () -> Unit
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

                LoginFormTitle(Modifier.padding(top = 24.dp))

                LoginFormFields(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    email = uiState.email,
                    password = uiState.password,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onSubmit = onSignInRequest,
                    enabled = !uiState.loading
                )

                if (compact) Spacer(Modifier.weight(1f))

                LoginFormButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    compact = compact,
                    loading = uiState.loading,
                    onLoginClick = onSignInRequest,
                    onRegisterClick = { onRegisterClick(uiState.typedCredentials) }
                )
            }
        }
    }

    if (uiState.showError) {
        LoginErrorDialog(onDismissRequest = onDismissErrorRequest)
    }
}

@Composable
private fun LoginErrorDialog(onDismissRequest: () -> Unit) {
    ErrorDialog(
        title = stringResource(Res.string.login_error_title),
        message = stringResource(Res.string.login_error_message),
        onDismissRequest = onDismissRequest
    )
}

@Composable
private fun LoginFormTitle(modifier: Modifier = Modifier) {
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
private fun LoginFormFields(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean
) {
    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onEnterKeyDown(onSubmit),
            value = email,
            onValueChange = onEmailChange,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(Res.string.email_label)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .onEnterKeyDown(onSubmit),
            value = password,
            onValueChange = onPasswordChange,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(Res.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onSubmit() })
        )
    }
}

@Composable
private fun LoginFormButtons(
    compact: Boolean,
    loading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (compact) {
        Column(modifier) {
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.create_account_label),
                onClick = onRegisterClick,
                enabled = !loading
            )

            Spacer(Modifier.height(4.dp))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.sign_in_label),
                onClick = onLoginClick,
                enabled = !loading,
                loading = loading
            )
        }
    } else {
        Row(modifier) {
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.create_account_label),
                onClick = onRegisterClick,
                enabled = !loading
            )

            Spacer(Modifier.width(16.dp))

            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.sign_in_label),
                onClick = onLoginClick,
                enabled = !loading,
                loading = loading
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenDarkPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        LoginScreen(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onRegisterClick = {},
            onSignInRequest = {},
            onSignIn = {},
            onDismissErrorRequest = {}
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
        LoginScreen(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onRegisterClick = {},
            onSignInRequest = {},
            onSignIn = {},
            onDismissErrorRequest = {}
        )
    }
}