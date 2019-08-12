package hr.sil.android.seeusvehicle.core.remote.model

import com.google.gson.annotations.SerializedName

class StopRequestReferenceId {

    @SerializedName("stationMac")
    var stationReferenceId: Int = 0

    @SerializedName("lineNumber")
    var lineNumber: Int = 0

    @SerializedName("periodSeconds")
    var periodSeconds: Int = 0
}