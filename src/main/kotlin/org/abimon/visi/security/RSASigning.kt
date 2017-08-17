package org.abimon.visi.security

import java.io.File
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

fun ByteArray.sign(privateKey: PrivateKey): ByteArray {
    val signature = Signature.getInstance("SHA1withRSA")
    signature.initSign(privateKey)
    signature.update(this)

    return signature.sign()
}

fun ByteArray.verify(signatureData: ByteArray, publicKey: PublicKey): Boolean {
    val signature = Signature.getInstance("SHA1withRSA")
    signature.initVerify(publicKey)
    signature.update(this)

    return signature.verify(signatureData)
}

fun ByteArray.sign(privateKey: File): ByteArray {
    val keyFactory = KeyFactory.getInstance("RSA")
    val private = keyFactory.generatePrivate(RSAPrivateKeySpec(privateKey.readText()))
    return sign(private)
}

fun ByteArray.verify(signatureData: ByteArray, publicKey: File): Boolean {
    val keyFactory = KeyFactory.getInstance("RSA")
    val public = keyFactory.generatePublic(RSAPublicKeySpec(publicKey.readText()))
    return verify(signatureData, public)
}

fun ByteArray.encryptRSA(publicKey: PublicKey): ByteArray {
    val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    return cipher.doFinal(this)
}

fun ByteArray.decryptRSA(privateKey: PrivateKey): ByteArray {
    val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
    cipher.init(Cipher.DECRYPT_MODE, privateKey)
    return cipher.doFinal(this)
}

fun RSAPrivateKey(str: String): PrivateKey
    = KeyFactory.getInstance("RSA").generatePrivate(RSAPrivateKeySpec(str))

fun RSAPublicKey(str: String): PublicKey
        = KeyFactory.getInstance("RSA").generatePublic(RSAPublicKeySpec(str))

fun RSAPrivateKeySpec(str: String): KeySpec
        = PKCS8EncodedKeySpec(Base64.getDecoder().decode(str
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("\\s+".toRegex(), "")))

fun RSAPublicKeySpec(str: String): KeySpec
        = X509EncodedKeySpec(Base64.getDecoder().decode(str
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\\s+".toRegex(), "")))