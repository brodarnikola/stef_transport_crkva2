package hr.sil.android.seeusvehicle.core.remote

import hr.sil.android.seeusvehicle.cache.model.DeviceStationData
import hr.sil.android.seeusvehicle.core.remote.base.WSBase
import hr.sil.android.seeusvehicle.core.remote.model.REndAdminInfo
import hr.sil.android.seeusvehicle.core.remote.model.REndDeviceInfo
import hr.sil.android.seeusvehicle.core.remote.model.RequestRegisterDevice
import hr.sil.android.seeusvehicle.core.remote.model.RequestUpdateDevice
import hr.sil.android.seeusvehicle.core.remote.service.AdminAppService

object W_VehicleApp : WSBase() {
    suspend fun getDeviceInfo(): REndDeviceInfo? {
        return wrapAwaitData(
            call = AdminAppService.service.getDeviceInfo(),
            methodName = "getDeviceInfo()"
        )
    }

    suspend fun registerDevice(requestRegisterDevice: RequestRegisterDevice): String? {
        return wrapAwaitData(
            call = AdminAppService.service.registerDevice(requestRegisterDevice),
            methodName = "registerDevice()"
        )
    }

    suspend fun updateDevice(deviceUpdate: RequestUpdateDevice): REndDeviceInfo? {
        return wrapAwaitData(
            call = AdminAppService.service.getDeviceUpdate(deviceUpdate),
            methodName = "updateDevice()"
        )
    }

    suspend fun login(): REndAdminInfo? {
        return wrapAwaitData(
            call = AdminAppService.service.login(),
            methodName = "login()"
        )
    }

    suspend fun getAccountInfo(): REndAdminInfo? {
        return wrapAwaitData(
            call = AdminAppService.service.getAccountInfo(),
            methodName = "getAccountInfo()"
        )
    }

    suspend fun getDeviceStationData(): List<DeviceStationData>? {
        return wrapAwaitData(
            call = AdminAppService.service.getDeviceStationData(),
            methodName = "getDeviceStationData()"
        )
    }

    suspend fun getDeviceStopRequestMac(stationMac: String, lineNumber: String, periodSeconds: String): String? {
        return wrapAwaitData(
            call = AdminAppService.service.getDeviceStopRequestMac(stationMac, lineNumber, periodSeconds),
            methodName = "getDeviceStopRequestMac()"
        )
    }

    suspend fun getExitStationByMac(stationMac: String): String? {
        return wrapAwaitData(
            call = AdminAppService.service.getExitStationByMac(stationMac),
            methodName = "getExitStationByMac()"
        )
    }
}