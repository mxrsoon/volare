package com.mxrsoon.volare.login

import androidx.compose.runtime.Composable
import com.mxrsoon.volare.common.ui.dialog.ErrorDialog
import com.mxrsoon.volare.resources.Res
import com.mxrsoon.volare.resources.login_error_message
import com.mxrsoon.volare.resources.login_error_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginErrorDialog(onDismissRequest: () -> Unit) {
    ErrorDialog(
        title = stringResource(Res.string.login_error_title),
        message = stringResource(Res.string.login_error_message),
        onDismissRequest = onDismissRequest
    )
}