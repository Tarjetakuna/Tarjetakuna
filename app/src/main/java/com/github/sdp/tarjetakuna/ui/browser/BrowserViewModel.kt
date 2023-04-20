package com.github.sdp.tarjetakuna.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicCardType
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicRarity
import com.github.sdp.tarjetakuna.model.MagicSet
import java.time.LocalDate

class BrowserViewModel : ViewModel() {

    private val _searchState = MutableLiveData<String>()
    private val _filterState = MutableLiveData<FilterState>()
    private val _sorterState = MutableLiveData<Comparator<MagicCard>>()
    private val _initialCards = MutableLiveData<ArrayList<MagicCard>>()
    val searchState: LiveData<String> = _searchState
    val filterState: LiveData<FilterState> = _filterState
    val sorterState: LiveData<Comparator<MagicCard>> = _sorterState
    val initialCards: LiveData<ArrayList<MagicCard>> = _initialCards

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
     * TODO change it when we have the web api to get the cards
     * Generate cards in order to simulate the display of the cards
     */
    private fun generateCards(): ArrayList<MagicCard> {
        val cardsArray = ArrayList<MagicCard>()
        for (i in 0..39) {
            val name = if (i + 1 < 10) {
                "Magic 190${i + 1}"
            } else {
                "Magic 19${i + 1}"
            }
            val card = MagicCard(
                "Ambush Paratrooper ${i + 1}",
                "Flash, Flying\n5: Creatures you control get +1/+1 until end of turn.",
                MagicLayout.NORMAL,
                2,
                "{1}{W}",
                MagicSet("BRO", name, "Core", "Core Block", LocalDate.of(2019, 3, 10)),
                1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.RARE,
                MagicCardType.CREATURE,
                listOf("Human", "Soldier"),
                "1",
                "2",
                "Vladimir Krisetskiy"
            )
            cardsArray.add(card)
        }

        //Example of an another card
        cardsArray.add(
            MagicCard(
                "Pégase solgrâce",
                "Vol\nLien de vie",
                MagicLayout.NORMAL,
                3,
                "{1}{W}{W}",
                MagicSet("M15", "Magic 2015", "Core", "Core", LocalDate.now()),
                1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.COMMON,
                MagicCardType.CREATURE,
                listOf("Pégase"),
                "1",
                "2",
                "Phill Simmer"
            )
        )
        return cardsArray
    }

    /**
     * Apply the filter and the sorter to the list of cards
     */
    private fun applyFilters(): ArrayList<MagicCard> {
        val cardsArray = generateCards()
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
