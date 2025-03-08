package com.mxrsoon.volare.common.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.autoupdatingCurrentLocale

/**
 * Helper for formatting dates and times.
 */
actual object DateTimeFormatHelper {

    /**
     * Format the date in a human-readable format, using the preferred format for the user's locale.
     */
    actual fun format(date: LocalDate): String {
        val platformDate = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toNSDate()
        val formatter = NSDateFormatter()

        formatter.dateStyle = NSDateFormatterShortStyle
        formatter.timeStyle = NSDateFormatterNoStyle
        formatter.locale = NSLocale.autoupdatingCurrentLocale

        return formatter.stringFromDate(platformDate)
    }

    /**
     * Format the date and time in a human-readable format, using the preferred format for the user's locale.
     */
    actual fun format(dateTime: LocalDateTime): String {
        val platformDate = dateTime.toInstant(TimeZone.currentSystemDefault()).toNSDate()
        val formatter = NSDateFormatter()

        formatter.dateStyle = NSDateFormatterShortStyle
        formatter.timeStyle = NSDateFormatterShortStyle
        formatter.locale = NSLocale.autoupdatingCurrentLocale

        return formatter.stringFromDate(platformDate)
    }
}