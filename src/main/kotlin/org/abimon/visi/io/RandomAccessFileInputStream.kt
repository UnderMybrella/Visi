package org.abimon.visi.io

import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile

@Deprecated("Use Java builtins", replaceWith = ReplaceWith("Channels.newInputStream(RandomAccessFile(file, \"r\").channel)"))
open class RandomAccessFileInputStream(file: File): InputStream() {
    private val randomAccessFile: RandomAccessFile = RandomAccessFile(file, "r")
    private var mark: Long = 0

    override fun read(): Int = randomAccessFile.read()
    override fun read(b: ByteArray): Int = randomAccessFile.read(b)
    override fun read(b: ByteArray, off: Int, len: Int): Int = randomAccessFile.read(b, off, len)

    override fun available(): Int = (randomAccessFile.length() - randomAccessFile.filePointer).toInt()
    override fun close() = randomAccessFile.close()

    override fun markSupported(): Boolean = true
    override fun mark(readlimit: Int) {
        mark = randomAccessFile.filePointer
    }

    override fun reset() {
        randomAccessFile.seek(mark)
    }

    override fun skip(n: Long): Long {
        val pos: Long = randomAccessFile.filePointer
        val len: Long = randomAccessFile.length()
        var newpos: Long

        if (n <= 0)
            return 0

        newpos = pos + n
        if (newpos > len)
            newpos = len

        randomAccessFile.seek(newpos)

        /* return the actual number of bytes skipped */
        return newpos - pos
    }
}