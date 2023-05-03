package com.github.sdp.tarjetakuna.model

/**
 * Represents a Magic card.
 */
data class MagicCard(
    /**
     * The card name.
     */
    val name: String,

    /**
     * The card text.
     * Example : "Flying"
     */
    val text: String,

    /**
     * The card layout.
     */
    val layout: MagicLayout,

    /**
     * The total card mana cost.
     * Example : 7
     */
    val convertedManaCost: Int,

    /**
     * The card mana cost.
     * Example : {5}{W}{W}
     */
    val manaCost: String,

    /**
     * The card set.
     */
    val set: MagicSet,

    /**
     * The card number of the set.
     * Example : 56
     */
    val number: Int,

    /**
     * the image url for the card.
     */
    val imageUrl: String,

    /**
     * The card rarity.
     */
    val rarity: MagicRarity,

    /**
     * The card type.
     * Example : CREATURE
     */
    val type: MagicCardType,

    /**
     * The card subtypes.
     * Example : Angel, Soldier
     */
    val subtypes: List<String>,

    /**
     * The card power.
     * Only for creatures.
     */
    val power: String,

    /**
     * The card toughness.
     * Only for creatures.
     */
    val toughness: String,

    /**
     * The card artist.
     */
    val artist: String,
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
