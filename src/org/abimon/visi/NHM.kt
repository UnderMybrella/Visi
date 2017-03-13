package org.abimon.visi

import org.abimon.visi.collections.Pool
import org.abimon.visi.collections.PoolableObject
import org.abimon.visi.io.check
import org.abimon.visi.io.println
import org.abimon.visi.lang.*
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