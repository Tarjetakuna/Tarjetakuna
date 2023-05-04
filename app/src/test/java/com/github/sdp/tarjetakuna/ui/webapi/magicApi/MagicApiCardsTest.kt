package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.mockdata.CommonMagicApiCard
import org.junit.Test

class MagicApiCardsTest {
    private val validTotalCards = 1
    private val validHasMore = false
    private val validNextPage = "fake valid next page"
    private val validCards = listOf(CommonMagicApiCard.mockCard)

    @Test
    fun validMagicApiCards() {
        val cards = MagicApiCards(
            total_cards = validTotalCards,
            has_more = validHasMore,
            next_page = validNextPage,
            data = validCards
        )

        assert(cards.total_cards == validTotalCards)
        assert(cards.has_more == validHasMore)
        assert(cards.next_page == validNextPage)
        assert(cards.data == validCards)
    }
}
