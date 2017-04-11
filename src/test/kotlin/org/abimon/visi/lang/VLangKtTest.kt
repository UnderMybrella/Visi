package org.abimon.visi.lang

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

internal class VLangKtTest {
    @Test
    fun plural() {
        val singular = "apple"
        val plural = "apples"

        val testing = mapOf(
                Pair(0, plural),
                Pair(1, singular),
                Pair(2, plural),
                Pair(Int.MAX_VALUE, plural)
        )

        for((num, expected) in testing)
            assertEquals("$num $expected", num.plural(singular, plural))
    }

    @Test
    fun orElseMaybe() {
        assertEquals("ABCDEFG", Optional.empty<String>().orElseMaybe { Optional.of("ABCDEFG") }.get())
    }
}