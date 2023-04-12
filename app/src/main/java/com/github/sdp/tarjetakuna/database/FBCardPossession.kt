package com.github.sdp.tarjetakuna.database

/**
 * Enum to represent the user's possession of a MagicCard
 * NONE: used for the getter as a default value as it only cares about the card for the key
 */
enum class FBCardPossession {
    OWNED,
    WANTED,
    NONE
}
