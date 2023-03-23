// ====================
// MagicDeck.kt
// Tarjetakuna, 2023
// ====================

package com.github.sdp.tarjetakuna.model

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
     * @see MagicCard
     */
    // For the sake of simplicity now, we'll just use MagicCard class.
    // It will be better to use a different class for this (like Magic ID).
    val cards: List<MagicCard>
    ) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
    }
}
