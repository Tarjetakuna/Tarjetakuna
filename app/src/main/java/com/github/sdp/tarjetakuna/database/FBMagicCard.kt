package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.database.local.MagicCardEntity
import com.github.sdp.tarjetakuna.model.MagicCard

/**
 * Class to represent a MagicCard in the Firebase database
< */
data class FBMagicCard(
    val card: MagicCard,
    val possession: FBCardPossession = FBCardPossession.NONE,
    val lastUpdate: Long = System.currentTimeMillis(),
    val code: String = "unknown",
    val number: Int = 0,
) {

    /**
     * Creates a MagicCardEntity from a FBMagicCard.
     */
    fun toMagicCardEntity(): MagicCardEntity {
        return MagicCardEntity(card, possession)
    }
}
