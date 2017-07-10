package org.abimon.visi.lang

import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun Throwable.exportStackTrace(): String {
    val baos = ByteArrayOutputStream()
    printStackTrace(PrintStream(baos, true, Charsets.UTF_8.name()))
    return String(baos.toByteArray(), Charsets.UTF_8)
}