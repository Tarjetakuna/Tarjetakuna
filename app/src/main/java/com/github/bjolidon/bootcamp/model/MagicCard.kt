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
     * The card colors.
     * Example : [MagicColor.White, MagicColor.White]
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
    val imageUrl: String
    ) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(text.isNotBlank()) { "Text cannot be blank" }
        require(convertedManaCost >= 0) { "CMC cannot be negative" }
        require(manaCost.isNotBlank()) { "Mana cost cannot be blank" } //TODO : parsing
        require(number > 0) { "Number cannot be negative or zero" }
        require(imageUrl.isNotBlank()) { "Image url cannot be blank" }
    }
}