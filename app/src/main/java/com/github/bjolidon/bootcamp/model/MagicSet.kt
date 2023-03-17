// ====================
// MagicSet.kt
// Tarjetakuna, 2023
// ====================

package com.github.bjolidon.bootcamp.model

/**
 * Represents a Magic set.
 */
data class MagicSet(
    /**
     * The set code.
     */
    val code: String,

    /**
     * The set name.
     */
    val name: String
) {
    init {
        require(code.isNotBlank()) { "Code cannot be blank" }
        require(name.isNotBlank()) { "Name cannot be blank" }
    }

    override fun toString(): String {
        return "set: $name, code: $code"
    }
}
