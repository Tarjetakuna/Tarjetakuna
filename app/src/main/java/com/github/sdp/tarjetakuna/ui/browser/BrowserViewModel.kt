package com.github.sdp.tarjetakuna.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.model.MagicCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrowserViewModel : ViewModel() {

    private val _searchState = MutableLiveData<String>()
    private val _filterState = MutableLiveData<FilterState>()
    private val _sorterState = MutableLiveData<Comparator<MagicCard>>()
    private val _initialCards = MutableLiveData<ArrayList<MagicCard>>()
    val searchState: LiveData<String> = _searchState
    val filterState: LiveData<FilterState> = _filterState
    val sorterState: LiveData<Comparator<MagicCard>> = _sorterState
    val initialCards: LiveData<ArrayList<MagicCard>> = _initialCards


    var localDatabase: AppDatabase? = null

    // The cards that are displayed in the recycler view
    private val _cards: MutableLiveData<ArrayList<MagicCard>> =
        MutableLiveData<ArrayList<MagicCard>>()
    val cards: LiveData<ArrayList<MagicCard>> = _cards

    var cardsArray: ArrayList<MagicCard> = arrayListOf()

    init {
        _searchState.value = ""
        _filterState.value = FilterState()
        _sorterState.value = Comparator { card1, card2 -> card1.name.compareTo(card2.name) }
        _initialCards.value = applyFilters()
    }

    /**
     * Change the state of the search bar
     */
    fun setSearchState(searchState: String) {
        _searchState.value = searchState
    }

    /**
     * Clear the filters
     */
    fun clearFilters() {
        _filterState.value = FilterState()
    }

    /**
     * Apply the mana filter to the cards
     */
    fun setManaFilter(manaFilter: Int?) {
        _filterState.value = _filterState.value!!.copy(manaFilter = manaFilter)
    }

    /**
     * Apply the set filter to the cards
     */
    fun setSetFilter(setFilter: String?) {
        _filterState.value = _filterState.value!!.copy(setFilter = setFilter)
    }

    /**
     * Change the state of the sorter
     */
    fun setSorterState(sorterState: Comparator<MagicCard>) {
        _sorterState.value = sorterState
    }

    /**
     * Apply the filters to the cards
     */
    fun setInitialCards() {
        _initialCards.value = applyFilters()
    }

    /**
     * Get the cards from the local database
     */
    fun getCardsFromDatabase() {
        val cardsArray = ArrayList<MagicCard>()
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val cards = localDatabase?.magicCardDao()?.getAllCards()
                if (cards != null) {
                    for (card in cards) {
                        if (card.possession == CardPossession.OWNED) {
                            cardsArray.add(card.toMagicCard())
                        }
                    }
                }
            }
        }.invokeOnCompletion {
            cardsArray.sortedWith { o1: MagicCard, o2: MagicCard ->
                o1.name.compareTo(o2.name)
            }
            this.cardsArray = cardsArray
            _cards.value = cardsArray
        }
    }

    /**
     * Apply the filter and the sorter to the list of cards
     */
    private fun applyFilters(): ArrayList<MagicCard> {
        val searchState = searchState.value!!
        val filterState = filterState.value!!
        val sorterState = sorterState.value!!
        val filteredArray = filterState.filter(cardsArray)

        val nameFilteredArray = filteredArray.filter {
            it.name.contains(
                searchState,
                true
            )
        }
        return ArrayList(nameFilteredArray.sortedWith(sorterState))
    }

}
