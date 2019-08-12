package hr.sil.android.seeusvehicle.view.ui.displayAttention


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.sil.android.seeusvehicle.R
import kotlinx.android.synthetic.main.attention_text.view.*
import androidx.core.content.ContextCompat
import hr.sil.android.seeusvehicle.core.util.SettingsHelper
import hr.sil.android.seeusvehicle.model.*
import hr.sil.android.seeusvehicle.util.ByteOperations
import kotlinx.android.synthetic.main.attention_two_pictures.view.*


class AttentionVehicleAdapter(
    private val items: MutableList<ItemsAttention>,
    private val context: AttentionVehicleFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    enum class ITEM_TYPES(val typeValue: Int) {
        ITEM_TWO_PICTURES_WITHOUT_DISABILITY(0),
        ITEM_TEXT(1),
        ITEM_TWO_PICTURES_WITH_DISABILITY(2),
        ITEM_DISABILITY_PICTURE(3);

        companion object {
            fun from(findValue: Int): ITEM_TYPES = ITEM_TYPES.values().first { it.typeValue == findValue }
        }
    }


    override fun getItemViewType(position: Int): Int {

        return ITEM_TYPES.from(items.get(position).getListItemType()).typeValue
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.attention_two_pictures, parent, false)
            return TwoPicturesWithoutDisabilityHolder(view)
        }
        else if (viewType == 1) {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.attention_text, parent, false)
            return TextHolder(view)
        }
        else if (viewType == 2) {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.attention_two_pictures, parent, false)
            return TwoPicturesWithDisabilityHolder(view)
        }
        else {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.attentions_disabillity, parent, false)
            return DisabillityHolder(view)
        }
    }


    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    inner class TwoPicturesWithoutDisabilityHolder(itemView: View) : ViewHolder(itemView) {

        val leftPicture: ImageView = itemView.imageViewLeftPicture
        val rightPicture: ImageView = itemView.imageViewRightPicture

        fun bindItem(boardinSupport: TwoPicturesWithoutDisability) {

            val isStopFromBackend = ByteOperations.isStopBitSet(SettingsHelper.globalBackendData)
            val isStopFromUsb = ByteOperations.isStopBitSet(SettingsHelper.globalUsbData)

            if( isStopFromBackend  && ( isStopFromUsb || SettingsHelper.convertStopBitFromSub ) ) {

                leftPicture.visibility = View.VISIBLE
                rightPicture.visibility = View.VISIBLE
                leftPicture.setImageResource(R.drawable.ic_attention_from_bus_stop)
                rightPicture.setImageResource(R.drawable.ic_attention_vehicle)
            }
            else if( isStopFromBackend ) {

                leftPicture.visibility = View.VISIBLE
                leftPicture.setImageResource(R.drawable.ic_attention_from_bus_stop)
                rightPicture.visibility = View.GONE
            }
            else if( ( isStopFromUsb || SettingsHelper.convertStopBitFromSub ) ) {

                leftPicture.visibility = View.VISIBLE
                leftPicture.setImageResource(R.drawable.ic_attention_vehicle)
                rightPicture.visibility = View.GONE
            }
        }
    }

    inner class TwoPicturesWithDisabilityHolder(itemView: View) : ViewHolder(itemView) {

        val leftPicture: ImageView = itemView.imageViewLeftPicture
        val rightPicture: ImageView = itemView.imageViewRightPicture

        fun bindItem(boardinSupport: TwoPicturesWithDisability) {

            val isDisabilityFromBackend = ByteOperations.isDisabilityBitSet(SettingsHelper.globalBackendData)
            val isDisabilityFromUsb = ByteOperations.isDisabilityBitSet(SettingsHelper.globalUsbData)

            if( isDisabilityFromBackend  && isDisabilityFromUsb ) {

                leftPicture.visibility = View.VISIBLE
                rightPicture.visibility = View.VISIBLE
                leftPicture.setImageResource(R.drawable.ic_boarding_support)
                rightPicture.setImageResource(R.drawable.ic_de_boarding_support)
            }
            else if( isDisabilityFromBackend ) {

                leftPicture.visibility = View.VISIBLE
                leftPicture.setImageResource(R.drawable.ic_boarding_support)
                rightPicture.visibility = View.GONE
            }
            else if( isDisabilityFromUsb ) {

                leftPicture.visibility = View.VISIBLE
                leftPicture.setImageResource(R.drawable.ic_de_boarding_support)
                rightPicture.visibility = View.GONE
            }
        }
    }

    inner class TextHolder(itemView: View) : ViewHolder(itemView) {

        val name: TextView = itemView.tvText

        fun bindItem(keyObject: VehicleText) {
            name.text = keyObject.attentionBoardingSupport
        }
    }

    inner class DisabillityHolder(itemView: View) : ViewHolder(itemView) {

        val imageViewIcon1: ImageView = itemView.findViewById(R.id.imageViewIcon1)
        val imageViewIcon2: ImageView = itemView.findViewById(R.id.imageViewIcon2)
        val imageViewIcon3: ImageView = itemView.findViewById(R.id.imageViewIcon3)
        val imageViewIcon4: ImageView = itemView.findViewById(R.id.imageViewIcon4)
        val imageViewIcon5: ImageView = itemView.findViewById(R.id.imageViewIcon5)
        val imageViewIcon6: ImageView = itemView.findViewById(R.id.imageViewIcon6)

        fun bindItem(keyObject: AttentionDisability) {

            var counter = 0
            for( items in keyObject.listDisability ) {

                if( counter == 0 ) {
                    drawDisabilityPictureToPosition( items.indexOfPicture, imageViewIcon1 )
                }
                else if( counter == 1 ) {

                    drawDisabilityPictureToPosition( items.indexOfPicture, imageViewIcon2 )
                }
                else if( counter == 2 ) {

                    drawDisabilityPictureToPosition( items.indexOfPicture, imageViewIcon3 )
                }
                else if( counter == 3 ) {

                    drawDisabilityPictureToPosition( items.indexOfPicture, imageViewIcon4 )
                }
                else if( counter == 4 ) {

                    drawDisabilityPictureToPosition( items.indexOfPicture, imageViewIcon5 )
                }
                else if( counter == 5 ) {

                    drawDisabilityPictureToPosition( items.indexOfPicture, imageViewIcon6 )
                }

                counter++
            }
        }


        private fun drawDisabilityPictureToPosition(position: Int, imageViewIcon: ImageView) {

            val icon: Int = if (position == 2) {
                R.drawable.ic_disability_other
            } else if (position == 3) {
                R.drawable.ic_disability_blind_or_partially_sighted
            } else if (position == 4) {
                R.drawable.ic_disability_speech_disorder
            } else if (position == 5) {
                R.drawable.ic_disability_hard_of_hearing
            } else if (position == 6) {
                R.drawable.ic_disability_wheelchair
            } else if (position == 7) {
                R.drawable.ic_disability_children
            } else {
                return
            }

            imageViewIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, icon))
            imageViewIcon.visibility = View.VISIBLE
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val viewType = items[position]
        when (viewType.getListItemType()) {
            ITEM_TYPES.ITEM_TWO_PICTURES_WITHOUT_DISABILITY.typeValue -> {

                val childItem = items[position] as TwoPicturesWithoutDisability
                holder as TwoPicturesWithoutDisabilityHolder
                holder.bindItem(childItem)
            }
            ITEM_TYPES.ITEM_TEXT.typeValue -> {

                val childItem = items[position] as VehicleText
                holder as TextHolder
                holder.bindItem(childItem)
            }
            ITEM_TYPES.ITEM_TWO_PICTURES_WITH_DISABILITY.typeValue -> {

                val childItem = items[position] as TwoPicturesWithDisability
                holder as TwoPicturesWithDisabilityHolder
                holder.bindItem(childItem)
            }
            ITEM_TYPES.ITEM_DISABILITY_PICTURE.typeValue -> {

                val childItem = items[position] as AttentionDisability
                holder as DisabillityHolder
                holder.bindItem(childItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}