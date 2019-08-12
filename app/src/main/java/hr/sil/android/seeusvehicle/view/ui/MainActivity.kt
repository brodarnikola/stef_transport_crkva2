package hr.sil.android.seeusvehicle.view.ui

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hr.sil.android.seeusvehicle.App
import hr.sil.android.seeusvehicle.BuildConfig
import hr.sil.android.seeusvehicle.R
import hr.sil.android.seeusvehicle.backend.UserUtil
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.kiosk.LockTaskModeHandler
import hr.sil.android.seeusvehicle.nina.Nina
import hr.sil.android.seeusvehicle.stationsData.BusStationDataHandler
import hr.sil.android.seeusvehicle.view.ui.displayAttention.AttentionVehicleFragment
import hr.sil.android.seeusvehicle.view.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity()  {
    private val log = logger()

    private val fragmentLoaderHandler = Handler()

    private fun loadFragment(fragment: Fragment, fragmentTag: String) {
        if (fragmentTag == "attentionVehicleFragment") {
            fragmentLoaderHandler.post {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.mainFrame, fragment, fragmentTag)
                fragmentTransaction.commitAllowingStateLoss()
            }
        } else if (fragmentTag == "settingsFragment") {
            fragmentLoaderHandler.post {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.mainFrame, fragment, fragmentTag)
                fragmentTransaction.commitAllowingStateLoss()
            }
        } else {
            fragmentLoaderHandler.post {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.mainFrame, fragment, fragmentTag)
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    fun loadAttentionVehicleFragment() {
        val fragment = AttentionVehicleFragment()
        loadFragment(fragment, "attentionVehicleFragment")
    }

    fun loadSettingsFragment() {
        val fragment = SettingsFragment()
        loadFragment(fragment, "settingsFragment")
    }

    private val noWifiFrame: FrameLayout by lazy { findViewById<FrameLayout>(R.id.no_internet_layout) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        toolbarWrapper.setOnClickListener {
        }

        GlobalScope.launch(Dispatchers.Main) {
            //login, load device info, update push token, etc.

                BusStationDataHandler.start()
                tvBusLineNumber.text = UserUtil.device?.reference ?: ""
                loadAttentionVehicleFragment()

        }
    }

    override fun onResume() {
        super.onResume()
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        noWifiFrame.visibility = if (available) View.GONE else View.VISIBLE
    }

    override fun onPause() {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onPause()
    }

}