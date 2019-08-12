package hr.sil.android.seeusvehicle.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import hr.sil.android.seeusvehicle.R
import hr.sil.android.seeusvehicle.backend.UserUtil
import hr.sil.android.seeusvehicle.core.util.AppUtil
import hr.sil.android.seeusvehicle.fcm.FCMService
import hr.sil.android.seeusvehicle.nina.Nina
import hr.sil.android.seeusvehicle.stationsData.BusStationDataHandler
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvBusLineNumber: TextView? = activity?.findViewById(R.id.tvBusLineNumber)
        tvBusLineNumber?.visibility = View.GONE

        val tvLineHardCoded: TextView? = activity?.findViewById(R.id.tvLineHardCoded)
        tvLineHardCoded?.visibility = View.GONE

        val tvCurrentBusStationName: TextView? = activity?.findViewById(R.id.tvCurrentBusStationName)
        tvCurrentBusStationName?.visibility = View.GONE

        val rlGear: RelativeLayout? = activity?.findViewById(R.id.rlGear)
        rlGear?.visibility = View.GONE

        btnConfirm.setOnClickListener {
            if (AppUtil.isInternetAvailable()) {
                val vehicleMacAddress = Nina.mac
                val vehiclePlateNumber = etVehiclePlateNumber.text.toString().trim()
                if (vehiclePlateNumber.isNotBlank()) {
                    if (vehicleMacAddress != null && vehicleMacAddress.isNotBlank()) {
                        progresbar.visibility = View.VISIBLE
                        GlobalScope.launch {
                            val pushToken = FCMService.token
                            if (pushToken != null) {
                                val isRegistrationSuccessful = UserUtil.registerDevice(
                                    mac = vehicleMacAddress,
                                    referenceId = etVehicleReferenceId.text.toString().trim(),
                                    registrationPlateNumber = etVehiclePlateNumber.text.toString().trim(),
                                    pushToken = pushToken
                                )

                                withContext(Dispatchers.Main) {
                                    progresbar.visibility = View.GONE
                                    if (isRegistrationSuccessful) {
                                        BusStationDataHandler.start()
                                        tvBusLineNumber?.text = UserUtil.device?.reference ?: ""
                                        (activity as? MainActivity?)?.loadAttentionVehicleFragment()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Maybe this register vehicle plate number already exists or something went wrong, please try again latter.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                withContext(Dispatchers.Main) {

                                    Toast.makeText(
                                        context,
                                        "Push token invalid!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "USB device not ready!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Registration plate is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val snackBarText = "You don't have internet connection"
                AppUtil.showSnackBar(it, requireContext(), snackBarText)
            }
        }
    }
}