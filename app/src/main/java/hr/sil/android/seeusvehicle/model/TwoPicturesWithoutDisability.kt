package hr.sil.android.seeusvehicle.model

import hr.sil.android.seeusvehicle.model.ItemsAttention

class TwoPicturesWithoutDisability : ItemsAttention {


    private val ITEM_ATTENTION_PICTURES = 0

    override fun getListItemType(): Int {
        return ITEM_ATTENTION_PICTURES
    }

    var numberOfPictures: Int = 0
    var firstOrSecondRowPictures: String = ""
    var busOrStationRequest: String = ""
    var leftPictureName: String = ""
    var rightPictureName: String = ""
}