package hr.sil.android.seeusvehicle.kiosk

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * @author mfatiga
 */
class AppDeviceAdminReceiver : DeviceAdminReceiver() {
    /* TODO: register in AndroidManifest

    <receiver android:name=".kiosk.AppDeviceAdminReceiver"
              android:description="@string/app_name"
              android:label="@string/app_name"
              android:permission="android.permission.BIND_DEVICE_ADMIN">
        <meta-data
            android:name="android.app.device_admin"
            android:resource="@xml/device_admin_receiver" />
        <intent-filter>
            <action android:name="android.intent.action.DEVICE_ADMIN_ENABLED"/>
            <action android:name="android.intent.action.PROFILE_PROVISIONING_COMPLETE"/>
            <action android:name="android.intent.action.BOOT_COMPLETED"/>
        </intent-filter>
    </receiver>

     */

    /* TODO: to make the app a device owner

    1) enable usb debugging
    2) run the app
    3) run in shell:
       adb shell dpm set-device-owner hr.sil.android.seeusvehicle/.kiosk.AppDeviceAdminReceiver
    4) then start lock activity
    -------------------------------
    5) the app should start immediately after rebooting the device:
       - wait 10 seconds after starting lock task mode and before rebooting the device

     */

    companion object {
        @JvmStatic
        fun getComponentName(context: Context): ComponentName {
            return ComponentName(context.applicationContext, AppDeviceAdminReceiver::class.java)
        }
    }

    override fun onEnabled(context: Context?, intent: Intent?) {
        super.onEnabled(context, intent)

        //TODO: notify waiting activity that admin is enabled
    }
}