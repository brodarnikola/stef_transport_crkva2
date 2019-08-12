package hr.sil.android.seeusvehicle.stationsData

import android.location.Location
import android.widget.Button
import android.widget.Toast
import hr.sil.android.seeusvehicle.App
import hr.sil.android.seeusvehicle.BuildConfig
import hr.sil.android.seeusvehicle.LocationListenerProxy
import hr.sil.android.seeusvehicle.backend.UserUtil
import hr.sil.android.seeusvehicle.cache.DataCache
import hr.sil.android.seeusvehicle.cache.model.DeviceStationData
import hr.sil.android.seeusvehicle.core.remote.W_VehicleApp
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.util.ByteOperations
import hr.sil.android.seeusvehicle.util.LocationUtils
import hr.sil.android.seeusvehicle.view.ui.displayAttention.AttentionVehicleFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

object BusStationDataHandler {
    private val log = logger()

    private const val LOCATION_UPDATE_MIN_TIME = 2000L     //  2 seconds
    private const val LOCATION_UPDATE_MIN_DISTANCE = 15.0f // 15 meters

    private lateinit var btnStop:Button
    private var gpsLocationList: MutableList<Location> = mutableListOf()

    private val locationProxy by lazy {
        LocationListenerProxy(
            context = App.ref,
            locationUpdateMinTime = LOCATION_UPDATE_MIN_TIME,
            locationUpdateMinDistance = LOCATION_UPDATE_MIN_DISTANCE
        ).apply {
            addListener(this@BusStationDataHandler::onLocationUpdate)
        }
    }

    private fun onLocationUpdate(location: Location) {
            handleLocationUpdate(location)
    }

    private val started = AtomicBoolean(false)

    fun start() {
        if (started.compareAndSet(false, true)) {
            locationProxy.start()
        }
    }

    fun setStopButtonReferenceId( buttonStop: Button) {
        btnStop = buttonStop
        btnStop.setOnClickListener {

            App.ref.eventBus.post( GpsLocationData( gpsLocationList ))
            locationProxy.stop()
        }
    }

    fun getLastKnownLocation() = locationProxy.getLastKnownLocation()

    private var previousStation: DeviceStationData? = null

    private fun handleLocationUpdate(location: Location) {
        GlobalScope.launch(Dispatchers.IO) {
            gpsLocationList.add(location)
        }
    }

}