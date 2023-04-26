package com.github.sdp.tarjetakuna.utils

import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicCardType
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicRarity

/**
 * This is a common class that can be used in both _androidTest_ and _unitTest_ to get [MagicCard] objects
 */
class CommonMagicCard {

    /**
     * This is a valid [MagicCard] object
     */
    val validCard: MagicCard = MagicCard(
        "Aeronaut Tinkerer",
        "Aeronaut Tinkerer has flying as long as you control an artifact. (It can’t be blocked except by creatures with flying or reach.)",
        MagicLayout.NORMAL,
        3,
        "{2}{B}",
        CommonMagicSet().validSet,
        43,
        "https://cards.scryfall.io/large/front/e/1/e145e85d-1eaa-4ec6-9208-ca6491577302.jpg?1562795701",
        MagicRarity.COMMON,
        MagicCardType.CREATURE,
        listOf("Human", "Artificer"),
        "2",
        "3",
        "William Murai"
    )

}
