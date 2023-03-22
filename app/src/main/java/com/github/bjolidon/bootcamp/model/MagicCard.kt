// ====================
// MagicCard.kt
// Tarjetakuna, 2023
// ====================

package com.github.bjolidon.bootcamp.model

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
     * The card colors.
     * Example : [MagicColor.White, MagicColor.White]
     */
    val set: MagicSet = MagicSet("Unknown code", "Unknown name"),

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
    val subtypes: List<String> = listOf(),
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(text.isNotBlank()) { "Text cannot be blank" }
        require(convertedManaCost >= 0) { "CMC cannot be negative" }
        require(manaCost.isNotBlank()) { "Mana cost cannot be blank" } //TODO : parsing
        require(number > 0) { "Number cannot be negative or zero" }
        require(imageUrl.isNotBlank()) { "Image url cannot be blank" }

        for (subtype in subtypes) {
            require(subtype.isNotBlank()) { "Subtype cannot be blank" }
        }
    }
}
