package com.mxrsoon.volare.common.ui.dialog

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

/**
 * Intercepts the enter key press and triggers the provided action.
 */
fun Modifier.onEnterKeyDown(action: () -> Unit): Modifier =
    this.onPreviewKeyEvent { event ->
        if (event.key.keyCode == Key.Enter.keyCode && event.type == KeyEventType.KeyDown) {
            action()
            true
        } else {
            false
        }
    }