package org.abimon.visi.security

import org.junit.Test
import java.io.ByteArrayInputStream
import java.security.SecureRandom
import kotlin.system.measureTimeMillis

class VSecurityKtTest {
    @Test
    fun fileTest() {
        val ms = measureTimeMillis { println("Hash: " + SecureRandom().run { val bytes = ByteArray(8192); nextBytes(bytes); return@run ByteArrayInputStream(bytes) }.sha256Hash()) }
        println("Took $ms ms")
    }
}