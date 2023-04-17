package com.github.sdp.tarjetakuna.database.local

import androidx.room.Entity
import com.github.sdp.tarjetakuna.database.FBCardPossession
import com.github.sdp.tarjetakuna.model.*
import com.google.gson.Gson

/**
 * Represents a Magic card.
 */
@Entity(tableName = "magic_card", primaryKeys = ["set", "number"])
data class MagicCardEntity(
    /**
     * The card name.
     */
    val name: String = "Unknown name",

    /**
     * The card text.
     * Example : "Flying"
     */
    val text: String = "Unknown text",

    /**
     * The card layout.
     */
    val layout: MagicLayout = MagicLayout.Normal,

    /**
     * The total card mana cost.
     * Example : 7
     */
    val convertedManaCost: Int = 0,

    /**
     * The card mana cost.
     * Example : {5}{W}{W}
     */
    val manaCost: String = "{0}",

    /**
     * The card set.
     * Example : MagicSet("M21", "Core Set 2021")
     */
    val set: String = Gson().toJson(MagicSet("Unknown code", "Unknown name")),

    /**
     * The card number of the set.
     * Example : 56
     */
    val number: Int = 1,

    /**
     * the image url for the card.
     */
    val imageUrl: String = "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",

    /**
     * The card rarity.
     */
    val rarity: MagicRarity = MagicRarity.Common,

    /**
     * The card type.
     * Example : Creature
     */
    val type: MagicType = MagicType.Creature,

    /**
     * The card subtypes.
     * Example : Angel, Soldier
     */
    val subtypes: String = Gson().toJson(listOf<String>()),

    /**
     * The card power.
     * Only for creatures.
     */
    val power: String = "0",

    /**
     * The card toughness.
     * Only for creatures.
     */
    val toughness: String = "0",

    /**
     * The card artist.
     */
    val artist: String = "Unknown artist",

    /**
     * The card possession
     */
    val possession: FBCardPossession = FBCardPossession.NONE
) {
    /**
     * Creates a MagicCardEntity from a MagicCard and a possession.
     */
    constructor(card: MagicCard, possession: FBCardPossession) : this(
        name = card.name,
        text = card.text,
        layout = card.layout,
        convertedManaCost = card.convertedManaCost,
        manaCost = card.manaCost,
        set = Gson().toJson(card.set),
        number = card.number,
        imageUrl = card.imageUrl,
        rarity = card.rarity,
        type = card.type,
        subtypes = Gson().toJson(card.subtypes),
        power = card.power,
        toughness = card.toughness,
        artist = card.artist,
        possession = possession
    )

    /**
     * Converts the entity to a MagicCard.
     */
    fun toMagicCard(): MagicCard {
        return MagicCard(
            name = name,
            text = text,
            layout = layout,
            convertedManaCost = convertedManaCost,
            manaCost = manaCost,
            set = Gson().fromJson(set, MagicSet::class.java),
            number = number,
            imageUrl = imageUrl,
            rarity = rarity,
            type = type,
            subtypes = Gson().fromJson(subtypes, List::class.java) as List<String>,
            power = power,
            toughness = toughness,
            artist = artist,
        )
    }
}
