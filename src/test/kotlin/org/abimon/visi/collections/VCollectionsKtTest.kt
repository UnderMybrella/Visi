package org.abimon.visi.collections

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class VCollectionsKtTest {

    @Test
    fun toArrayString() {
        assertEquals("[A, B, C, D]", arrayOf("A", "B", "C", "D").toArrayString())
    }

    @Test
    fun copyFrom() {
        assertArrayEquals(intArrayOf(3, 4), intArrayOf(1, 2, 3, 4).copyFrom(2))
        assertArrayEquals(arrayOf("B", "C", "D"), arrayOf("1", "C", "D", "99", "B", "C", "D").copyFrom(4))
    }

    @Test
    fun toSequentialString() {
        assertEquals("1, 2, and 3", arrayOf(1, 2, 3).toSequentialString())
        assertEquals("1, 2, and 3", listOf(1, 2, 3).toSequentialString())
    }
}