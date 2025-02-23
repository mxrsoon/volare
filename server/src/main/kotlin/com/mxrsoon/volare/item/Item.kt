package com.mxrsoon.volare.item

import org.jetbrains.exposed.sql.ResultRow

/**
 * Converts this result row to an [Item].
 */
fun ResultRow.toItem(): Item = Item(
    id = this[Items.id].toString(),
    name = this[Items.name],
    creatorId = this[Items.creatorId].toString(),
    collectionId = this[Items.collectionId].toString(),
    createdAt = this[Items.createdAt],
    url = this[Items.url]
)