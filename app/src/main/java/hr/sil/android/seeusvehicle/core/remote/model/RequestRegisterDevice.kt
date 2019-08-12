package hr.sil.android.seeusvehicle.core.remote.model


import com.google.gson.annotations.SerializedName


class RequestRegisterDevice {
    @SerializedName("mac")
    var mac: String = ""

    @SerializedName("reference")
    var reference: String = ""

    @SerializedName("customerId")
    var customerId: Int = 0

    @SerializedName("registrationPlateNumber")
    var registrationPlateNumber: String = ""

    @SerializedName("deviceToken")
    var deviceToken: String = ""
}
