package org.abimon.visi.nums

import org.junit.Test

internal class SuperLongTest {
    @Test
    fun superLong() {
        var deckSize = 40
        val chanceOfExodia = SuperLong(deckSize--) * deckSize-- * deckSize-- * deckSize-- * deckSize
        println(chanceOfExodia)
    }
}