package com.mxrsoon.volare.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.mxrsoon.volare.common.ui.padding.plus
import com.mxrsoon.volare.composeapp.generated.resources.Res
import com.mxrsoon.volare.composeapp.generated.resources.collections_label
import com.mxrsoon.volare.composeapp.generated.resources.item_count_format
import com.mxrsoon.volare.composeapp.generated.resources.item_list_label
import com.mxrsoon.volare.composeapp.generated.resources.task_list_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(Res.string.collections_label)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        val contentPadding = innerPadding + 24.dp

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .consumeWindowInsets(contentPadding)
                .fillMaxSize(),
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding,
            content = {
                items(collectionsMock) { collection ->
                    CollectionListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        item = collection
                    )
                }
            }
        )
    }
}

@Composable
private fun CollectionListItem(
    item: Collection,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        onClick = {}
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stringResource(Res.string.item_count_format, item.count),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = item.type.getLabel(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CollectionType.getLabel(): String = when (this) {
    CollectionType.ItemList -> stringResource(Res.string.item_list_label)
    CollectionType.TaskList -> stringResource(Res.string.task_list_label)
}