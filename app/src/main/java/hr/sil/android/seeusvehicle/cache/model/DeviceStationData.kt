package hr.sil.android.seeusvehicle.cache.model

import com.google.gson.annotations.SerializedName

class DeviceStationData {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("mac")
    var mac: String = ""

    @SerializedName("station___name")
    var stationName: String = ""

    @SerializedName("latitude")
    var latitude: Double = 0.0

    @SerializedName("longitude")
    var longitude: Double = 0.0

    @SerializedName("radiusMeters")
    var radiusMeters: Int = 0

    @SerializedName("polygon")
    var polygonPoints: List<DeviceStationPolygonPoint>? = null
}