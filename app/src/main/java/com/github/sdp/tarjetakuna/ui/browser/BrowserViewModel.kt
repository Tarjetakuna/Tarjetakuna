package com.github.sdp.tarjetakuna.ui.browser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.*

class BrowserViewModel : ViewModel() {

    private val _filterState = MutableLiveData<FilterState>()
    private val _initialCards = MutableLiveData<ArrayList<MagicCard>>()
    val filterState: MutableLiveData<FilterState> = _filterState
    val initialCards: MutableLiveData<ArrayList<MagicCard>> = _initialCards

    init {
        _filterState.value = FilterState()
        _initialCards.value = applyFilter(filterState.value!!)
    }

    fun setFilterState(filterState: FilterState) {
        _filterState.value = filterState
    }

    fun updateFilterType(filterType: FilterType) {
        _filterState.value = FilterState(filterType, _filterState.value!!.filterValue)
    }

    fun updateFilterValue(filterValue: String) {
        _filterState.value = FilterState(_filterState.value!!.filterType, filterValue)
    }

    fun setInitialCards(filterState: FilterState) {
        _initialCards.value = applyFilter(filterState)
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
                MagicLayout.Normal,
                2,
                "{1}{W}",
                MagicSet("BRO", name),
                1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.Common,
                MagicType.Creature,
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
                MagicLayout.Normal,
                2,
                "{1}{W}",
                MagicSet("M15", "Magic 2015"),
                1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.Common,
                MagicType.Creature,
                listOf("Pégase"),
                "1",
                "2",
                "Phill Simmer"
            )
        )
        return cardsArray
    }

    /**
     * Apply the filter to the cards
     */
    private fun applyFilter(filterState: FilterState): ArrayList<MagicCard> {
        val cardsArray = generateCards()
        return when (filterState.filterType) {
            FilterType.NONE -> cardsArray
            FilterType.NAME -> cardsArray.filter { it.name.contains(filterState.filterValue, true) }
            FilterType.SET -> cardsArray.filter {
                it.set.name.contains(
                    filterState.filterValue,
                    true
                )
            }
        } as ArrayList<MagicCard>
    }
}
