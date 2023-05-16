package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import org.junit.Assert.assertEquals
import org.junit.Test

class DBMagicCardTest {
    private val validMagicCard = CommonMagicCard.aeronautTinkererCard

    @Test
    fun toMagicCardWorksOnValidCard() {
        val dbMagicCard = DBMagicCard(validMagicCard)
        val magicCard = dbMagicCard.toMagicCard()
        assertEquals(validMagicCard, magicCard)
    }

    @Test
    fun clearPossessionWorksOnValidCard() {
        val dbMagicCard = DBMagicCard(validMagicCard, CardPossession.OWNED)
        val expectedDBCard = DBMagicCard(validMagicCard, CardPossession.NONE)
        val dbMagicCardCleared = dbMagicCard.clearPossession()
        assertEquals(expectedDBCard.possession, dbMagicCardCleared.possession)
    }
}
