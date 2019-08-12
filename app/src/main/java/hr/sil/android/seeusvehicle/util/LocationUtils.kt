package hr.sil.android.seeusvehicle.util

import android.location.Location
import hr.sil.android.seeusvehicle.cache.model.DeviceStationData
import hr.sil.android.seeusvehicle.cache.model.DeviceStationPolygonPoint
import hr.sil.android.seeusvehicle.util.geoutil.GeoGeometry


/**
 * @author mfatiga
 */
object LocationUtils {
    private fun getDistance(fromLatitude: Double, fromLongitude: Double, toLatitude: Double, toLongitude: Double): Float {
        val resultArray = FloatArray(3)
        Location.distanceBetween(
            fromLatitude,
            fromLongitude,
            toLatitude,
            toLongitude,
            resultArray
        )
        return resultArray[0]
    }

    private fun toPolygon(boundary: List<DeviceStationPolygonPoint>): Array<DoubleArray> {
        val polygonPoints = Array(boundary.count()) { doubleArrayOf() }
        for (i in polygonPoints.indices) {
            polygonPoints[i] = doubleArrayOf(boundary[i].longitude, boundary[i].latitude)
        }
        return polygonPoints
    }

    private fun isInPolygon(latitude: Double, longitude: Double, polygonLocations: List<DeviceStationPolygonPoint>): Boolean {
        return GeoGeometry.polygonContains(latitude, longitude, arrayOf(toPolygon(polygonLocations)))
    }

    private fun isInRadius(latitude: Double, longitude: Double, stationLatitude: Double, stationLongitude: Double, radiusMeters: Int): Boolean {
        val distance = getDistance(latitude, longitude, stationLatitude, stationLongitude)
        return distance <= radiusMeters.toFloat()
    }

    fun isInStation(latitude: Double, longitude: Double, stationDevice: DeviceStationData): Boolean {
        //radius check
        val radiusMeters = stationDevice.radiusMeters
        val stationLatitude = stationDevice.latitude
        val stationLongitude = stationDevice.longitude

        //polygon check
        val polygonPoints = stationDevice.polygonPoints

        //to calculate with polygon, we need at least 3 points
        return if (polygonPoints != null && polygonPoints.count() > 2) {
            isInPolygon(latitude, longitude, polygonPoints)
        } else {
            isInRadius(latitude, longitude, stationLatitude, stationLongitude, radiusMeters)
        }
    }

    fun isInStation(location: Location, stationDevice: DeviceStationData): Boolean {
        return isInStation(location.latitude, location.longitude, stationDevice)
    }
}