package hr.sil.android.seeusvehicle.model


class AttentionDisability : ItemsAttention {


    private val ITEM_DISABILITY = 3

    override fun getListItemType(): Int {
        return ITEM_DISABILITY
    }

    var disabilityName: String = String()

    var indexOfPicture: Int = 0
    var listDisability: MutableList<AttentionDisability> = mutableListOf()

}