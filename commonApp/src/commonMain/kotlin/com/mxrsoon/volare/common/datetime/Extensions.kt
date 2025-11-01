package com.mxrsoon.volare.common.datetime

import kotlinx.datetime.LocalDateTime

/**
 * Format the date and time in a human-readable format, using the preferred format for the user's locale.
 */
fun LocalDateTime.format(): String = DateTimeFormatHelper.format(this)
