package org.abimon.visi.nums

import org.junit.Test
import java.math.BigInteger
import java.util.*
import kotlin.system.measureTimeMillis

internal class SuperLongTest {
    @Test
    fun superLong() {
        val rng = Random()
        val randomTimes = (1 until 16)//.map { rng.nextInt(Math.abs(it) + 64) }
        val starting = 128L

        var superLong = SuperLong(starting)
        val superLongTime = measureTimeMillis { randomTimes.forEach { superLong *= it } }

        var bigInt = BigInteger.valueOf(starting)
        val bigIntTime = measureTimeMillis { randomTimes.forEach { bigInt *= BigInteger.valueOf(it.toLong()) } }

        println("Starting at $starting")
        println("Random sequence: $randomTimes")
        println("SuperLong: $superLong, took $superLongTime ms")
        println("BigInt: $bigInt, took $bigIntTime ms")
    }
}