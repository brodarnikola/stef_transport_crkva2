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


package hr.sil.android.seeusvehicle.core.remote.base

import android.util.Base64
import hr.sil.android.seeusvehicle.core.remote.model.REncryptRequest
import hr.sil.android.seeusvehicle.core.remote.model.REncryptResponse
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.core.util.macRealToClean
import retrofit2.Call
import retrofit2.Response
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResponse
import ru.gildor.coroutines.retrofit.awaitResult


/**
 * @author mfatiga
 */
abstract class WSBase {

    protected val log = logger()

    private suspend fun <T> wrapAwaitResponse(call: Call<T>, methodName: String): Response<T>? {
        return try {
            val response = call.awaitResponse()
            if (!response.isSuccessful) {
                log.error("Error in $methodName! Code=${response.code()}")
            }
            response
        } catch (exc: Exception) {
            log.error("Error in $methodName!", exc)
            null
        }
    }

    protected suspend fun <T> wrapAwaitData(call: Call<T>, methodName: String, defaultNullValue: T? = null): T? {
        val response = wrapAwaitResponse(call, methodName)
        log.info("Response from backend is: ${methodName} ${response?.code()} ${response?.errorBody().toString()}")
        return if (response?.isSuccessful == true) response.body() ?: defaultNullValue else null
    }

    protected suspend fun <T> wrapAwaitIsSuccessful(call: Call<T>, methodName: String): Boolean {
        val response = wrapAwaitResponse(call, methodName)
        log.info(response.toString())
        return response?.isSuccessful == true
    }

}