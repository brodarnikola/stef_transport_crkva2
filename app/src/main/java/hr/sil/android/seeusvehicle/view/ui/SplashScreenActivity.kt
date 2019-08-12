package hr.sil.android.seeusvehicle.view.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hr.sil.android.seeusvehicle.BuildConfig
import hr.sil.android.seeusvehicle.R
import hr.sil.android.seeusvehicle.core.util.AppUtil
import hr.sil.android.seeusvehicle.kiosk.LockTaskModeHandler
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 2500
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    private var startupBeginTimestamp: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        startupBeginTimestamp = System.currentTimeMillis()

        startApp(true)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private var permissionRequestGranted = false
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequestGranted =
            requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED
        startApp()
    }

    private suspend fun checkSplashDelay() {
        val duration = System.currentTimeMillis() - startupBeginTimestamp
        if (duration < SPLASH_DISPLAY_LENGTH) {
            delay(SPLASH_DISPLAY_LENGTH - duration)
        }
    }

    private fun checkPermissions(): Boolean {
        return if (permissionRequestGranted) true
        else ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startApp(isInitial: Boolean = false) {
        if (!checkPermissions()) {
            if (isInitial) {
                requestPermission()
            }
            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            checkSplashDelay()

            @Suppress("ConstantConditionIf")
            if (BuildConfig.KIOSK_MODE_ENABLED) {
                if (LockTaskModeHandler.startLockedActivity(
                        this@SplashScreenActivity,
                        packageManager,
                        MainActivity::class.java
                    )
                ) {
                    finish()
                }
            } else {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}
