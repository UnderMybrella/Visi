package org.abimon.visi.nums

import org.junit.Test

internal class SuperLongTest {
    @Test
    fun superLong() {
        println(SuperLong(Long.MAX_VALUE) + Int.MAX_VALUE + Short.MAX_VALUE)
    }
}