package org.abimon.visi.io

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.function.Supplier

interface DataSource {
    /**
     * Get an input stream associated with this data source.
     */
    val inputStream: InputStream
    val location: String
    val data: ByteArray
    val size: Long
    
    fun <T> use(action: (InputStream) -> T): T = inputStream.use(action)
}

class FileDataSource(val file: File) : DataSource {
    private var prev: FileInputStream? = null

    override val location: String = file.absolutePath

    override val data: ByteArray = file.readBytes()

    override val inputStream: InputStream = run {
        if (prev != null)
            prev!!.close()
        prev = FileInputStream(file)
        return@run prev!!
    }

    override val size: Long = file.length()
}

class HTTPDataSource(val url: URL, val userAgent: String) : DataSource {

    constructor(url: URL) : this(url, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0")

    override val location: String = url.toExternalForm()

    override val data: ByteArray = run {
        val baos = ByteArrayOutputStream()
        use { it.writeTo(baos, 8192, true) }
        return@run baos.toByteArray()
    }

    override val inputStream: InputStream = run {
        val http = url.openConnection() as HttpURLConnection
        http.requestMethod = "GET"
        http.setRequestProperty("User-Agent", userAgent)
        return@run if(http.responseCode < 400) http.inputStream else http.errorStream
    }

    override val size: Long = use { it.available().toLong() }
}

class FunctionalDataSource(val dataSupplier: Supplier<ByteArray>) : DataSource {
    override val location: String = "Supplier " + dataSupplier.toString()

    override val data: ByteArray = dataSupplier.get()

    override val inputStream: InputStream = ByteArrayInputStream(data)

    override val size: Long = data.size.toLong()
}

class FunctionDataSource(val dataFunc: () -> ByteArray): DataSource {
    override val location: String = dataFunc.toString()

    override val data: ByteArray = dataFunc()

    override val inputStream: InputStream = ByteArrayInputStream(dataFunc())

    override val size: Long = dataFunc().size.toLong()

}

/** One time use */
class InputStreamDataSource(val stream: InputStream) : DataSource {
    override val location: String = stream.toString()

    override val data: ByteArray = stream.use { it.readBytes() }

    override val inputStream: InputStream = stream

    override val size: Long = stream.use { it.available().toLong() }

}