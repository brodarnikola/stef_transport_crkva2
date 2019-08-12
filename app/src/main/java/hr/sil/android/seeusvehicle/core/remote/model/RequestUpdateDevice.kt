package hr.sil.android.seeusvehicle.core.remote.model

import com.google.gson.annotations.SerializedName

class RequestUpdateDevice {
    @SerializedName("reference")
    var reference: String = ""

    @SerializedName("registrationPlateNumber")
    var registrationPlateNumber: String = ""

    @SerializedName("deviceToken")
    var deviceToken: String = ""
}