package com.github.sdp.tarjetakuna.ui.browser

import com.github.sdp.tarjetakuna.model.MagicCard

/**
 * Represents the state of the filter.
 */
data class FilterState(
    val setFilter: String? = null,
    val manaFilter: Int? = null,
) {
    /**
     * Apply the filters to the cards.
     * @param cards The cards to filter.
     * @return The filtered cards.
     */
    fun filter(cards: ArrayList<Pair<MagicCard, Int>>): ArrayList<Pair<MagicCard, Int>> {
        var filteredCards = cards

        if (setFilter != null) {
            filteredCards =
                filteredCards.filter { it.first.set.code.contains(setFilter) } as ArrayList<Pair<MagicCard, Int>>
        }
        if (manaFilter != null) {
            filteredCards =
                filteredCards.filter { it.first.convertedManaCost == manaFilter.toDouble() } as ArrayList<Pair<MagicCard, Int>>
        }
        return filteredCards
    }
}
