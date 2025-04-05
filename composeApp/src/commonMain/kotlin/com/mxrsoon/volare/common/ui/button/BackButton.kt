package com.mxrsoon.volare.common.ui.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mxrsoon.volare.resources.Res
import com.mxrsoon.volare.resources.arrow_back_24px
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(vectorResource(Res.drawable.arrow_back_24px), null)
    }
}