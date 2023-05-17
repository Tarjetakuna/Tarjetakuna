package com.github.sdp.tarjetakuna.database

import androidx.room.Entity
import com.github.sdp.tarjetakuna.model.MagicCard
import com.google.gson.Gson

/**
 * Class to represent a MagicCard in the Firebase database
 */
@Entity(tableName = "magic_card", primaryKeys = ["code", "number"])
data class DBMagicCard(
    val card: String,
    val possession: CardPossession,
    val lastUpdate: Long,
    val code: String,
    val number: Int,
) {
    constructor(card: MagicCard) : this(
        card = Gson().toJson(card),
        possession = CardPossession.NONE,
        lastUpdate = System.currentTimeMillis(),
        code = card.set.code,
        number = card.number,
    )

    constructor(card: MagicCard, possession: CardPossession) : this(
        card = Gson().toJson(card),
        possession = possession,
        lastUpdate = System.currentTimeMillis(),
        code = card.set.code,
        number = card.number,
    )

    /**
     * Converts a DBMagicCard to a MagicCard
     */
    fun toMagicCard(): MagicCard {
        return Gson().fromJson(card, MagicCard::class.java)
    }

    /**
     * Creates a DBMagicCard with no possession
     */
    fun clearPossession(): DBMagicCard {
        return DBMagicCard(this.toMagicCard(), CardPossession.NONE)
    }

    /**
     * Get the correct key for this DBMagicCard for the Firebase database
     */
    fun getFbKey(): String {
        return code + "_" + number.toString()
    }

    companion object {
        /**
         * Create a DBMagicCard from a MagicCard
         */
        fun fromMagicCard(
            card: MagicCard,
            possession: CardPossession = CardPossession.NONE
        ): DBMagicCard {
            return DBMagicCard(card, possession)
        }
    }
}
