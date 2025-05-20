package com.mxrsoon.volare.common.ui.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mxrsoon.volare.resources.Res
import com.mxrsoon.volare.resources.arrow_back_24px
import com.mxrsoon.volare.resources.back_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(Res.drawable.arrow_back_24px),
            contentDescription = stringResource(Res.string.back_label)
        )
    }
}