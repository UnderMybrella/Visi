package org.abimon.visi.nums

import org.junit.Test

internal class SuperLongTest {
    @Test
    fun superLong() {
        println(Long.MAX_VALUE)
        println(SuperLong.MAX_VALUE)
    }
}