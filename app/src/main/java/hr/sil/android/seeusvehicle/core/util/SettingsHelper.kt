package hr.sil.android.seeusvehicle.core.util

import android.content.Context
import android.content.SharedPreferences

object SettingsHelper {

    private const val NAME = "VehicleSettings"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    // list of app specific preferences
    private val GLOBAL_BACKEND_DATA = Pair("global_backend_data", "00000000")
    private val GLOBAL_USB_DATA = Pair("global_usb_data", "00000000")

    private val CONVERT_STOP_BIT_FROM_USB = Pair("convert_stop_bit_from_usb", false)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var globalBackendData: Byte
        get() {
            val str = preferences.getString(GLOBAL_BACKEND_DATA.first, GLOBAL_BACKEND_DATA.second)
            return ((str.toIntOrNull() ?: 0) and 0xFF).toByte()
        }
        set(value) = preferences.edit {
            it.putString(GLOBAL_BACKEND_DATA.first, value.toInt().toString())
        }

    var globalUsbData: Byte
        get() {
            val str = preferences.getString(GLOBAL_USB_DATA.first, GLOBAL_USB_DATA.second)
            return ((str.toIntOrNull() ?: 0) and 0xFF).toByte()
        }
        set(value) = preferences.edit {
            it.putString(GLOBAL_USB_DATA.first, value.toInt().toString())
        }

    var convertStopBitFromSub: Boolean
        get() = preferences.getBoolean(CONVERT_STOP_BIT_FROM_USB.first, CONVERT_STOP_BIT_FROM_USB.second)
        set(value) = preferences.edit {
            it.putBoolean(CONVERT_STOP_BIT_FROM_USB.first, value)
        }
}