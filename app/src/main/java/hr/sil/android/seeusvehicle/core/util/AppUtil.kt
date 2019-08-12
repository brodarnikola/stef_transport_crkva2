package hr.sil.android.seeusvehicle.core.util

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import hr.sil.android.seeusvehicle.App
import hr.sil.android.seeusvehicle.R

object AppUtil {
    val log = logger()

    fun isInternetAvailable(): Boolean {
        try {
            val cm = App.ref.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        } catch (e: SecurityException) {
            log.error("Please check if you grant ACCESS_NETWORK_STATE, or put insights = false in App init!")
            return false
        }
    }

    fun showSnackBar(view: View, context: Context, text: String) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.colorRed
            )
        )
        val textView =
            snackbarView.findViewById(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 17f
        textView.gravity = Gravity.CENTER
        snackbar.show()
    }
}

