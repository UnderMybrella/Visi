package org.abimon.visi.security

import org.junit.Test
import java.io.File
import java.io.FileInputStream
import kotlin.system.measureTimeMillis

class VSecurityKtTest {
    @Test
    fun fileTest() {
        val ms = measureTimeMillis { println("Hash: " + FileInputStream(File("test.png")).sha256Hash()) }
        println("Took $ms ms")
    }
}