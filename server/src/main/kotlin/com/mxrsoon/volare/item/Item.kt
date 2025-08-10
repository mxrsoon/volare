package com.mxrsoon.volare.item

import kotlin.time.ExperimentalTime
import org.jetbrains.exposed.v1.core.ResultRow

/**
 * Converts this result row to an [Item].
 */
@OptIn(ExperimentalTime::class)
fun ResultRow.toItem(): Item = Item(
    id = this[Items.id].toString(),
    name = this[Items.name],
    creatorId = this[Items.creatorId].toString(),
    collectionId = this[Items.collectionId].toString(),
    createdAt = this[Items.createdAt],
    url = this[Items.url]
)