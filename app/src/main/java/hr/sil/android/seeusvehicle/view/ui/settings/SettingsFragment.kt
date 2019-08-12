package hr.sil.android.seeusvehicle.view.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import hr.sil.android.seeusvehicle.R
import hr.sil.android.seeusvehicle.backend.UserUtil
import hr.sil.android.seeusvehicle.cache.DataCache
import hr.sil.android.seeusvehicle.core.util.AppUtil
import hr.sil.android.seeusvehicle.nina.Nina
import hr.sil.android.seeusvehicle.util.ByteOperations
import hr.sil.android.seeusvehicle.view.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {
    private val NAME = "VehicleSettings"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etBusReferenceId.setText( UserUtil.device?.reference )
        etRegistrationPlateNumber.setText( UserUtil.device?.registrationPlateNumber )

        val rlGear: RelativeLayout? = this@SettingsFragment.activity?.findViewById(R.id.rlGear)
        rlGear?.visibility = View.VISIBLE
        rlGear?.setOnClickListener {
            (activity as? MainActivity?)?.loadAttentionVehicleFragment()
        }

        val btnConfirm: Button = view.findViewById(R.id.btnConfirm) as Button
        btnConfirm.setOnClickListener {
            GlobalScope.launch {
                val isUpdateSuccessful = UserUtil.updateDevice(
                    referenceId = etBusReferenceId.text.toString().trim(),
                    registrationPlateNumber = etRegistrationPlateNumber.text.toString().trim()
                )

                withContext(Dispatchers.Main) {
                    val resultText =
                        if (isUpdateSuccessful) "Vehicle has been successfully updated"
                        else "Vehicle update failed!"
                    AppUtil.showSnackBar(it, requireContext(), resultText)

                    (activity as? MainActivity?)?.loadAttentionVehicleFragment()
                }
            }
        }

        val tvUpdateStation: TextView = view.findViewById(R.id.tvUpdateStation) as TextView
        tvUpdateStation.setOnClickListener {
            progresbar.visibility = View.VISIBLE
            btnConfirm.isClickable = false
            btnClearSharedPreference.isClickable = false
            rlGear?.isClickable = false

            GlobalScope.launch(Dispatchers.Default) {
                try {
                    DataCache.getDeviceStations(true)

                    withContext(Dispatchers.Main) {
                        progresbar.visibility = View.GONE

                        btnConfirm.isClickable = true
                        btnClearSharedPreference.isClickable = true
                        rlGear?.isClickable = true

                        AppUtil.showSnackBar(it, requireContext(),"Stations have been successfully updated")
                    }
                } catch (ex: Exception) {
                    AppUtil.log.error("Something went wrong...", ex)
                }
            }
        }

        btnClearSharedPreference.setOnClickListener {
            val prefs = context?.getSharedPreferences(NAME, Context.MODE_PRIVATE)
            val editor = prefs?.edit()
            editor?.clear()
            editor?.commit()
            ByteOperations.resetClearAdvertisment()
            (activity as? MainActivity?)?.loadAttentionVehicleFragment()

        }
    }
}
