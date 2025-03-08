package com.mxrsoon.volare.item.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.mxrsoon.volare.common.datetime.format
import com.mxrsoon.volare.common.ui.dialog.ErrorDialog
import com.mxrsoon.volare.common.ui.padding.plus
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.composeapp.generated.resources.Res
import com.mxrsoon.volare.composeapp.generated.resources.add_20px
import com.mxrsoon.volare.composeapp.generated.resources.add_item_label
import com.mxrsoon.volare.composeapp.generated.resources.arrow_back_24px
import com.mxrsoon.volare.composeapp.generated.resources.back_label
import com.mxrsoon.volare.composeapp.generated.resources.cancel_label
import com.mxrsoon.volare.composeapp.generated.resources.create_label
import com.mxrsoon.volare.composeapp.generated.resources.created_at_format
import com.mxrsoon.volare.composeapp.generated.resources.delete_24px
import com.mxrsoon.volare.composeapp.generated.resources.delete_label
import com.mxrsoon.volare.composeapp.generated.resources.item_name
import com.mxrsoon.volare.composeapp.generated.resources.loading_error_message
import com.mxrsoon.volare.composeapp.generated.resources.loading_error_title
import com.mxrsoon.volare.composeapp.generated.resources.more_vert_24px
import com.mxrsoon.volare.composeapp.generated.resources.open_context_menu_label
import com.mxrsoon.volare.item.Item
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ItemsScreen(
    collectionId: String,
    collectionName: String,
    onBackRequest: () -> Unit,
    viewModel: ItemsViewModel = viewModel { ItemsViewModel() }
) {
    LaunchedEffect(collectionId) {
        viewModel.initialize(collectionId)
    }

    ItemsScreen(
        collectionName = collectionName,
        uiState = viewModel.uiState,
        onBackRequest = onBackRequest,
        onCreateItemClick = { viewModel.showItemCreation() },
        onDismissErrorRequest = { viewModel.dismissActionError() },
        onDismissItemCreationRequest = { viewModel.dismissItemCreation() },
        onNewItemNameChange = { viewModel.setNewItemName(it) },
        onCreateItemRequest = { viewModel.createItem() },
        onDeleteItemClick = { viewModel.deleteItem(it) },
        onRefresh = { viewModel.refresh() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemsScreen(
    collectionName: String,
    uiState: ItemsUiState,
    onBackRequest: () -> Unit,
    onCreateItemClick: () -> Unit,
    onDismissErrorRequest: () -> Unit,
    onDismissItemCreationRequest: () -> Unit,
    onNewItemNameChange: (String) -> Unit,
    onCreateItemRequest: () -> Unit,
    onDeleteItemClick: (Item) -> Unit,
    onRefresh: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyStaggeredGridState()
    val newItemSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = collectionName) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBackRequest) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back_24px),
                            contentDescription = stringResource(Res.string.back_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(Res.string.add_item_label)) },
                icon = { Icon(Icons.Outlined.Add, null) },
                expanded = gridState.firstVisibleItemIndex == 0 || gridState.lastScrolledBackward,
                onClick = onCreateItemClick
            )
        }
    ) { innerPadding ->
        val contentPadding = innerPadding + 16.dp
        val fabPadding = PaddingValues(bottom = 72.dp)
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
                contentPadding = contentPadding + fabPadding,
                content = {
                    items(
                        items = entries,
                        key = { entry -> entry.id }
                    ) { entry ->
                        ItemCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            entry = entry,
                            onDeleteClick = { onDeleteItemClick(entry) }
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

    if (uiState.showItemCreation) {
        CreateItemSheet(
            sheetState = newItemSheetState,
            itemName = uiState.newItemName,
            onItemNameChange = onNewItemNameChange,
            onDismissRequest = onDismissItemCreationRequest,
            onConfirm = onCreateItemRequest
        )
    }
}

@Composable
private fun ItemCard(
    entry: Item,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showContextMenu by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier,
        onClick = {}
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
                    text = entry.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = stringResource(
                        Res.string.created_at_format,
                        entry.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).format()
                    ),
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
private fun CreateItemSheet(
    sheetState: SheetState,
    itemName: String,
    onItemNameChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        CreateItemSheetContents(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            itemName = itemName,
            onItemNameChange = onItemNameChange,
            onDismissRequest = onDismissRequest,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun CreateItemSheetContents(
    itemName: String,
    onItemNameChange: (String) -> Unit,
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
            value = itemName,
            onValueChange = onItemNameChange,
            label = { Text(stringResource(Res.string.item_name)) }
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
                enabled = itemName.isNotBlank(),
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
private fun ItemsScreenPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        ItemsScreen(
            collectionName = "Contas a pagar",
            uiState = ItemsUiState(
                loading = false,
                entries = listOf(
                    Item(
                        id = "1",
                        name = "Apartamento",
                        creatorId = "1",
                        collectionId = "",
                        createdAt = Clock.System.now().minus(1, DateTimeUnit.YEAR, TimeZone.currentSystemDefault()),
                        url = null
                    ),
                    Item(
                        id = "2",
                        name = "Energia",
                        creatorId = "1",
                        collectionId = "",
                        createdAt = Clock.System.now().minus(5, DateTimeUnit.MONTH, TimeZone.currentSystemDefault()),
                        url = null
                    ),
                    Item(
                        id = "3",
                        name = "Cartões",
                        creatorId = "1",
                        collectionId = "",
                        createdAt = Clock.System.now(),
                        url = null
                    )
                ),
                showItemCreation = false
            ),
            onBackRequest = {},
            onCreateItemClick = {},
            onDismissErrorRequest = {},
            onDismissItemCreationRequest = {},
            onNewItemNameChange = {},
            onCreateItemRequest = {},
            onDeleteItemClick = {},
            onRefresh = {}
        )
    }
}

@Preview
@Composable
private fun CreateItemSheetContentsPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        Box(Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
            CreateItemSheetContents(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 24.dp),
                itemName = "Apartamento",
                onItemNameChange = {},
                onDismissRequest = {},
                onConfirm = {}
            )
        }
    }
}

@Preview
@Composable
private fun ItemListItemPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        ItemCard(
            entry = Item(
                id = "1",
                name = "Apartamento",
                creatorId = "1",
                collectionId = "",
                createdAt = Clock.System.now(),
                url = null
            ),
            onDeleteClick = {}
        )
    }
}