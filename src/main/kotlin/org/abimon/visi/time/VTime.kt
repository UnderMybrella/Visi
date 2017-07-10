package org.abimon.visi.time

import org.abimon.visi.collections.toSequentialString
import org.abimon.visi.lang.pluralise
import java.time.*

data class TimeDifference(val years: Long, val months: Long, val days: Long, val hours: Long, val minutes: Long, val seconds: Long) {
    constructor(period: Period, duration: Duration): this(period.years.toLong(), period.months.toLong(), period.days.toLong(), duration.toHours() % 24, duration.toMinutes() % 60, duration.seconds % 60)
    override fun toString(): String = format()

    fun format(doYears: Boolean = true, doMonths: Boolean = true, doDays: Boolean = true, doHours: Boolean = true, doMinutes: Boolean = true, doSeconds: Boolean = true): String {
        val components = ArrayList<String>()
        if(years > 0 && doYears)
            components.add(years.pluralise("year"))
        if(months > 0 && doMonths)
            components.add(months.pluralise("month"))
        if(days > 0 && doDays)
            components.add(days.pluralise("day"))
        if(hours > 0 && doHours)
            components.add(hours.pluralise("hour"))
        if(minutes > 0 && doMinutes)
            components.add(minutes.pluralise("minute"))
        if(seconds > 0 && doSeconds)
            components.add(seconds.pluralise("second"))
        return components.toSequentialString(", ", " and ")
    }
}

fun LocalDateTime.timeDifference(): TimeDifference = TimeDifference(Period.between(this.toLocalDate(), LocalDate.now()), Duration.between(this.toLocalTime(), LocalTime.now()))