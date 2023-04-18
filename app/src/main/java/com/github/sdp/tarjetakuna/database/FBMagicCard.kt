package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.MagicCard

/**
 * Class to represent a MagicCard in the Firebase database
< */
data class FBMagicCard(
    val card: MagicCard,
    val possession: FBCardPossession = FBCardPossession.NONE,
    val lastUpdate: Long = System.currentTimeMillis()
) {
}
