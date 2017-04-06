package org.abimon.visi.image

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import javax.imageio.ImageIO

/**
 * Returns a list of the pixels in this image
 * @param rows Should the pixels in the list be retrieved in rows (that is, (0,0) to (width,0), then (0,1) to (width,1), and so on
 */
fun BufferedImage.toPixelList(rows: Boolean = true): List<Color> {
    val list = ArrayList<Color>(width * height)

    if(rows) {
        for(y in 0 until height) {
            for (x in 0 until width) {
                list.add(Color(getRGB(x, y)))
            }
        }
    }
    else {
        for(x in 0 until width) {
            for (y in 0 until height) {
                list.add(Color(getRGB(x, y)))
            }
        }
    }

    return list
}
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
