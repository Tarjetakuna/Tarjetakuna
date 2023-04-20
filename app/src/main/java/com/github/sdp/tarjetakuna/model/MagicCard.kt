package com.github.sdp.tarjetakuna.model

/**
 * Represents a Magic card.
 */
data class MagicCard(
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
    val set: MagicSet = MagicSet(),

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
     * Example : CREATURE
     */
    val type: MagicCardType = MagicCardType.CREATURE,

    /**
     * The card subtypes.
     * Example : Angel, Soldier
     */
    val subtypes: List<String> = listOf(),

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
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(text.isNotBlank()) { "Text cannot be blank" }
        require(convertedManaCost >= 0) { "CMC cannot be negative" }
        require(manaCost.isNotBlank()) { "Mana cost cannot be blank" } //TODO : parsing
        require(number > 0) { "Number cannot be negative or zero" }
        require(imageUrl.isNotBlank()) { "Image url cannot be blank" }
        require(power.isNotBlank()) { "Power cannot be blank" }
        require(toughness.isNotBlank()) { "Toughness cannot be blank" }
        require(artist.isNotBlank()) { "Artist cannot be blank" }

        for (subtype in subtypes) {
            require(subtype.isNotBlank()) { "Subtype cannot be blank" }
        }
    }
}
