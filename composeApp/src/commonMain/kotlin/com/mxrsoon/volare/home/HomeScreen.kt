package com.mxrsoon.volare.home

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mxrsoon.volare.composeapp.generated.resources.Res
import com.mxrsoon.volare.composeapp.generated.resources.collections_label
import com.mxrsoon.volare.composeapp.generated.resources.home_24px
import com.mxrsoon.volare.composeapp.generated.resources.home_label
import com.mxrsoon.volare.composeapp.generated.resources.search_24px
import com.mxrsoon.volare.composeapp.generated.resources.search_label
import com.mxrsoon.volare.composeapp.generated.resources.stacks_24px
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class Page(val label: StringResource, val icon: DrawableResource) {
    Home(Res.string.home_label, Res.drawable.home_24px),
    Search(Res.string.search_label, Res.drawable.search_24px),
    Collections(Res.string.collections_label, Res.drawable.stacks_24px)
}

@Composable
fun HomeScreen() {
    var selectedPage by remember { mutableStateOf(Page.Home) }

    Scaffold(
        modifier = Modifier.imePadding(),
        bottomBar = {
            NavigationBar {
                Page.entries.forEach { page ->
                    NavigationBarItem(
                        selected = selectedPage == page,
                        onClick = { selectedPage = page },
                        icon = { Icon(painter = painterResource(page.icon), contentDescription = null) },
                        label = { Text(text = stringResource(page.label)) }
                    )
                }
            }
        }
    ) { paddingValues ->

    }
}