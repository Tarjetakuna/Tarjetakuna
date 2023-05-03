package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.utils.JsonAdapters
import com.google.gson.annotations.JsonAdapter
import java.time.LocalDate

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
    val name: String,

    /**
     * The set type.
     */
    val type: String,

    /**
     * The set release date.
     */
    @field:JsonAdapter(JsonAdapters.LocalDateAdapter::class)
    val releaseDate: LocalDate,

    /**
     * The set icon.
     */
    val iconUri: String,

    /**
     * The set card count.
     */
    val cardCount: Int,

    /**
     * The set parent set code.
     * Empty if the set has no parent set.
     */
    val parentSetCode: String,

    /**
     * The set cards uri from the API.
     */
    val cardsInSetUri: String,
) {
    init {
        require(code.isNotBlank()) { "Code cannot be blank" }
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(type.isNotBlank()) { "Type cannot be blank" }
        require(iconUri.isNotBlank()) { "Icon cannot be blank" }
        require(cardCount >= 0) { "Card count cannot be negative" }
    }
}
