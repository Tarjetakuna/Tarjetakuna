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
    val code: String = "Unknown code",

    /**
     * The set name.
     */
    val name: String = "Unknown name",

    /**
     * The set type.
     */
    val type: String = "Unknown type",

    /**
     * The set block.
     */
    val block: String = "Unknown block",

    /**
     * The set release date.
     */
    @field:JsonAdapter(JsonAdapters.LocalDateAdapter::class)
    val releaseDate: LocalDate = LocalDate.of(1970, 1, 1)
) {
    init {
        require(code.isNotBlank()) { "Code cannot be blank" }
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(type.isNotBlank()) { "Type cannot be blank" }
        require(block.isNotBlank()) { "Block cannot be blank" }
    }
}
