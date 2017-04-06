package org.abimon.visi.io

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.function.Supplier

interface DataSource {
    fun getLocation(): String
    fun getData(): ByteArray
    fun getInputStream(): InputStream
    fun getDataSize(): Long
}

class FileDataSource(val file: File) : DataSource {

    override fun getLocation(): String = file.absolutePath

    override fun getData(): ByteArray = file.readBytes()

    override fun getInputStream(): InputStream = FileInputStream(file)

    override fun getDataSize(): Long = file.length()
}

class HTTPDataSource(val url: URL, val userAgent: String) : DataSource {

    constructor(url: URL) : this(url, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0")

    override fun getLocation(): String {
        return url.toExternalForm()
    }

    override fun getData(): ByteArray {
        val baos = ByteArrayOutputStream()
        getInputStream().use { it.writeTo(baos, 8192, true) }
        return baos.toByteArray()
    }

    override fun getInputStream(): InputStream {
        val http = url.openConnection() as HttpURLConnection
        http.requestMethod = "GET"
        http.setRequestProperty("User-Agent", userAgent)
        return if(http.responseCode < 400) http.inputStream else http.errorStream
    }

    override fun getDataSize(): Long = getInputStream().available().toLong()
}

class FunctionalDataSource(val data: Supplier<ByteArray>) : DataSource {
    override fun getLocation(): String {
        return "Supplier " + data.toString()
    }

    override fun getData(): ByteArray {
        return data.get()
    }

    override fun getInputStream(): InputStream = ByteArrayInputStream(getData())

    override fun getDataSize(): Long = getData().size.toLong()
}

class FunctionDataSource(val data: () -> ByteArray): DataSource {
    override fun getLocation(): String = data.toString()

    override fun getData(): ByteArray = data.invoke()

    override fun getInputStream(): InputStream = ByteArrayInputStream(data.invoke())

    override fun getDataSize(): Long = data.invoke().size.toLong()

}

/** One time use */
class InputStreamDataSource(val stream: InputStream) : DataSource {
    override fun getLocation(): String = stream.toString()

    override fun getData(): ByteArray = stream.readBytes()

    override fun getInputStream(): InputStream = stream

    override fun getDataSize(): Long = stream.available().toLong()

}