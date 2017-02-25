package org.abimon.visi

import org.abimon.visi.io.check
import org.abimon.visi.io.errPrintln
import org.abimon.visi.io.println
import org.abimon.visi.lang.*
import java.io.File
import java.io.FileInputStream

fun main(args: Array<String>) {
    println(FileInputStream(File("Visi.iml")).check(FileInputStream(File("Visi.iml"))))
    println(Kelvin(0).toFahrenheit())
    Millimetre(1392).toCentimetres().println()
    ByteUnit(1024).toMegabytes().println()

    Kilogram(3.14).toPounds().println()
    Kilogram(1.101).toOunces().println()
    Kilogram(1.101).toPounds().toOunces().println()
}