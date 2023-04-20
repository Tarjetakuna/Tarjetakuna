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
    fun filter(cards: ArrayList<MagicCard>): ArrayList<MagicCard> {
        var filteredCards = cards

        if (setFilter != null) {
            filteredCards =
                filteredCards.filter { it.set.code.contains(setFilter) } as ArrayList<MagicCard>
        }
        if (manaFilter != null) {
            filteredCards =
                filteredCards.filter { it.convertedManaCost == manaFilter } as ArrayList<MagicCard>
        }
        return filteredCards
    }
}
