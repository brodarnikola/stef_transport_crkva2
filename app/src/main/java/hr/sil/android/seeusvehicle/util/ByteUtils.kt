package hr.sil.android.seeusvehicle.util

import java.nio.ByteBuffer

/**
 * @author mfatiga
 */
fun Int.toU16Bytes(): ByteArray {
    val bytes = ByteArray(4)
    val buf = ByteBuffer.allocate(4).putInt(this and 0x0000FFFF)
    buf.position(0)
    buf.get(bytes)
    return bytes.drop(2).toByteArray()
}

/**
 * @author mfatiga
 */
fun ByteArray.toHexString(): String {
    val hexArray = "0123456789ABCDEF".toCharArray()
    val result = CharArray(this.size * 2)
    for (i in 0..(this.size - 1)) {
        val v = this[i].toInt() and 0xFF
        result[i * 2] = hexArray[v ushr 4]
        result[i * 2 + 1] = hexArray[v and 0x0F]
    }
    return result.joinToString(separator = "", transform = Char::toString)
}

/**
 * @author mfatiga
 */
fun ByteArray.macToString() = toHexString().chunked(2).joinToString(":") { it }