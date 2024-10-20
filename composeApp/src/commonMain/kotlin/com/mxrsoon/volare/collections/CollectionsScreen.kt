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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mxrsoon.volare.common.ui.padding.plus
import kotlinx.serialization.Serializable

@Serializable
data class Collection(val name: String)

private val collections: List<Collection> = listOf(
    Collection("Lista de compras"),
    Collection("Filmes"),
    Collection("Livros"),
    Collection("Jogos"),
    Collection("Projetos"),
    Collection("Tarefas"),
    Collection("Lista de compras"),
    Collection("Filmes"),
    Collection("Livros"),
    Collection("Jogos"),
    Collection("Projetos"),
    Collection("Tarefas"),
    Collection("Lista de compras"),
    Collection("Filmes"),
    Collection("Livros"),
    Collection("Jogos"),
    Collection("Projetos"),
    Collection("Tarefas"),
    Collection("Lista de compras"),
    Collection("Filmes"),
    Collection("Livros"),
    Collection("Jogos"),
    Collection("Projetos"),
    Collection("Tarefas")
)

@Composable
fun CollectionsScreen() {
    Scaffold(Modifier.imePadding()) { innerPadding ->
        val contentPadding = innerPadding + 24.dp

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .consumeWindowInsets(contentPadding)
                .fillMaxSize(),
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding,
            content = {
                items(collections) { collection ->
                    CollectionListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        name = collection.name
                    )
                }
            }
        )
    }
}

@Composable
private fun CollectionListItem(
    name: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}