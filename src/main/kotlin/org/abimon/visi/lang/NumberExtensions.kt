package org.abimon.visi.lang

fun Long.square(): Long = this * this
fun Int.square(): Long = this.toLong().square()
fun Short.square(): Int = this * this
fun Byte.square(): Int = this * this

fun Double.square(): Double = this * this
fun Float.square(): Float = this * this

fun Long.toBinaryString(): String = java.lang.Long.toBinaryString(this)

fun Int.pluralise(singular: String, plural: String = "${singular}s") = if(this == 1 || this == -1) "$this $singular" else "$this $plural"
fun Long.pluralise(singular: String, plural: String = "${singular}s") = if(this == 1L || this == -1L) "$this $singular" else "$this $plural"