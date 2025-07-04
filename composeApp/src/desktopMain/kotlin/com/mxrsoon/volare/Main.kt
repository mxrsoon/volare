package com.mxrsoon.volare

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mxrsoon.volare.resources.Res
import com.mxrsoon.volare.resources.app_name
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name)
    ) {
        App()
    }
}