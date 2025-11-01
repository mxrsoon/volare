package com.mxrsoon.volare.common.datetime

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime

/**
 * Helper for formatting dates and times.
 */
actual object DateTimeFormatHelper {

    /**
     * Format the date and time in a human-readable format, using the preferred format for the user's locale.
     */
    actual fun format(dateTime: LocalDateTime): String =
        DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .format(dateTime.toJavaLocalDateTime())
}