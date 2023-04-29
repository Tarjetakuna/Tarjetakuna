package com.github.sdp.tarjetakuna.model

/**
 * Represents the rarity of a Magic card.
 */
enum class MagicRarity(private val rarityName: String) {
    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    MYTHIC_RARE("Mythic Rare"),
    SPECIAL("Special"),
    BASIC_LAND("Basic Land");

    override fun toString(): String {
        return rarityName
    }
}
