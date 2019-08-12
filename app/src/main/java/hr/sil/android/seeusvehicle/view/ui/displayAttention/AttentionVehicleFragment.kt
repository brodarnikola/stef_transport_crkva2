package hr.sil.android.seeusvehicle.view.ui.displayAttention


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.sil.android.seeusvehicle.App
import hr.sil.android.seeusvehicle.R
import hr.sil.android.seeusvehicle.core.util.SettingsHelper
import hr.sil.android.seeusvehicle.core.util.logger
import hr.sil.android.seeusvehicle.fcm.BackendPushDataEvent
import hr.sil.android.seeusvehicle.model.*
import hr.sil.android.seeusvehicle.view.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_attention_vehicle.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.app.Activity
import android.location.Location
import android.widget.Button
import hr.sil.android.seeusvehicle.backend.UserUtil

import hr.sil.android.seeusvehicle.BuildConfig
import hr.sil.android.seeusvehicle.stationsData.*
import hr.sil.android.seeusvehicle.util.ByteOperations
import hr.sil.android.util.general.extensions.format


class AttentionVehicleFragment : Fragment() {
    private val log = logger()

    private lateinit var adapter: AttentionVehicleAdapter

    lateinit var btnStop: Button


    var finalStopRequestItems: MutableList<ItemsAttention> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attention_vehicle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnStop = view.findViewById(R.id.btnStop)
        BusStationDataHandler.setStopButtonReferenceId(btnStop)

        val tvBusLineNumber: TextView? = this@AttentionVehicleFragment.activity?.findViewById(R.id.tvBusLineNumber)
        tvBusLineNumber?.visibility = View.VISIBLE
        if( UserUtil.device != null )
            tvBusLineNumber?.text = UserUtil.device?.reference

        val tvCurrentBusStationName: TextView? =
            this@AttentionVehicleFragment.activity?.findViewById(R.id.tvCurrentBusStationName)
        tvCurrentBusStationName?.visibility = View.VISIBLE


        val tvLineHardCoded: TextView? = activity?.findViewById(R.id.tvLineHardCoded)
        tvLineHardCoded?.visibility = View.VISIBLE

        val rlGear: RelativeLayout? = this@AttentionVehicleFragment.activity?.findViewById(R.id.rlGear)
        rlGear?.visibility = View.VISIBLE
        rlGear?.setOnClickListener {
            (activity as? MainActivity?)?.loadSettingsFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        App.ref.eventBus.register(this)

    }

    override fun onPause() {
        super.onPause()
        App.ref.eventBus.unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun displayTotalDistance(gpsLocationList: GpsLocationData) {
        try {

            var location: Location? = null
            var totalDistance = 0.0f
            for( items in gpsLocationList.locationData ) {

                if( location != null ) {
                    totalDistance = totalDistance + location.distanceTo(items)
                }
                location = items
            }

            totalDistance = totalDistance / 1000
            dbgTextView.visibility = View.VISIBLE
            dbgTextView.setText("Total distance is: " + totalDistance)

            ivSeeUsLogo.visibility = View.VISIBLE

            attentionVehicleRecyclerView.visibility = View.GONE
            mainLayoutFragment.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))

        } catch (ex: Exception) {
            log.error("Something went wrong...", ex)
        }
    }


}
