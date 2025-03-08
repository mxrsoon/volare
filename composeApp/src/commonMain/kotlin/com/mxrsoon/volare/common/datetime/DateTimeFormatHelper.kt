package com.mxrsoon.volare.common.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

/**
 * Helper class to format dates and times in a human-readable format.
 */
expect object DateTimeFormatHelper {

    /**
     * Format the date in a human-readable format, using the preferred format for the user's locale.
     */
    fun format(date: LocalDate): String

    /**
     * Format the date and time in a human-readable format, using the preferred format for the user's locale.
     */
    fun format(dateTime: LocalDateTime): String
}