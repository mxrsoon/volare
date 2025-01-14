package com.mxrsoon.volare.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.mxrsoon.volare.collections.Collection
import com.mxrsoon.volare.collections.CollectionType
import com.mxrsoon.volare.collections.collectionsMock
import com.mxrsoon.volare.common.ui.padding.plus
import com.mxrsoon.volare.composeapp.generated.resources.Res
import com.mxrsoon.volare.composeapp.generated.resources.item_count_format
import com.mxrsoon.volare.composeapp.generated.resources.item_list_label
import com.mxrsoon.volare.composeapp.generated.resources.search_label
import com.mxrsoon.volare.composeapp.generated.resources.task_list_label
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                        )
                    ),
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(stringResource(Res.string.search_label)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                val suggestions = collectionsMock
                    .filter { it.name.contains(query.trim(), ignoreCase = true) }
                    .sortedBy { it.name.indexOf(query, ignoreCase = true) }

                items(suggestions) { item ->
                    ListItem(
                        modifier = Modifier
                            .clickable { expanded = false }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text(item.type.getLabel()) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }

        val contentPadding = PaddingValues(
            start = 16.dp,
            top = 72.dp,
            end = 16.dp,
            bottom = 16.dp
        ) + WindowInsets.safeDrawing.asPaddingValues()

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .consumeWindowInsets(contentPadding)
                .fillMaxSize()
                .semantics { traversalIndex = 1f },
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 72.dp) + 16.dp +
                WindowInsets.safeDrawing.asPaddingValues(),
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