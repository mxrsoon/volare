package com.mxrsoon.volare.common.datetime

import kotlinx.datetime.LocalDateTime

/**
 * Helper for formatting dates and times.
 */
actual object DateTimeFormatHelper {

    /**
     * Format the date and time in a human-readable format, using the preferred format for the user's locale.
     */
    actual fun format(dateTime: LocalDateTime): String {
        // TODO: Implement proper locale-based formatting for web
        return dateTime.toString()
    }
}