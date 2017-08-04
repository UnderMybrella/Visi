package org.abimon.visi.io

import org.abimon.visi.lang.replaceLast
import java.io.File

fun File.ensureUnique(): File {
    if (exists())
        return File(this.absolutePath.replaceLast(".$extension", "-${java.util.UUID.randomUUID()}.$extension"))
    return this
}

/**
 * Get the relative path to this file, from parent directory [to]
 * Path will start with the name of [to]
 * @see [relativePathFrom]
 */
infix fun File.relativePathTo(to: File): String = to.name + absolutePath.replace(to.absolutePath, "")
/**
 * Get the relative path from parent directory [to] to this file
 * Path will ***not*** start with the name of [to]
 * @see [relativePathTo]
 */
infix fun File.relativePathFrom(to: File): String = absolutePath.replace(to.absolutePath + File.separator, "")
