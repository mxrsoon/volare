package com.mxrsoon.volare.collection

import org.jetbrains.exposed.sql.ResultRow

/**
 * Converts this result row to a collection.
 */
fun ResultRow.toCollection() = Collection(
    id = this[Collections.id].toString(),
    name = this[Collections.name],
    creatorId = this[Collections.creatorId].toString()
)