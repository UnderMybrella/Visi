package org.abimon.visi.util.zip

import java.util.function.Consumer
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun ZipInputStream.forEach(action: (ZipEntry) -> Unit) {
    while(true) action(this.nextEntry ?: break)
}

fun ZipInputStream.forEach(action: Consumer<ZipEntry>) {
    while(true) action.accept(this.nextEntry ?: break)
}