package com.mxrsoon.volare.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.mxrsoon.volare.resources.NotoSans_Black
import com.mxrsoon.volare.resources.NotoSans_Bold
import com.mxrsoon.volare.resources.NotoSans_ExtraBold
import com.mxrsoon.volare.resources.NotoSans_ExtraLight
import com.mxrsoon.volare.resources.NotoSans_Light
import com.mxrsoon.volare.resources.NotoSans_Medium
import com.mxrsoon.volare.resources.NotoSans_Regular
import com.mxrsoon.volare.resources.NotoSans_SemiBold
import com.mxrsoon.volare.resources.NotoSans_Thin
import com.mxrsoon.volare.resources.Res
import org.jetbrains.compose.resources.Font

private val baseline = Typography()

@Composable
private fun getDefaultFontFamily() = FontFamily(
    Font(Res.font.NotoSans_Thin, FontWeight.Thin),
    Font(Res.font.NotoSans_ExtraLight, FontWeight.ExtraLight),
    Font(Res.font.NotoSans_Light, FontWeight.Light),
    Font(Res.font.NotoSans_Regular, FontWeight.Normal),
    Font(Res.font.NotoSans_Medium, FontWeight.Medium),
    Font(Res.font.NotoSans_SemiBold, FontWeight.SemiBold),
    Font(Res.font.NotoSans_Bold, FontWeight.Bold),
    Font(Res.font.NotoSans_ExtraBold, FontWeight.ExtraBold),
    Font(Res.font.NotoSans_Black, FontWeight.Black)
)

@Composable
fun getDefaultTypography(): Typography {
    val fontFamily = getDefaultFontFamily()

    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = fontFamily),
    )
}

