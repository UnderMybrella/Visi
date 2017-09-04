package org.abimon.visi.io

import org.abimon.visi.collections.get
import java.io.InputStream
import java.io.OutputStream

class ByteArrayIOStream {
    private val buffer: MutableList<Byte> = ArrayList()
    private var pos: Int = 0

    val inputStream: InputStream =
            object : InputStream() {
                val io: ByteArrayIOStream = this@ByteArrayIOStream

                override fun read(): Int = io.read()
                override fun read(b: ByteArray): Int = io.read(b)
                override fun read(b: ByteArray, off: Int, len: Int): Int = io.read(b, off, len)

                override fun skip(n: Long): Long {
                    io.seekFromPos(n.toInt())
                    return n
                }

                override fun available(): Int = io.buffer.size - io.pos
                override fun reset() {
                    io.pos = 0
                }
            }

    val outputStream: OutputStream =
            object : OutputStream() {
                val io: ByteArrayIOStream = this@ByteArrayIOStream

                override fun write(b: Int) = io.write(b.toByte())
                override fun write(b: ByteArray) = io.write(b)
                override fun write(b: ByteArray, off: Int, len: Int) = io.write(b, off, len)
            }

    val dataSource: DataSource =
            object : DataSource {
                val io: ByteArrayIOStream = this@ByteArrayIOStream

                override val inputStream: InputStream
                    get() = io.inputStream
                override val seekableInputStream: InputStream
                    get() = io.inputStream
                override val location: String
                    get() = "ByteArrayIOStrea#dataSource ${io.hashCode()}"
                override val data: ByteArray
                    get() = io.toByteArray()
                override val size: Long
                    get() = buffer.size.toLong()

                override fun pipe(out: OutputStream) = out.write(toByteArray())
            }

    fun read(): Int = buffer[pos++, -1].toInt() and 0xFF
    fun read(bytes: ByteArray): Int {
        for (i in 0 until bytes.size) {
            if (pos + i >= buffer.size)
                return i
            bytes[i] = buffer[pos++]
        }

        return bytes.size
    }
    fun read(bytes: ByteArray, off: Int, len: Int): Int {
        for (i in 0 until len) {
            if (pos + i >= buffer.size || i + off >= bytes.size)
                return i
            bytes[i + off] = buffer[pos++]
        }

        return len
    }

    fun write(byte: Byte) {
        buffer.add(pos++, byte)
    }

    fun write(bytes: ByteArray) {
        bytes.forEach { buffer.add(pos++, it) }
    }

    fun write(bytes: ByteArray, off: Int, len: Int) {
        for(i in 0 until len) {
            if(i + off >= bytes.size)
                return

            buffer.add(pos++, bytes[i + off])
        }
    }

    fun seek(pos: Int) {
        this.pos = pos
    }

    fun seekFromEnd(offset: Int) {
        pos = buffer.size - offset - 1
    }

    fun seekFromPos(offset: Int) {
        pos += offset
    }

    fun clear() {
        buffer.clear()
    }

    fun toByteArray(): ByteArray = buffer.toByteArray()

    fun writeTo(output: OutputStream) = output.write(toByteArray())
}