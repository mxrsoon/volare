package com.mxrsoon.volare.item.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mxrsoon.volare.common.datetime.format
import com.mxrsoon.volare.common.ui.button.BackButton
import com.mxrsoon.volare.common.ui.dialog.ErrorDialog
import com.mxrsoon.volare.common.ui.padding.plus
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.item.Item
import com.mxrsoon.volare.resources.Res
import com.mxrsoon.volare.resources.add_20px
import com.mxrsoon.volare.resources.add_24px
import com.mxrsoon.volare.resources.add_item_label
import com.mxrsoon.volare.resources.cancel_label
import com.mxrsoon.volare.resources.create_label
import com.mxrsoon.volare.resources.created_at_format
import com.mxrsoon.volare.resources.delete_24px
import com.mxrsoon.volare.resources.delete_label
import com.mxrsoon.volare.resources.item_count_format
import com.mxrsoon.volare.resources.item_list_label
import com.mxrsoon.volare.resources.item_name
import com.mxrsoon.volare.resources.loading_error_message
import com.mxrsoon.volare.resources.loading_error_title
import com.mxrsoon.volare.resources.more_vert_24px
import com.mxrsoon.volare.resources.open_context_menu_label
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
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
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.surfaceContainer),
                title = { Text(text = collectionName) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackButton(onClick = onBackRequest)
                }
            )
        },
        floatingActionButton = {
            if (!uiState.loading) {
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(Res.string.add_item_label)) },
                    icon = { Icon(painterResource(Res.drawable.add_24px), null) },
                    expanded = gridState.firstVisibleItemIndex == 0 || gridState.lastScrolledBackward,
                    onClick = onCreateItemClick
                )
            }
        }
    ) { innerPadding ->
        val contentPadding = innerPadding + 16.dp

        val outerContentPadding = LocalLayoutDirection.current.let { layoutDirection ->
            PaddingValues(
                top = contentPadding.calculateTopPadding(),
                start = contentPadding.calculateStartPadding(layoutDirection),
                end = contentPadding.calculateEndPadding(layoutDirection)
            )
        }

        val innerContentPadding = PaddingValues(
            bottom = contentPadding.calculateBottomPadding()
        )

        val fabPadding = PaddingValues(bottom = 72.dp)
        val entries = uiState.entries.orEmpty()
        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier
                .consumeWindowInsets(outerContentPadding)
                .padding(outerContentPadding)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            isRefreshing = uiState.refreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            contentAlignment = Alignment.Center,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = uiState.refreshing,
                    state = pullToRefreshState
                )
            }
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .consumeWindowInsets(innerContentPadding)
                    .fillMaxSize(),
                state = gridState,
                columns = StaggeredGridCells.Adaptive(240.dp),
                verticalItemSpacing = 2.dp,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = innerContentPadding + fabPadding,
                content = {
                    itemsIndexed(
                        items = entries,
                        key = { _, entry -> entry.id }
                    ) { index, entry ->
                        ItemCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .animateItem(),
                            entry = entry,
                            isFirstItem = index == 0,
                            isLastItem = index == entries.lastIndex,
                            onDeleteClick = { onDeleteItemClick(entry) }
                        )
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = uiState.loading,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                LoadingIndicator(Modifier.size(72.dp))
            }
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

@OptIn(ExperimentalTime::class)
@Composable
private fun ItemCard(
    entry: Item,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false
) {
    var showContextMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        onClick = {},
        shape = when {
            isFirstItem && isLastItem -> RoundedCornerShape(24.dp)

            isFirstItem -> RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = 8.dp,
                bottomEnd = 8.dp
            )

            isLastItem -> RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomStart = 24.dp,
                bottomEnd = 24.dp
            )

            else -> RoundedCornerShape(8.dp)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        )
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
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
                        onClick = {
                            onDeleteClick()
                            showContextMenu = false
                        }
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

@OptIn(ExperimentalTime::class)
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

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun ItemsScreenLoadingPreview() {
    VolareTheme(
        platformColorScheme = false,
        darkMode = true
    ) {
        ItemsScreen(
            collectionName = "Contas a pagar",
            uiState = ItemsUiState(
                loading = true,
                entries = null,
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

@OptIn(ExperimentalTime::class)
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