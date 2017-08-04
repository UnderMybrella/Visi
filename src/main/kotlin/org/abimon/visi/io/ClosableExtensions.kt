package org.abimon.visi.io

import java.io.Closeable
import java.util.function.Consumer

fun <T : Closeable?> T.use(consumer: Consumer<T>): Unit = use { consumer.accept(it) }
fun <T : Closeable?, R> T.use(function: java.util.function.Function<T, R>): R? = use { function.apply(it) }