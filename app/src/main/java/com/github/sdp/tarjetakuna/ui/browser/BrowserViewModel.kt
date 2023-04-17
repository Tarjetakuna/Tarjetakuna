package com.github.sdp.tarjetakuna.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.model.MagicCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrowserViewModel : ViewModel() {

    var localDatabase: AppDatabase? = null

    // The cards that are displayed in the recycler view
    private val _cards: MutableLiveData<ArrayList<MagicCard>> = MutableLiveData()
    val cards: LiveData<ArrayList<MagicCard>> = _cards

    /**
     * Get the cards from the local database
     */
    fun getCardsFromDatabase() {
        val cardsArray = ArrayList<MagicCard>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val cards = localDatabase?.magicCardDao()?.getAllCards()
                if (cards != null) {
                    for (card in cards) {
                        cardsArray.add(card.toMagicCard())
                    }
                }
            }
        }.invokeOnCompletion {
            _cards.value = cardsArray
        }
    }
}
