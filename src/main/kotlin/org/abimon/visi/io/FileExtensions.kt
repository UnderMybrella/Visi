package org.abimon.visi.io

import org.abimon.visi.lang.replaceLast
import java.io.File

fun File.ensureUnique(): File {
    if (exists())
        return File(this.absolutePath.replaceLast(".$extension", "-${java.util.UUID.randomUUID()}.$extension"))
    return this
}