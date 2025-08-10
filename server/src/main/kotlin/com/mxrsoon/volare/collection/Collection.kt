package com.mxrsoon.volare.collection

import kotlin.time.ExperimentalTime
import org.jetbrains.exposed.v1.core.ResultRow

/**
 * Converts this result row to a [Collection].
 */
@OptIn(ExperimentalTime::class)
fun ResultRow.toCollection() = Collection(
    id = this[Collections.id].toString(),
    name = this[Collections.name],
    creatorId = this[Collections.creatorId].toString(),
    createdAt = this[Collections.createdAt]
)