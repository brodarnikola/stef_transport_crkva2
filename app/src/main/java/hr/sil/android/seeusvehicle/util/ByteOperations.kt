package hr.sil.android.seeusvehicle.util

import hr.sil.android.seeusvehicle.App
import hr.sil.android.seeusvehicle.core.util.SettingsHelper
import hr.sil.android.seeusvehicle.fcm.BackendPushDataEvent
import hr.sil.android.seeusvehicle.nina.Nina
import hr.sil.android.seeusvehicle.stationsData.BusStopRequest
import hr.sil.android.seeusvehicle.stationsData.DeviceStationId
import hr.sil.android.seeusvehicle.stationsData.ExitStationId

object ByteOperations {

    fun getMergedData(): Byte {
        return ((SettingsHelper.globalBackendData.toInt() or SettingsHelper.globalUsbData.toInt()) and 0xFF).toByte()
    }


    fun onBackendData(newDeviceStation: DeviceStationId) {
        val dataFromBackend = newDeviceStation.binaryData
        SettingsHelper.globalBackendData = dataFromBackend

        Nina.advSetLedStatusBitmask(getMergedData())

        App.ref.eventBus.post(DeviceStationId(
            binaryData = newDeviceStation.binaryData,
            stationMac = newDeviceStation.stationMac,
            stationName = newDeviceStation.stationName
        ))
    }

    fun onBackendPush(newReceivedData: Byte) {
        val newBackendPush = ((SettingsHelper.globalBackendData.toInt() or newReceivedData.toInt()) and 0xFF).toByte()
        SettingsHelper.globalBackendData = newBackendPush

        Nina.advSetLedStatusBitmask(getMergedData())

        val pushByteData = newReceivedData
        val pushStringData = String.format("%8s", Integer.toBinaryString(pushByteData.toInt() and 0xFF)).replace(' ', '0')

        App.ref.eventBus.post(BackendPushDataEvent(pushStringData))
    }

    fun onUsbData(newReceivedData: Byte) {
        val newDataFromUsb = ((SettingsHelper.globalUsbData.toInt() or newReceivedData.toInt()) and 0xFF).toByte()

        SettingsHelper.globalUsbData = newDataFromUsb

        Nina.advSetLedStatusBitmask(getMergedData())

        App.ref.eventBus.post(BusStopRequest(byteData = newReceivedData, backendResponse = false))
    }

    fun resetData(exitStationId: ExitStationId) {
        //dataFromBackend = 0x00.toByte()
        //dataFromUsb = 0x00.toByte()

        SettingsHelper.globalBackendData = 0x00.toByte()
        SettingsHelper.globalUsbData = 0x00.toByte()

        Nina.advSetLedStatusBitmask(getMergedData())

        App.ref.eventBus.post(
            ExitStationId(
                exitStationId.stationExitName ?: "",
                exitStationId.latitude.toString(),
                exitStationId.longitude.toString()
            )
        )
    }

    fun resetClearAdvertisment() {

        SettingsHelper.globalBackendData = 0x00.toByte()
        SettingsHelper.globalUsbData = 0x00.toByte()
        Nina.advSetLedStatusBitmask(getMergedData())
    }

    fun isStopBitSet(data: Byte): Boolean {
        return (data.toInt() and 0b00000001) > 0
    }

    fun isDisabilityBitSet(data: Byte): Boolean {
        return (data.toInt() and 0b00000010) > 0
    }

    fun isDisabilityOtherBitSet(data: Byte): Boolean {
        return (data.toInt() and 0b00000100) > 0
    }

    fun isDisabilityBlindBitSet(data: Byte): Boolean {
        return (data.toInt() and 0b00001000) > 0
    }

    fun isDisabilitySpeechBitSet(data: Byte): Boolean {
        return (data.toInt() and 0b00010000) > 0
    }

    fun isDisabilityHardOfHearingBitSet(data: Byte): Boolean {
        return (data.toInt() and 0b00100000) > 0
    }

    fun isDisabilityReducedMobilityBitSet(data: Byte): Boolean {
        return (data.toInt() and 0b01000000) > 0
    }

    fun isDisabilityWheeelChairBitSet(data: Byte): Boolean {
        return (data.toInt() and 0b10000000) > 0
    }



}