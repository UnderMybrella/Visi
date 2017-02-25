package org.abimon.visi.lang

import java.text.DecimalFormat

object UnitConstants {
    val SPEED_OF_LIGHT_MS = 300000000.0
}

/** Data */

data class ByteUnit(val bytes: Double) {
    constructor(bytes: Long): this(bytes.toDouble())

    fun toKilobytes(): Kilobyte = Kilobyte(bytes / 1000.0)
    fun toMegabytes(): Megabyte = Megabyte(bytes / 1000.0 / 1000.0)

    override fun toString(): String = "$bytes B"
    fun format(format: DecimalFormat): String = "${format.format(bytes)} B"
}

data class Kilobyte(val kilobytes: Double) {
    constructor(kilobytes: Long): this(kilobytes.toDouble())

    fun toBytes(): ByteUnit = ByteUnit(kilobytes * 1000)
    fun toMegabytes(): Megabyte = Megabyte(kilobytes / 1000.0)

    override fun toString(): String = "$kilobytes kB"
    fun format(format: DecimalFormat): String = "${format.format(kilobytes)} kB"
}

data class Megabyte(val megabytes: Double) {
    constructor(megabytes: Long): this(megabytes.toDouble())

    fun toBytes(): ByteUnit = ByteUnit(megabytes * 1000 * 1000)
    fun toKilobytes(): Kilobyte = Kilobyte(megabytes * 1000)

    override fun toString(): String = "$megabytes MB"
    fun format(format: DecimalFormat): String = "${format.format(megabytes)} MB"
}

/** Temperature */

data class Celsius(val degrees: Double) {
    constructor(degrees: Long): this(degrees.toDouble())

    fun toFahrenheit(): Fahrenheit = Fahrenheit((degrees * (9.0/5.0)) + 32)
    fun toKelvin(): Kelvin = Kelvin(degrees + 273.15)

    override fun toString(): String = "$degrees °C"

    init {
        if(degrees < -273.15)
            throw IllegalArgumentException("$this is an invalid temperature (lower than Absolute Zero at -273.15 °C)")
    }
}

data class Fahrenheit(val degrees: Double) {
    constructor(degrees: Long): this(degrees.toDouble())

    fun toCelsius(): Celsius = Celsius((degrees - 32) * (5.0/9.0))
    fun toKelvin(): Kelvin = Kelvin((degrees + 459.67) * (5.0/9.0))

    override fun toString(): String = "$degrees °F"

    init {
        if(degrees < -459.67)
            throw IllegalArgumentException("$this is an invalid temperature (lower than Absolute Zero at -459.67 °F)")
    }
}

data class Kelvin(val degrees: Double) {
    constructor(degrees: Long): this(degrees.toDouble())

    fun toCelsius(): Celsius = Celsius(degrees - 273.15)
    fun toFahrenheit(): Fahrenheit = Fahrenheit((degrees * (9.0/5.0)) - 459.67)

    override fun toString(): String = "$degrees K"
    init {
        if(degrees < 0)
            throw IllegalArgumentException("$this is an invalid temperature (lower than Absolute Zero at 0 K)")
    }
}

/** Distance */

data class Millimetre(val mm: Double) {
    constructor(mm: Long): this(mm.toDouble())

    fun toCentimetres(): Centimetre = Centimetre(mm / 1000)

    override fun toString(): String = "$mm mm"
}

data class Centimetre(val cm: Double) {
    constructor(cm: Long): this(cm.toDouble())

    override fun toString(): String = "$cm cm"
}

/** Energy */

data class Joule(val joules: Double) {
    constructor(joules: Long): this(joules.toDouble())

    fun toKilograms(): Kilogram = Kilogram(joules / UnitConstants.SPEED_OF_LIGHT_MS.square())
    fun toPounds(): Pound = toKilograms().toPounds()

    override fun toString(): String = "$joules J"
}

/** Mass */

data class Gram(val g: Double) {
    constructor(g: Long): this(g.toDouble())

    fun toJoules(): Joule = toKilograms().toJoules()

    fun toKilograms(): Kilogram = Kilogram(g / 1000.0)
    fun toOunces(): Ounce = Ounce(g * 0.0353)
    fun toPounds(): Pound = Pound(g * 0.0022)

    override fun toString(): String = "$g g"
}

data class Kilogram(val kg: Double) {
    constructor(kg: Long): this(kg.toDouble())

    fun toJoules(): Joule = Joule(kg * UnitConstants.SPEED_OF_LIGHT_MS.square())

    fun toOunces(): Ounce = toPounds().toOunces()
    fun toPounds(): Pound = Pound(kg / 0.45359237)

    override fun toString(): String = "$kg kg"
}

data class Ounce(val oz: Double) {
    constructor(oz: Long): this(oz.toDouble())

    fun toGrams(): Gram = Gram(oz / 0.0353)

    override fun toString(): String = "$oz oz"
}

data class Pound(val lb: Double) {
    constructor(lb: Long): this(lb.toDouble())

    fun toJoules(): Joule = toKilograms().toJoules()

    fun toOunces(): Ounce = Ounce(lb * 16)
    fun toKilograms(): Kilogram = Kilogram(lb * 0.45359237)

    override fun toString(): String = "$lb lb"
}