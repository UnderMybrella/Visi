package org.abimon.visi.lang

fun Runtime.usedMemory(): Long = (totalMemory() - freeMemory())

fun setHeadless() = System.setProperty("java.awt.headless", "true")