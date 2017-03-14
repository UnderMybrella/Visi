package org.abimon.visi

import org.abimon.visi.collections.Pool
import org.abimon.visi.collections.PoolableObject
import org.abimon.visi.image.toBufferedImage
import org.abimon.visi.image.toPixelList
import org.abimon.visi.io.check
import org.abimon.visi.io.println
import org.abimon.visi.io.question
import org.abimon.visi.lang.*
import org.abimon.visi.security.*
import java.io.File
import java.io.FileInputStream
import java.util.*

enum class EnumTest { A, B, C }

fun main(args: Array<String>) {
    println(FileInputStream(File("Visi.iml")).check(FileInputStream(File("Visi.iml"))))
    println(Kelvin(0).toFahrenheit())
    Millimetre(1392).toCentimetres().println()
    ByteUnit(1024).toMegabytes().println()

    Kilogram(3.14).toPounds().println()
    Kilogram(1.101).toOunces().println()
    Kilogram(1.101).toPounds().toOunces().println()

    println(EnumTest::class.isValue("E"))

    if(question("Do Pool Test? ", "Y")) {
        val pool = Pool<ArrayList<String>>(4)
        pool.add(PoolableObject(ArrayList<String>()))
        pool.add(PoolableObject(ArrayList<String>()))
        pool.add(PoolableObject(ArrayList<String>()))
        pool.add(PoolableObject(ArrayList<String>()))

        println(pool.getFree().size)
        Thread {
            (pool.get()!! as PoolableObject).use { obj ->
                Thread.sleep(10000)
            }
        }.start()
        Thread.sleep(1000)
        println(pool.getFree().size)
        Thread.sleep(10000)
        println(pool.getFree().size)
    }

    val bees = File("/Users/undermybrella/Bee Movie Script.txt").readText().toByteArray()

    bees.md2Hash().println()
    bees.md5Hash().println()
    bees.sha1Hash().println()
    bees.sha224Hash().println()
    bees.sha256Hash().println()
    bees.sha384Hash().println()
    bees.sha512Hash().println()

    bees.md2Hash().md5Hash().sha1Hash().sha224Hash().sha256Hash().sha384Hash().sha512Hash().println()

    val lineOfBees = File("LineOfBees.png").readBytes().toBufferedImage().toPixelList(false)
    println(lineOfBees.size)
}