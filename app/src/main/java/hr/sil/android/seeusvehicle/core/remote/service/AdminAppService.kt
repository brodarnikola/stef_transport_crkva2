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

package hr.sil.android.seeusvehicle.core.remote.service


import hr.sil.android.rest.core.factory.RestServiceAccessor
import hr.sil.android.seeusvehicle.cache.model.DeviceStationData
import hr.sil.android.seeusvehicle.core.remote.model.REndAdminInfo
import hr.sil.android.seeusvehicle.core.remote.model.REndDeviceInfo
import hr.sil.android.seeusvehicle.core.remote.model.RequestRegisterDevice
import hr.sil.android.seeusvehicle.core.remote.model.RequestUpdateDevice
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


/**
 * @author mfatiga
 */
interface AdminAppService {
    companion object : RestServiceAccessor<AdminAppService>(AdminAppService::class) {
        //auth: Basic
        private const val ENDPOINT_PREFIX = "vehicleApp/"
    }

    @GET(ENDPOINT_PREFIX + "admin/login")
    fun login(): Call<REndAdminInfo>

    @GET(ENDPOINT_PREFIX + "admin/account/info")
    fun getAccountInfo(): Call<REndAdminInfo>

    @POST(ENDPOINT_PREFIX + "admin/registerDevice")
    fun registerDevice(@Body deviceInfo: RequestRegisterDevice): Call<String>

    @GET(ENDPOINT_PREFIX + "device/info")
    fun getDeviceInfo(): Call<REndDeviceInfo>

    @POST(ENDPOINT_PREFIX + "device/update")
    fun getDeviceUpdate(@Body deviceUpdate: RequestUpdateDevice): Call<REndDeviceInfo>

    @GET(ENDPOINT_PREFIX + "deviceStations")
    fun getDeviceStationData(): Call<List<DeviceStationData>>

    @GET(ENDPOINT_PREFIX + "device/approachAndGetStopRequestsByMac/{stationMac}/{lineNumber}/{periodSeconds}")
    fun getDeviceStopRequestMac(
        @Path("stationMac") stationMac: String,
        @Path("lineNumber") lineNumber: String,
        @Path("periodSeconds") periodSeconds: String
    ): Call<String>

    @GET(ENDPOINT_PREFIX + "device/exitStationByMac/{stationMac}")
    fun getExitStationByMac(@Path("stationMac") stationMac: String): Call<String>
}