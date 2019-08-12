package hr.sil.android.seeusvehicle.nina

import hr.sil.android.usbninacommunicator.NinaAdvertisementPacket

/**
 * @author mfatiga
 */
class NinaAdvMain : NinaAdvertisementPacket(0x73) {
    //props
    var ledStatusBitmask: Byte = 0x00.toByte()
        set(value) {
            if (field != value) {
                field = value
                cacheDirty = true
            }
        }

    //util
    private var cachedBytes = byteArrayOf()

    @Volatile
    private var cacheDirty = true

    override fun getBytes(): ByteArray {
        if (cacheDirty) {
            var result = byteArrayOf()

            //led status bitmask - 1 byte
            result += byteArrayOf(ledStatusBitmask)

            cachedBytes = result
            cacheDirty = false
            return result
        } else {
            return cachedBytes
        }
    }
}