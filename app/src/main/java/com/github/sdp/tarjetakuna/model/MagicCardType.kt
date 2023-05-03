package com.github.sdp.tarjetakuna.model

/**
 * Represents a Magic type.
 */
enum class MagicCardType(private val typeName: String) {
    ARTIFACT("Artifact"),
    CREATURE("Creature"),
    ENCHANTMENT("Enchantment"),
    INSTANT("Instant"),
    LAND("Land"),
    PLANESWALKER("Planeswalker"),
    SORCERY("Sorcery"),
    TRIBAL("Tribal"),
    UNKNOWN("Unknown");

    override fun toString(): String {
        return typeName
    }
}
