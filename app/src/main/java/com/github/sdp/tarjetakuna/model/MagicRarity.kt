package com.github.sdp.tarjetakuna.model

/**
 * Represents the rarity of a Magic card.
 */
enum class MagicRarity(private val rarityName: String) {
    COMMON("common"),
    UNCOMMON("uncommon"),
    RARE("rare"),
    MYTHIC_RARE("mythic"),
    SPECIAL("special"),
    BONUS("bonus");

    override fun toString(): String {
        return rarityName
    }

    companion object {
        fun fromApiString(apiString: String): MagicRarity {
            return MagicRarity.values().firstOrNull { it.rarityName == apiString }
                ?: COMMON
        }
    }
}
