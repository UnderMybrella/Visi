package org.abimon.visi.io

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*

fun InputStream.readPartialBytes(len: Int, bufferSize: Int = 8192): ByteArray {
    val baos = ByteArrayOutputStream()
    val buffer = ByteArray(bufferSize)
    var read = 0
    var total = 0

    while (read > -1 && total < len) {
        read = read(buffer, 0, bufferSize.coerceAtMost(len - total))
        if(read < 0)
            break

        baos.write(buffer, 0, read)
        total += read
    }

    return baos.toByteArray()
}

fun InputStream.readAllBytes(): ByteArray {
    val data = ByteArrayOutputStream()
    writeTo(data)
    return data.toByteArray()
}

fun InputStream.writeTo(writingTo: OutputStream, bufferSize: Int = 8192, closeAfter: Boolean = false): Int {
    val buffer = ByteArray(bufferSize)
    var read = 0
    var total = 0
    var count = 0.toByte()

    while (read > -1) {
        read = read(buffer)
        if(read < 0)
            break
        if(read == 0) {
            count++
            if(count > 3)
                break
        }

        writingTo.write(buffer, 0, read)
        total += read
    }

    if(closeAfter)
        close()

    return total
}

fun InputStream.check(inputStream: InputStream): Boolean {
    use {
        inputStream.use {
            val buffer = ByteArray(8192)
            val secondBuffer = ByteArray(8192)

            var read = 0
            var secondRead = 0

            while (read > -1 && secondRead > -1) {
                read = read(buffer)
                secondRead = inputStream.read(secondBuffer)

                if (read < 0 && secondRead < 0)
                    return true
                else if (read < 0 || secondRead < 0)
                    return false

                if (!Arrays.equals(buffer, secondBuffer))
                    return false
            }

            return Arrays.equals(buffer, secondBuffer)
        }
    }
}

fun InputStream.skipBytes(bytes: Long): InputStream {
    skip(bytes)
    return this
}

fun File.iterate(includeDirs: Boolean = false): LinkedList<File> {
    val files = LinkedList<File>()

    if (isDirectory && listFiles() != null)
        for (f in listFiles()!!) {
            if (includeDirs || f.isFile)
                files.add(f)
            if (f.isDirectory)
                files.addAll(f.iterate(includeDirs))
        }

    return files
}

fun Any?.println() = println(this)

fun errPrintln(message: Any?) {
    System.err.println(message)
}

/**
 * Print an error message, and then shut down the program
 */
fun forceError(message: Any?) {
    System.err.println(message)
    System.exit(1)
}