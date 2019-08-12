/* SWISS INNOVATION LAB CONFIDENTIAL
*
* www.swissinnolab.com
* __________________________________________________________________________
*
* [2016] - [2018] Swiss Innovation Lab AG
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

package hr.sil.android.seeusvehicle.core.remote

import android.content.Context
import hr.sil.android.rest.core.configuration.ServiceConfig
import hr.sil.android.rest.core.configuration.parameters.model.Authorization
import hr.sil.android.rest.core.configuration.parameters.model.RestHeader
import hr.sil.android.rest.core.util.UserHashUtil
import hr.sil.android.seeusvehicle.BuildConfig
import hr.sil.android.seeusvehicle.core.remote.service.AdminAppService
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.preferences.PreferenceStore

/**
 * @author mfatiga
 */
object WSAdminConfig {

    private val log = logger()

    fun initialize(applicationContext: Context) {
        log.info("Initializing web service configuration...")
        ServiceConfig.initialize(applicationContext)
        log.info("Web service configuration initialized, APP_KEY: ${ServiceConfig.cfg.appKey}")

        log.info("Configuring WSUser clients...")
        AdminAppService.config.setBaseURL(BuildConfig.API_BASE_URL, BuildConfig.API_CONTEXT)
    }

    fun setLoginCredentials(username: String?, password: String?) {
        if (username != null && password != null && username.isNotEmpty() && password.isNotEmpty()) {
            PreferenceStore.userHash = UserHashUtil.createUserHash(username, password)
        } else {
            PreferenceStore.userHash = ""
        }
        updateAuthorizationKeys()
    }

    fun setDeviceApiKey(deviceApiKey: String) {
        PreferenceStore.deviceApiKey = deviceApiKey
        updateAuthorizationKeys()
    }

    fun updateAuthorizationKeys() {
        log.info("Updating authorization keys...")
        val authKey = PreferenceStore.userHash ?: ""
        val apiKey = PreferenceStore.deviceApiKey ?: ""
        AdminAppService.config.setAuthorization(Authorization.Basic(authKey))
        AdminAppService.config.setHeader("ApiKey", RestHeader("ApiKey", apiKey))

        log.info("Auth Basic: $authKey")
        log.info("Auth ApiKey: $apiKey")
    }
}