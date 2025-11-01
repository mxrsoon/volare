package com.mxrsoon.volare.common.ui.padding

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Adds two [PaddingValues] together.
 */
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(LayoutDirection.Ltr) +
            other.calculateStartPadding(LayoutDirection.Ltr),
    top = this.calculateTopPadding() + other.calculateTopPadding(),
    end = this.calculateEndPadding(LayoutDirection.Ltr) +
            other.calculateEndPadding(LayoutDirection.Ltr),
    bottom = this.calculateBottomPadding() + other.calculateBottomPadding()
)

/**
 * Adds a [Dp] to all sides of the [PaddingValues].
 */
operator fun PaddingValues.plus(other: Dp): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(LayoutDirection.Ltr) + other,
    top = this.calculateTopPadding() + other,
    end = this.calculateEndPadding(LayoutDirection.Ltr) + other,
    bottom = this.calculateBottomPadding() + other
)