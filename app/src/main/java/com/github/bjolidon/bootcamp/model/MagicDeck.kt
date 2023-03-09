// ====================
// MagicDeck.kt
// Tarjetakuna, 2023
// ====================

package com.github.bjolidon.bootcamp.model

/**
 * Represents a Magic deck.
 */
data class MagicDeck(
    /**
     * The deck name.
     */
    val name: String,

    /**
     * The deck description.
     */
    val description: String,

    /**
     * The deck cards.
     */
    val cards: List<MagicCard>
    ) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
    }
}
