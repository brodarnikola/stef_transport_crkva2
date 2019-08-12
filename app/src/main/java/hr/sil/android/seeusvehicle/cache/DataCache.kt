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

package hr.sil.android.seeusvehicle.cache

import hr.sil.android.datacache.AutoCache
import hr.sil.android.datacache.TwoLevelCache
import hr.sil.android.datacache.updatable.CacheSource
import hr.sil.android.seeusvehicle.App
import hr.sil.android.seeusvehicle.cache.model.DeviceStationData
import hr.sil.android.seeusvehicle.core.remote.W_VehicleApp
import java.util.concurrent.TimeUnit

object DataCache {
    private val deviceStationUnitCache by lazy {
        AutoCache.Builder(
            TwoLevelCache
                .Builder(DeviceStationData::class, DeviceStationData::id)
                .memoryLruMaxSize(100)
                .build(App.ref)
        )
            .enableNetworkChecking(App.ref)
            .setFullSource(CacheSource.ForCache.Suspendable(24, TimeUnit.HOURS) {
                W_VehicleApp.getDeviceStationData()
            })
            .build()
    }

    suspend fun getDeviceStations(awaitUpdate: Boolean = false): Collection<DeviceStationData> {
        return deviceStationUnitCache.getAll(awaitUpdate)
    }

    suspend fun getDeviceStation(id: Int, awaitUpdate: Boolean = false): DeviceStationData? {
        return deviceStationUnitCache.get(id, awaitUpdate)
    }
}

