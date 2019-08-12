package hr.sil.android.seeusvehicle.model


class VehicleText : ItemsAttention {


    private val ITEM_ATTENTION_TEXT = 1

    override fun getListItemType(): Int {
        return ITEM_ATTENTION_TEXT
    }

    var attentionBoardingSupport: String = ""

}