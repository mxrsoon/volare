package com.mxrsoon.volare.common.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDateComponentsFormatter
import platform.Foundation.NSFormattingUnitStyleShort

/**
 * Helper for formatting dates and times.
 */
actual object DateTimeFormatHelper {

    /**
     * Format the date in a human-readable format, using the preferred format for the user's locale.
     */
    actual fun format(date: LocalDate): String {
        val dateComponents = date.toNSDateComponents()
        val formatter = NSDateComponentsFormatter()

        formatter.allowedUnits = NSCalendarUnitDay or NSCalendarUnitMonth or NSCalendarUnitYear
        formatter.unitsStyle = NSFormattingUnitStyleShort

        return formatter.stringFromDateComponents(dateComponents)
            ?: throw IllegalStateException("Failed to format date")
    }

    /**
     * Format the date and time in a human-readable format, using the preferred format for the user's locale.
     */
    actual fun format(dateTime: LocalDateTime): String {
        val dateComponents = dateTime.toNSDateComponents()
        val formatter = NSDateComponentsFormatter()

        formatter.allowedUnits = NSCalendarUnitDay or NSCalendarUnitMonth or NSCalendarUnitYear or
                NSCalendarUnitHour or NSCalendarUnitMinute

        formatter.unitsStyle = NSFormattingUnitStyleShort

        return formatter.stringFromDateComponents(dateComponents)
            ?: throw IllegalStateException("Failed to format date and time")
    }
}