package org.abimon.visi.security

import org.abimon.visi.io.readChunked
import java.io.InputStream
import java.math.BigInteger
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.security.MessageDigest

/** ***Do not use for things like passwords*** */
fun ByteArray.hash(algorithm: String): String {
    val md = MessageDigest.getInstance(algorithm)
    val hashBytes = md.digest(this)
    return BigInteger(1, hashBytes).toString(16)
}
/** ***Do not use for things like passwords*** */
fun ByteArray.md2Hash(): String = hash("MD2")
/** ***Do not use for things like passwords*** */
fun ByteArray.md5Hash(): String = hash("MD5")
/** ***Do not use for things like passwords*** */
fun ByteArray.sha1Hash(): String = hash("SHA-1")
/** ***Do not use for things like passwords*** */
fun ByteArray.sha224Hash(): String = hash("SHA-224")
/** ***Do not use for things like passwords*** */
fun ByteArray.sha256Hash(): String = hash("SHA-256")
/** ***Do not use for things like passwords*** */
fun ByteArray.sha384Hash(): String = hash("SHA-384")
/** ***Do not use for things like passwords*** */
fun ByteArray.sha512Hash(): String = hash("SHA-512")

/** **Do not use for things like passwords, or situations where the data needs to be blanked out** */
fun String.md2Hash(): String = toByteArray(Charsets.UTF_8).md2Hash()
/** **Do not use for things like passwords, or situations where the data needs to be blanked out** */
fun String.md5Hash(): String = toByteArray(Charsets.UTF_8).md5Hash()
/** **Do not use for things like passwords, or situations where the data needs to be blanked out** */
fun String.sha1Hash(): String = toByteArray(Charsets.UTF_8).sha1Hash()
/** **Do not use for things like passwords, or situations where the data needs to be blanked out** */
fun String.sha224Hash(): String = toByteArray(Charsets.UTF_8).sha224Hash()
/** **Do not use for things like passwords, or situations where the data needs to be blanked out** */
fun String.sha256Hash(): String = toByteArray(Charsets.UTF_8).sha256Hash()
/** **Do not use for things like passwords, or situations where the data needs to be blanked out** */
fun String.sha384Hash(): String = toByteArray(Charsets.UTF_8).sha384Hash()
/** **Do not use for things like passwords, or situations where the data needs to be blanked out** */
fun String.sha512Hash(): String = toByteArray(Charsets.UTF_8).sha512Hash()

/** ***Do not use for things like passwords*** */
fun InputStream.hash(algorithm: String): String {
    val md = MessageDigest.getInstance(algorithm)
    readChunked { md.update(it) }
    val hashBytes = md.digest()
    return BigInteger(1, hashBytes).toString(16)
}
/** ***Do not use for things like passwords*** */
fun InputStream.md2Hash(): String = hash("MD2")
/** ***Do not use for things like passwords*** */
fun InputStream.md5Hash(): String = hash("MD5")
/** ***Do not use for things like passwords*** */
fun InputStream.sha1Hash(): String = hash("SHA-1")
/** ***Do not use for things like passwords*** */
fun InputStream.sha224Hash(): String = hash("SHA-224")
/** ***Do not use for things like passwords*** */
fun InputStream.sha256Hash(): String = hash("SHA-256")
/** ***Do not use for things like passwords*** */
fun InputStream.sha384Hash(): String = hash("SHA-384")
/** ***Do not use for things like passwords*** */
fun InputStream.sha512Hash(): String = hash("SHA-512")

fun CharArray.toByteArray(): ByteArray {
    val byteBuffer = Charset.forName("UTF-8").encode(CharBuffer.wrap(this))
    val byteArray = ByteArray(byteBuffer.remaining())
    byteBuffer.get(byteArray)
    return byteArray
}