package com.mxrsoon.volare.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mxrsoon.volare.common.ui.theme.VolareTheme

@Composable
fun HomeScreen(
    onSignOut: () -> Unit
) {
    Scaffold(Modifier.imePadding()) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = onSignOut
            ) {
                Text("Sign out")
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenDarkPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        HomeScreen(onSignOut = {})
    }
}

@Preview
@Composable
private fun HomeScreenLightPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = false
    ) {
        HomeScreen(onSignOut = {})
    }
}