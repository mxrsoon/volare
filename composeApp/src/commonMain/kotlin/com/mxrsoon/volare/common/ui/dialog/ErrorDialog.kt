package com.mxrsoon.volare.common.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.mxrsoon.volare.composeapp.generated.resources.Res
import com.mxrsoon.volare.composeapp.generated.resources.error_24px
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    icon: @Composable () -> Unit = { Icon(vectorResource(Res.drawable.error_24px), null) }
) {
    ConfirmationDialog(
        icon = icon,
        title = title,
        message = message,
        onDismissRequest = onDismissRequest,
        onConfirm = {},
        showDismissButton = false
    )
}