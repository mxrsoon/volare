package com.mxrsoon.volare.collection.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mxrsoon.volare.collection.Collection
import com.mxrsoon.volare.collection.CollectionListEntry
import com.mxrsoon.volare.common.ui.dialog.ErrorDialog
import com.mxrsoon.volare.common.ui.padding.plus
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.composeapp.generated.resources.Res
import com.mxrsoon.volare.composeapp.generated.resources.add_20px
import com.mxrsoon.volare.composeapp.generated.resources.cancel_label
import com.mxrsoon.volare.composeapp.generated.resources.collection_name
import com.mxrsoon.volare.composeapp.generated.resources.collections_label
import com.mxrsoon.volare.composeapp.generated.resources.create_collection_label
import com.mxrsoon.volare.composeapp.generated.resources.create_label
import com.mxrsoon.volare.composeapp.generated.resources.delete_24px
import com.mxrsoon.volare.composeapp.generated.resources.delete_label
import com.mxrsoon.volare.composeapp.generated.resources.item_count_format
import com.mxrsoon.volare.composeapp.generated.resources.item_list_label
import com.mxrsoon.volare.composeapp.generated.resources.loading_error_message
import com.mxrsoon.volare.composeapp.generated.resources.loading_error_title
import com.mxrsoon.volare.composeapp.generated.resources.more_vert_24px
import com.mxrsoon.volare.composeapp.generated.resources.open_context_menu_label
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CollectionsScreen(
    onCollectionClick: (Collection) -> Unit,
    viewModel: CollectionsViewModel = viewModel { CollectionsViewModel() }
) {
    CollectionsScreen(
        uiState = viewModel.uiState,
        onCollectionClick = onCollectionClick,
        onCreateCollectionClick = { viewModel.showCollectionCreation() },
        onDismissErrorRequest = { viewModel.dismissActionError() },
        onDismissCollectionCreationRequest = { viewModel.dismissCollectionCreation() },
        onNewCollectionNameChange = { viewModel.setNewCollectionName(it) },
        onCreateCollectionRequest = { viewModel.createCollection() },
        onDeleteCollectionClick = { viewModel.deleteCollection(it) },
        onRefresh = { viewModel.refresh() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionsScreen(
    uiState: CollectionsUiState,
    onCollectionClick: (Collection) -> Unit,
    onCreateCollectionClick: () -> Unit,
    onDismissErrorRequest: () -> Unit,
    onDismissCollectionCreationRequest: () -> Unit,
    onNewCollectionNameChange: (String) -> Unit,
    onCreateCollectionRequest: () -> Unit,
    onDeleteCollectionClick: (Collection) -> Unit,
    onRefresh: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyStaggeredGridState()
    val newCollectionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.collections_label)) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(Res.string.create_collection_label)) },
                icon = { Icon(Icons.Outlined.Add, null) },
                expanded = gridState.firstVisibleItemIndex == 0 || gridState.lastScrolledBackward,
                onClick = onCreateCollectionClick
            )
        }
    ) { innerPadding ->
        val contentPadding = innerPadding + 16.dp
        val entries = uiState.entries.orEmpty()
        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            isRefreshing = uiState.refreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(innerPadding),
                    isRefreshing = uiState.refreshing,
                    state = pullToRefreshState
                )
            }
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .consumeWindowInsets(contentPadding)
                    .fillMaxSize(),
                state = gridState,
                columns = StaggeredGridCells.Adaptive(240.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = contentPadding,
                content = {
                    items(
                        items = entries,
                        key = { entry -> entry.collection.id }
                    ) { entry ->
                        CollectionCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            entry = entry,
                            onClick = { onCollectionClick(entry.collection) },
                            onDeleteClick = { onDeleteCollectionClick(entry.collection) }
                        )
                    }
                }
            )
        }
    }

    if (uiState.actionError) {
        ErrorDialog(
            title = stringResource(Res.string.loading_error_title),
            message = stringResource(Res.string.loading_error_message),
            onDismissRequest = onDismissErrorRequest
        )
    }

    if (uiState.showCollectionCreation) {
        CreateCollectionSheet(
            sheetState = newCollectionSheetState,
            collectionName = uiState.newCollectionName,
            onCollectionNameChange = onNewCollectionNameChange,
            onDismissRequest = onDismissCollectionCreationRequest,
            onConfirm = onCreateCollectionRequest
        )
    }
}

@Composable
private fun CollectionCard(
    entry: CollectionListEntry,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showContextMenu by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = entry.collection.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(Res.string.item_count_format, entry.itemCount),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(Res.string.item_list_label),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box {
                IconButton(onClick = { showContextMenu = !showContextMenu }) {
                    Icon(
                        painter = painterResource(Res.drawable.more_vert_24px),
                        contentDescription = stringResource(Res.string.open_context_menu_label)
                    )
                }

                DropdownMenu(
                    expanded = showContextMenu,
                    onDismissRequest = { showContextMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.delete_label)) },
                        leadingIcon = { Icon(painterResource(Res.drawable.delete_24px), null) },
                        onClick = onDeleteClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateCollectionSheet(
    sheetState: SheetState,
    collectionName: String,
    onCollectionNameChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        CreateCollectionSheetContents(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            collectionName = collectionName,
            onCollectionNameChange = onCollectionNameChange,
            onDismissRequest = onDismissRequest,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun CreateCollectionSheetContents(
    collectionName: String,
    onCollectionNameChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    
    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = collectionName,
            onValueChange = onCollectionNameChange,
            label = { Text(stringResource(Res.string.collection_name)) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel_label))
            }

            Button(
                onClick = onConfirm,
                enabled = collectionName.isNotBlank(),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(Res.drawable.add_20px),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource(Res.string.create_label)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CollectionsScreenPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        CollectionsScreen(
            uiState = CollectionsUiState(
                loading = false,
                entries = listOf(
                    CollectionListEntry(
                        collection = Collection(
                            id = "1",
                            name = "Compras",
                            creatorId = "1",
                            createdAt = Clock.System.now()
                        ),
                        itemCount = 5
                    )
                ),
                showCollectionCreation = false
            ),
            onCollectionClick = {},
            onCreateCollectionClick = {},
            onDismissErrorRequest = {},
            onDismissCollectionCreationRequest = {},
            onNewCollectionNameChange = {},
            onCreateCollectionRequest = {},
            onDeleteCollectionClick = {},
            onRefresh = {}
        )
    }
}

@Preview
@Composable
private fun CreateCollectionSheetContentsPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        Box(Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
            CreateCollectionSheetContents(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 24.dp),
                collectionName = "Compras",
                onCollectionNameChange = {},
                onDismissRequest = {},
                onConfirm = {}
            )
        }
    }
}

@Preview
@Composable
private fun CollectionListItemPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        CollectionCard(
            entry = CollectionListEntry(
                collection = Collection(
                    id = "1",
                    name = "Compras",
                    creatorId = "1",
                    createdAt = Clock.System.now()
                ),
                itemCount = 5
            ),
            onClick = {},
            onDeleteClick = {}
        )
    }
}