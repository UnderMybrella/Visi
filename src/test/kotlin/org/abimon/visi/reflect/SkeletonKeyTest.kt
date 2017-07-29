package org.abimon.visi.reflect

import org.junit.Test
import java.util.*

internal class SkeletonKeyTest {
    @Test
    fun test() {
        println((SkeletonKey(Base64.getEncoder())["toBase64"] as CharArray).joinToString())
        println(SkeletonKey(Base64.getEncoder())("outLength", 1))
    }
}