package hr.sil.android.seeusvehicle

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import hr.sil.android.seeusvehicle.core.util.logger

/**
 * @author mfatiga
 */
class LocationListenerProxy(
    private val context: Context,
    private val locationUpdateMinTime: Long = 0L,
    private val locationUpdateMinDistance: Float = 0.0f
) : LocationListener {
    private val log = logger()

    companion object {
        const val GPS_WAIT_TIME = 10_000L
    }

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var lastKnownLocation: Location? = null

    @Volatile
    var enabled = false
        private set

    fun start(): Boolean {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            synchronized(this) {
                if (!enabled) {
                    var success = false
                    val requestedProviders = mutableListOf<String>()
                    for (provider in locationManager.getProviders(false)) {
                        if (provider == LocationManager.GPS_PROVIDER || provider == LocationManager.NETWORK_PROVIDER) {
                            success = true
                            locationManager.requestLocationUpdates(provider, locationUpdateMinTime, locationUpdateMinDistance, this)
                            requestedProviders.add(provider)
                        }
                    }

                    if (success) enabled = true

                    return success
                } else {
                    return true
                }
            }
        } else {
            return false
        }
    }

    fun stop() {
        synchronized(this) {
            if (enabled) {
                locationManager.removeUpdates(this)
                enabled = false
            }
        }
    }

    private fun getLastKnownLocationFromProxy(): Location? {
        return if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else null
    }

    fun getLastKnownLocation(): Location? {
        if (lastKnownLocation == null) {
            lastKnownLocation = getLastKnownLocationFromProxy()
        }
        return lastKnownLocation
    }

    private var lastGpsTimestamp: Long = 0L
    private fun shouldIgnoreLocation(provider: String, timestamp: Long): Boolean {
        if (LocationManager.GPS_PROVIDER == provider) {
            lastGpsTimestamp = timestamp
        } else {
            if (timestamp - lastGpsTimestamp < GPS_WAIT_TIME) {
                return true
            }
        }
        return false
    }

    override fun onLocationChanged(location: Location) {
        //ignore temporary non-gps fix
        if (shouldIgnoreLocation(location.provider, System.currentTimeMillis())) {
            log.info("Ignoring temporary non-GPS fix...")
        } else {
            //store last known location
            lastKnownLocation = location

            //notify listeners
            notifyListeners(location)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}

    private val locationListeners = mutableMapOf<String, (Location) -> Unit>()
    fun addListener(listener: (Location) -> Unit): String {
        val key = java.util.UUID.randomUUID().toString()
        locationListeners[key] = listener
        return key
    }

    fun removeListener(key: String) {
        locationListeners.remove(key)
    }

    private fun notifyListeners(location: Location) {
        locationListeners.values.forEach { it(location) }
    }
}