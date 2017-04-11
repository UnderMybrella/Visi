package org.abimon.visi.lang

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
            Assertions.assertEquals(num.plural(singular, plural), "$num $expected")
    }
}