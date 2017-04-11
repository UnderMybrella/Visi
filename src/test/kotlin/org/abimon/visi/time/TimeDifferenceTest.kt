package org.abimon.visi.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.Period

internal class TimeDifferenceTest {
    @Test
    fun format() {
        assertEquals("1 day and 1 minute", TimeDifference(Period.ofDays(1), Duration.ofMinutes(1)).format())
        assertEquals("2 days", TimeDifference(Period.ofDays(2), Duration.ZERO).format())
    }
}