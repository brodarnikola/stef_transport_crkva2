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
class REndAdminInfo {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("username")
    var username: String = ""

    @SerializedName("password")
    var password: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("role")
    var role: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("time_created")
    var timeCreated: String = ""

    @SerializedName("customer___id")
    var customerId: Int = 0

    @SerializedName("customer___name")
    var customerName: String = ""

    @SerializedName("language___id")
    var languageId: Int = 0

    @SerializedName("status")
    var status: String = ""
}
