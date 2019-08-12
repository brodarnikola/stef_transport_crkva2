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

package hr.sil.android.seeusvehicle.core.remote.model

import com.google.gson.annotations.SerializedName

/**
 * @author mfatiga
 */
class REndDeviceInfo {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("mac")
    var mac: String = ""

    @SerializedName("appKey")
    var appKey: String = ""

    @SerializedName("timeCreated")
    var timeCreated: String = ""

    @SerializedName("lastSeen")
    var lastSeen: String = ""

    @SerializedName("reference")
    var reference: String = ""

    @SerializedName("registrationPlateNumber")
    var registrationPlateNumber: String? = null

    @SerializedName("customer___id")
    var customerId: Int = 0

    @SerializedName("isDeleted")
    var isDeleted: Boolean = false

    @SerializedName("deviceToken")
    var deviceToken: String? = null
}