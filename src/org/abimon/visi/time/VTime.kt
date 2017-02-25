package org.abimon.visi.time

import org.abimon.visi.lang.numberOfOccurences
import java.time.*

data class TimeDifference(val years: Long, val months: Long, val days: Long, val hours: Long, val minutes: Long, val seconds: Long) {
    constructor(period: Period, duration: Duration): this(period.years.toLong(), period.months.toLong(), period.days.toLong(), duration.toHours() % 24, duration.toMinutes() % 60, duration.seconds % 60)
    override fun toString(): String = format()

    fun format(doYears: Boolean = true, doMonths: Boolean = true, doDays: Boolean = true, doHours: Boolean = true, doMinutes: Boolean = true, doSeconds: Boolean = true): String {
        var str = ""
        if(years > 0 && doYears)
            str += "$years year${if(years == 1L) "" else "s"}, "
        if(months > 0 && doMonths)
            str += "$months month${if(months == 1L) "" else "s"}, "
        if(days > 0 && doDays)
            str += "$days day${if(days == 1L) "" else "s"}, "
        if(hours > 0 && doHours)
            str += "$hours hour${if(hours == 1L) "" else "s"}, "
        if(minutes > 0 && doMinutes)
            str += "$minutes minute${if(minutes == 1L) "" else "s"}, "
        if(seconds > 0 && doSeconds)
            str += "$seconds second${if(seconds == 1L) "" else "s"}, "
        if(str.numberOfOccurences(", ") == 1)
            return str.substringBeforeLast(", ")
        return "${str.substringBeforeLast(", ").substringBeforeLast(", ")} and ${str.substringBeforeLast(", ").substringAfterLast(", ")}"
    }
}

fun LocalDateTime.timeDifference(): TimeDifference = TimeDifference(Period.between(this.toLocalDate(), LocalDate.now()), Duration.between(this.toLocalTime(), LocalTime.now()))