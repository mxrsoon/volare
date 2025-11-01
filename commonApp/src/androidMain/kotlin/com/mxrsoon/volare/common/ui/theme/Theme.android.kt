package com.mxrsoon.volare.common.ui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getPlatformColorScheme(darkMode: Boolean): ColorScheme? {
    val supportsDynamicColors = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    return when {
        supportsDynamicColors && darkMode -> dynamicDarkColorScheme(LocalContext.current)
        supportsDynamicColors && !darkMode -> dynamicLightColorScheme(LocalContext.current)
        else -> null
    }
}