package com.mxrsoon.volare.collections

import kotlin.random.Random
import kotlin.random.nextInt
import kotlinx.serialization.Serializable

/**
 * Represents a collection.
 */
@Serializable
data class Collection(
    val name: String,
    val type: CollectionType,
    val count: Int
)

/* TODO: Remove mock data. */
val collectionsMock: List<Collection> = listOf(
    Collection(name = "Anotações", type = CollectionType.ItemList, count = Random.nextInt(2..18)),
    Collection(name = "Compras", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Filmes", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Lembretes", type = CollectionType.ItemList, count = Random.nextInt(2..18)),
    Collection(name = "Livros", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Jogos", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Anotações", type = CollectionType.ItemList, count = Random.nextInt(2..18)),
    Collection(name = "Compras", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Filmes", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Lembretes", type = CollectionType.ItemList, count = Random.nextInt(2..18)),
    Collection(name = "Livros", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Jogos", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Anotações", type = CollectionType.ItemList, count = Random.nextInt(2..18)),
    Collection(name = "Compras", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Filmes", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Lembretes", type = CollectionType.ItemList, count = Random.nextInt(2..18)),
    Collection(name = "Livros", type = CollectionType.TaskList, count = Random.nextInt(2..18)),
    Collection(name = "Jogos", type = CollectionType.TaskList, count = Random.nextInt(2..18))
)