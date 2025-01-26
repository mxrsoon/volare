package com.mxrsoon.volare.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mxrsoon.volare.collection.Collection
import com.mxrsoon.volare.common.ui.dialog.ErrorDialog
import com.mxrsoon.volare.common.ui.padding.plus
import com.mxrsoon.volare.common.ui.theme.VolareTheme
import com.mxrsoon.volare.composeapp.generated.resources.Res
import com.mxrsoon.volare.composeapp.generated.resources.check_24px
import com.mxrsoon.volare.composeapp.generated.resources.collection_name
import com.mxrsoon.volare.composeapp.generated.resources.collections_label
import com.mxrsoon.volare.composeapp.generated.resources.confirm_label
import com.mxrsoon.volare.composeapp.generated.resources.create_collection_label
import com.mxrsoon.volare.composeapp.generated.resources.item_count_format
import com.mxrsoon.volare.composeapp.generated.resources.item_list_label
import com.mxrsoon.volare.composeapp.generated.resources.loading_error_message
import com.mxrsoon.volare.composeapp.generated.resources.loading_error_title
import kotlin.random.Random
import kotlin.random.nextInt
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CollectionsScreen(
    viewModel: CollectionsViewModel = viewModel { CollectionsViewModel() }
) {
    CollectionsScreen(
        uiState = viewModel.uiState,
        onCreateCollectionClick = { viewModel.showCollectionCreation() },
        onDismissErrorRequest = { viewModel.dismissActionError() },
        onDismissCollectionCreationRequest = { viewModel.dismissCollectionCreation() },
        onNewCollectionNameChange = { viewModel.setNewCollectionName(it) },
        onCreateCollectionRequest = { viewModel.createCollection() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionsScreen(
    uiState: CollectionsUiState,
    onCreateCollectionClick: () -> Unit,
    onDismissErrorRequest: () -> Unit,
    onDismissCollectionCreationRequest: () -> Unit,
    onNewCollectionNameChange: (String) -> Unit,
    onCreateCollectionRequest: () -> Unit
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
                expanded = !gridState.lastScrolledForward,
                onClick = onCreateCollectionClick
            )
        }
    ) { innerPadding ->
        val contentPadding = innerPadding + 16.dp
        val collections = uiState.collections.orEmpty()

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
                    items = collections,
                    key = { collection -> collection.id }
                ) { collection ->
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
                text = stringResource(Res.string.item_count_format, Random.nextInt(1..20)),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(Res.string.item_list_label),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                value = collectionName,
                onValueChange = onCollectionNameChange,
                label = { Text(stringResource(Res.string.collection_name)) }
            )

            IconButton(
                onClick = onConfirm,
                colors = IconButtonDefaults.filledIconButtonColors(),
                enabled = collectionName.isNotBlank()
            ) {
                Icon(painterResource(Res.drawable.check_24px), stringResource(Res.string.confirm_label))
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
                collections = listOf(
                    Collection(
                        id = "1",
                        name = "Compras",
                        creatorId = "1"
                    )
                ),
                showCollectionCreation = false
            ),
            onCreateCollectionClick = {},
            onDismissErrorRequest = {},
            onDismissCollectionCreationRequest = {},
            onNewCollectionNameChange = {},
            onCreateCollectionRequest = {}
        )
    }
}