package com.github.sdp.tarjetakuna.ui.collectionexport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet

class ExportCollectionViewModel : ViewModel() {

    // This is a test collection, it will be replaced later by the user's collection
    private val testCollection = listOf(
        MagicCard(
            "MagicCard",
            "A beautiful card",
            MagicLayout.Normal,
            7,
            "{5}{W}{W}",
            MagicSet("MT15", "Magic 2015"),
            56,
            "https://img.scryfall.com/cards/large/front/1/2/12345678.jpg?1562567890"
        ),
        MagicCard(
            "BestMagicCard",
            "An even more beautiful card",
            MagicLayout.Normal,
            7,
            "{7}{W}{W}",
            MagicSet("MT15", "Magic 2015"),
            56,
            "https://img.scryfall.com/cards/large/front/1/2/12345678.jpg?1562567890"
        )
    )


    // LiveData object that holds the user's collection data
    private val _collectionLiveData = MutableLiveData<List<MagicCard>>()
    val collectionLiveData: LiveData<List<MagicCard>>
        get() = _collectionLiveData

    /**
     * Sets the user's collection data in the LiveData object
     */
    fun setCollectionData(collection: List<MagicCard>) {
        _collectionLiveData.value = collection
    }

    /**
     * Sets the test collection data in the LiveData object (temporary)
     */
    fun setCollectionData() {
        _collectionLiveData.value = testCollection
    }
}