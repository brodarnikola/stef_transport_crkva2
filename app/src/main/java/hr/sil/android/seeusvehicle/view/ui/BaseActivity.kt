package hr.sil.android.seeusvehicle.view.ui

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.esotericsoftware.minlog.Log
import hr.sil.android.seeusvehicle.core.connectivity.NetworkChecker
import hr.sil.android.seeusvehicle.core.connectivity.NinaChecker

abstract class BaseActivity : AppCompatActivity() {
    private var networkCheckerListenerKey: String? = null
    private var ninaCheckerListenerKey: String? = null

    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun onResume() {
        super.onResume()

        if (networkCheckerListenerKey == null) {
            networkCheckerListenerKey = NetworkChecker.addListener { available ->
                uiHandler.post { onNetworkStateUpdated(available) }
            }
        }

        if (ninaCheckerListenerKey == null) {
            ninaCheckerListenerKey = NinaChecker.addListener { ready ->
                uiHandler.post { onNinaReadyUpdated(ready) }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.info("onPause()")

        networkCheckerListenerKey?.let { NetworkChecker.removeListener(it) }
        networkCheckerListenerKey = null
    }

    open fun onNetworkStateUpdated(available: Boolean) {}
    open fun onNinaReadyUpdated(ready: Boolean) {}
}