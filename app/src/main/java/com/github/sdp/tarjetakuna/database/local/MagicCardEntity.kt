package com.github.sdp.tarjetakuna.database.local

import androidx.room.Entity
import com.github.sdp.tarjetakuna.database.FBCardPossession
import com.github.sdp.tarjetakuna.model.MagicCard
import com.google.gson.Gson

/**
 * Represents a Magic card.
 */
@Entity(tableName = "magic_card", primaryKeys = ["code", "number"])
data class MagicCardEntity(
    /**
     * The code of the set of the card.
     */
    val code: String = "unknown",
    /**
     * The number of the card.
     */
    val number: Int = 0,
    /**
     * The magic card
     */
    val magicCard: String = "",
    /**
     * The card possession
     */
    val possession: FBCardPossession = FBCardPossession.NONE,
    /**
     * The last update time.
     */
    val lastUpdate: Long = System.currentTimeMillis()
) {
    /**
     * Creates a MagicCardEntity from a MagicCard and a possession.
     */
    constructor(card: MagicCard, possession: FBCardPossession) : this(
        code = card.set.code,
        number = card.number,
        magicCard = Gson().toJson(card),
        possession = possession,
        lastUpdate = System.currentTimeMillis()
    )

    /**
     * Converts the entity to a MagicCard.
     */
    fun toMagicCard(): MagicCard {
        return Gson().fromJson(magicCard, MagicCard::class.java)
    }
}
