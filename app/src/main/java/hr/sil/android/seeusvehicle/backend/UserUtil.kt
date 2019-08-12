/* SWISS INNOVATION LAB CONFIDENTIAL
*
* www.swissinnolab.com
* __________________________________________________________________________
*
* [2016] - [2017] Swiss Innovation Lab AG
* All Rights Reserved.
*
* @author mfatiga
*
* NOTICE:  All information contained herein is, and remains
* the property of Swiss Innovation Lab AG and its suppliers,
* if any.  The intellectual and technical concepts contained
* herein are proprietary to Swiss Innovation Lab AG
* and its suppliers and may be covered by E.U. and Foreign Patents,
* patents in process, and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from Swiss Innovation Lab AG.
*/

package hr.sil.android.seeusvehicle.backend

import hr.sil.android.seeusvehicle.core.remote.WSAdminConfig
import hr.sil.android.seeusvehicle.core.remote.W_VehicleApp
import hr.sil.android.seeusvehicle.core.remote.model.REndAdminInfo
import hr.sil.android.seeusvehicle.core.remote.model.REndDeviceInfo
import hr.sil.android.seeusvehicle.core.remote.model.RequestRegisterDevice
import hr.sil.android.seeusvehicle.core.remote.model.RequestUpdateDevice
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.preferences.PreferenceStore

object UserUtil {
    private val log = logger()

    var admin: REndAdminInfo? = null
        private set

    var device: REndDeviceInfo? = null
        private set

    fun isLoggedIn() = admin != null

    fun isDeviceRegistered() = device != null

    suspend fun login(): Boolean {
        if (!PreferenceStore.userHash.isNullOrBlank()) {
            val loginResponse = W_VehicleApp.login()
            admin = loginResponse
            return loginResponse != null
        }
        return false
    }

    suspend fun loadDeviceInfo(): Boolean {
        if (admin == null) return false

        val responseDeviceInfo = W_VehicleApp.getDeviceInfo()
        device = responseDeviceInfo
        return responseDeviceInfo != null
    }

    suspend fun registerDevice(
        mac: String,
        referenceId: String,
        registrationPlateNumber: String,
        pushToken: String
    ): Boolean {
        val adminUser = admin
        if (adminUser == null) {
            log.error("AdminUser is NULL! Device registration failed!")
            return false
        }

        val request = RequestRegisterDevice().apply {
            this.mac = mac
            this.reference = referenceId
            this.registrationPlateNumber = registrationPlateNumber
            this.customerId = adminUser.customerId
            this.deviceToken = pushToken
        }

        val apiKey = W_VehicleApp.registerDevice(request)
        return if (apiKey != null) {
            WSAdminConfig.setDeviceApiKey(apiKey)
            device = W_VehicleApp.getDeviceInfo()
            true
        } else {
            false
        }
    }

    suspend fun updateDevice(referenceId: String, registrationPlateNumber: String): Boolean {
        //validate login
        if (admin == null) return false
        val registeredDevice = device ?: return false
        val pushToken = registeredDevice.deviceToken ?: return false

        val request = RequestUpdateDevice().apply {
            this.reference = referenceId
            this.registrationPlateNumber = registrationPlateNumber
            this.deviceToken = pushToken
        }

        val responseDevice = W_VehicleApp.updateDevice(request)
        return if (responseDevice != null) {
            device = responseDevice
            true
        } else {
            false
        }
    }

    suspend fun updateDevicePushToken(pushToken: String): Boolean {
        //validate login
        if (admin == null) return false
        val registeredDevice = device ?: return false

        val request = RequestUpdateDevice().apply {
            this.reference = registeredDevice.reference
            this.registrationPlateNumber = registeredDevice.registrationPlateNumber ?: ""
            this.deviceToken = pushToken
        }

        val responseDevice = W_VehicleApp.updateDevice(request)
        return if (responseDevice != null) {
            device = responseDevice
            true
        } else {
            false
        }
    }
}