package com.mxrsoon.volare.common.datetime

import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.autoupdatingCurrentLocale

/**
 * Helper for formatting dates and times.
 */
actual object DateTimeFormatHelper {

    /**
     * Format the date and time in a human-readable format, using the preferred format for the user's locale.
     */
    @OptIn(ExperimentalTime::class)
    actual fun format(dateTime: LocalDateTime): String {
        val platformDate = dateTime.toInstant(TimeZone.currentSystemDefault()).toNSDate()
        val formatter = NSDateFormatter()

        formatter.dateStyle = NSDateFormatterShortStyle
        formatter.timeStyle = NSDateFormatterShortStyle
        formatter.locale = NSLocale.autoupdatingCurrentLocale

        return formatter.stringFromDate(platformDate)
    }
}