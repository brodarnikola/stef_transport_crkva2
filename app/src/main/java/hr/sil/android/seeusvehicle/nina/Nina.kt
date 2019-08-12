package hr.sil.android.seeusvehicle.nina

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import hr.sil.android.seeusvehicle.App
import hr.sil.android.seeusvehicle.BuildConfig
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.stationsData.BusStopRequest
import hr.sil.android.seeusvehicle.util.ByteOperations
import hr.sil.android.seeusvehicle.util.macToString
import hr.sil.android.seeusvehicle.util.toHexString
import hr.sil.android.usbninacommunicator.NinaCommandParserCommand
import hr.sil.android.usbninacommunicator.NinaDeviceConfig
import hr.sil.android.usbninacommunicator.NinaUsbWrapper
import hr.sil.android.usbninacommunicator.core.UsbRawCommunicator

/**
 * @author mfatiga
 */
object Nina {
    private val log = logger()

    const val DEVICE_TYPE = 0x02.toByte()

    const val INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB"

    private val advPacketMain = NinaAdvMain()
    private fun updateAdvertisement() {
        NinaUsbWrapper.setAdvertisementPacket(advPacketMain)
    }

    fun advGetLedStatusBitmask(): Byte {
        return advPacketMain.ledStatusBitmask
    }

    fun advSetLedStatusBitmask(ledStatusBitmask: Byte) {
        advPacketMain.ledStatusBitmask = ledStatusBitmask
        updateAdvertisement()
    }

    private fun onStopRequest(mac: ByteArray, data: ByteArray) {
        log.info("Received request: [${data.toHexString()}] from ${mac.macToString()}")
        val requestByte = data.firstOrNull()
        if (requestByte != null) {
            ByteOperations.onUsbData(requestByte)
        } else {
            log.info("Invalid request! Data is empty!")
        }
    }

    private fun initializeBleInCommands() {
        NinaUsbWrapper.registerCommandParserCommand(
            NinaCommandParserCommand(
                group = 0x02.toByte(),
                action = 0x00.toByte(),
                onWrite = ::onStopRequest
            )
        )
    }

    private fun onNinaReady() {
        updateAdvertisement()
        initializeBleInCommands()
    }

    fun isReady() = NinaUsbWrapper.isReady()

    val mac: String?
        get() {
            if (NinaUsbWrapper.isReady()) {
                val macBytes = NinaUsbWrapper.mac
                if (macBytes.isNotEmpty() && macBytes.count() == 6) {
                    return macBytes.toHexString().toUpperCase()
                }
            }
            return null
        }

    fun initialize(handleUsbDevicePermission: (UsbDevice) -> Boolean) {
        NinaUsbWrapper.initialize(
            getContext = { App.ref },
            getDevice = { (App.ref.getSystemService(Context.USB_SERVICE) as UsbManager).deviceList.values.firstOrNull() },
            usbConfig = UsbRawCommunicator.UsbConfig(
                useDTR = true,
                useRTS = false,
                permissionRequestIntentAction = INTENT_ACTION_GRANT_USB
            ),
            deviceConfig = NinaDeviceConfig(
                deviceType = DEVICE_TYPE,
                deviceReady = this::onNinaReady,
                enableEncryption = false
            ),
            handleDevicePermission = handleUsbDevicePermission
        )
    }
}