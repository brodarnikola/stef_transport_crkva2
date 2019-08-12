package hr.sil.android.seeusvehicle.kiosk

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.app.admin.SystemUpdatePolicy
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.UserManager
import android.provider.Settings
import androidx.annotation.RequiresApi

/**
 * @author mfatiga
 */
object LockTaskModeHandler {
    /* TODO: activity specification in AndroidManifest:

    //starter activity:
    <activity
        android:name=".StarterActivity"
        android:label="@string/app_name" >
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>


    //locked activity
    <activity
        android:name=".LockedActivity"
        android:label="@string/title_activity_locked"
        android:enabled="false">
        <intent-filter>
            <action android:name="android.intent.action.MAIN"/>
            <category android:name="android.intent.category.HOME"/>
            <category android:name="android.intent.category.DEFAULT"/>
        </intent-filter>
    </activity>

    */

    /* TODO: to start lock task:

      //in STARTER activity
      check if isDeviceOwnerApp():
        if returns false:
            showError( ERR_NOT_WHITELISTED )
        else:
            use startLockedActivity() to start the target locked activity
                if startLockedActivity() returns false:
                    showError( ERR_NOT_WHITELISTED )

      //in LOCKED activity
      in onCreate:
        if (isDeviceOwnerActivity()):
            setDefaultCosuPolicies(true)
        else:
            showError( ERR_NOT_DEVICE_OWNER )

      in onStart:
        if (isLockTaskPermitted()): // ignore result??
            startLockTask() // ignore result??

      to stop lock task mode:
        stopLockTask() // ignore result
        setDefaultCosuPolicies(false)

     */

    const val INTENT_EXTRA_STARTED_AS_LOCK = "INTENT_EXTRA_STARTED_AS_LOCK"

    private fun getPackage(context: Context) = context.applicationContext.packageName

    fun isDeviceOwnerApp(context: Context): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return dpm.isDeviceOwnerApp(context.applicationContext.packageName)
    }

    fun startLockedActivity(context: Context, packageManager: PackageManager, targetActivityClass: Class<*>): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return if (dpm.isDeviceOwnerApp(context.applicationContext.packageName)) {
            val lockIntent = Intent(context.applicationContext, targetActivityClass)
            lockIntent.putExtra(INTENT_EXTRA_STARTED_AS_LOCK, true)
            packageManager.setComponentEnabledSetting(
                ComponentName(context.applicationContext, targetActivityClass),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            context.startActivity(lockIntent)
            true
        } else {
            false
        }
    }

    fun isDeviceOwnerActivity(context: Context): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return dpm.isDeviceOwnerApp(context.packageName)
    }

    fun isLockTaskPermitted(activity: Activity): Boolean {
        val dpm = activity.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val appPackage = getPackage(activity)
        return dpm.isLockTaskPermitted(appPackage)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun startLockTask(activity: Activity): Boolean {
        val dpm = activity.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val am = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val adminComponentName = AppDeviceAdminReceiver.getComponentName(activity)
        return if (am.lockTaskModeState == ActivityManager.LOCK_TASK_MODE_NONE) {
            activity.startLockTask()
            true
        } else {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun stopLockTask(activity: Activity): Boolean {
        val dpm = activity.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val am = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val adminComponentName = AppDeviceAdminReceiver.getComponentName(activity)
        return if (am.lockTaskModeState == ActivityManager.LOCK_TASK_MODE_LOCKED) {
            activity.stopLockTask()
            true
        } else {
            false
        }
    }

    // util
    @RequiresApi(Build.VERSION_CODES.M)
    fun setDefaultCosuPolicies(
        activity: Activity,
        active: Boolean
    ) {
        val dpm = activity.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val adminComponentName = AppDeviceAdminReceiver.getComponentName(activity)

        // Set user restrictions
        setUserRestriction(dpm, adminComponentName, UserManager.DISALLOW_SAFE_BOOT, active)
        setUserRestriction(dpm, adminComponentName, UserManager.DISALLOW_FACTORY_RESET, active)
        setUserRestriction(dpm, adminComponentName, UserManager.DISALLOW_ADD_USER, active)
        setUserRestriction(dpm, adminComponentName, UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, active)
        setUserRestriction(dpm, adminComponentName, UserManager.DISALLOW_ADJUST_VOLUME, active)

        // Disable keyguard and status bar
        dpm.setKeyguardDisabled(adminComponentName, active)
        dpm.setStatusBarDisabled(adminComponentName, active)

        // Enable STAY_ON_WHILE_PLUGGED_IN
        enableStayOnWhilePluggedIn(dpm, adminComponentName, active)

        // Set system update policy
        if (active) {
            dpm.setSystemUpdatePolicy(
                adminComponentName,
                SystemUpdatePolicy.createWindowedInstallPolicy(60, 120)
            )
        } else {
            dpm.setSystemUpdatePolicy(
                adminComponentName,
                null
            )
        }

        // set this Activity as a lock task package
        dpm.setLockTaskPackages(
            adminComponentName,
            if (active) arrayOf(activity.packageName) else arrayOf<String>()
        )

        val intentFilter = IntentFilter(Intent.ACTION_MAIN)
        intentFilter.addCategory(Intent.CATEGORY_HOME)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)

        if (active) {
            // set Cosu activity as home intent receiver so that it is started
            // on reboot
            dpm.addPersistentPreferredActivity(
                adminComponentName, intentFilter, ComponentName(
                    activity.packageName, activity::class.java.name
                )
            )
        } else {
            dpm.clearPackagePersistentPreferredActivities(
                adminComponentName, activity.packageName
            )
        }
    }

    private fun setUserRestriction(
        dpm: DevicePolicyManager,
        adminComponentName: ComponentName,
        restriction: String,
        disallow: Boolean
    ) {
        if (disallow) {
            dpm.addUserRestriction(adminComponentName, restriction)
        } else {
            dpm.clearUserRestriction(adminComponentName, restriction)
        }
    }

    private fun enableStayOnWhilePluggedIn(
        dpm: DevicePolicyManager,
        adminComponentName: ComponentName,
        enabled: Boolean
    ) {
        if (enabled) {
            dpm.setGlobalSetting(
                adminComponentName,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                Integer.toString(
                    BatteryManager.BATTERY_PLUGGED_AC
                            or BatteryManager.BATTERY_PLUGGED_USB
                            or BatteryManager.BATTERY_PLUGGED_WIRELESS
                )
            )
        } else {
            dpm.setGlobalSetting(
                adminComponentName,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                "0"
            )
        }
    }
}