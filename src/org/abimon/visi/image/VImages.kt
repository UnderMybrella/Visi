package org.abimon.visi.image

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

fun BufferedImage.toByteArray(format: String = "PNG"): ByteArray {
    val baos = ByteArrayOutputStream()
    ImageIO.write(this, format, baos)
    return baos.toByteArray()
}

fun BufferedImage.toInputStream(): InputStream = ByteArrayInputStream(toByteArray())

fun InputStream.toBufferedImage(): BufferedImage {
    val img = ImageIO.read(this)
    return img
}
fun ByteArray.toBufferedImage(): BufferedImage = ByteArrayInputStream(this).toBufferedImage()
