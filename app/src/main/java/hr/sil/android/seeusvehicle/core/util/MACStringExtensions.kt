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

package hr.sil.android.seeusvehicle.core.util

import hr.sil.android.util.general.extensions.hexToByteArray


/**
 * @author mfatiga
 */
fun String.macCleanToReal(): String =
        this.chunked(2).joinToString(":").toUpperCase()

fun String.macRealToClean(): String =
        this.replace(":", "").toUpperCase()

fun String.macRealToBytes(): ByteArray =
        this.macRealToClean().hexToByteArray()

fun String.macCleanToBytes(): ByteArray =
        this.hexToByteArray()