package com.github.sdp.tarjetakuna.mockdata

import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicCardType
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicRarity

/**
 * This is a common class that can be used to get [MagicCard] objects
 */
object CommonMagicCard {

    /**
     * This is a valid [MagicCard] Aeronaut Tinkerer
     */
    val aeronautTinkererCard: MagicCard = MagicCard(
        "Aeronaut Tinkerer",
        "Aeronaut Tinkerer has flying as long as you control an artifact. (It can’t be blocked except by creatures with flying or reach.)",
        MagicLayout.NORMAL,
        3,
        "{2}{U}",
        CommonMagicSet.magic2015Set,
        43,
        "https://cards.scryfall.io/large/front/e/1/e145e85d-1eaa-4ec6-9208-ca6491577302.jpg?1562795701",
        MagicRarity.COMMON,
        MagicCardType.CREATURE,
        listOf("Human", "Artificer"),
        "2",
        "3",
        "William Murai"
    )

    /**
     * This is a valid [MagicCard] Venomous Hierophant
     */
    private val venomousHierophantCard: MagicCard = MagicCard(
        "Venomous Hierophant",
        "Deathtouch\nWhen Venomous Hierophant enters the battlefield, mill three cards.",
        MagicLayout.NORMAL,
        4,
        "{3}{B}",
        CommonMagicSet.promoMagic2015Set,
        122,
        "https://cards.scryfall.io/large/front/9/d/9dc2b661-2f42-419d-837f-bbf097c1153c.jpg?1581480034",
        MagicRarity.COMMON,
        MagicCardType.CREATURE,
        listOf("Gorgon", "Cleric"),
        "3",
        "3",
        "Johannes Voss"
    )

    /**
     * This is a valid [MagicCard] Solemn Offering
     */
    private val solemnOfferingCard: MagicCard = MagicCard(
        "Solemn Offering",
        "Destroy target artifact or enchantment. You gain 4 life.",
        MagicLayout.NORMAL,
        3,
        "{2}{W}",
        CommonMagicSet.magic2015Set,
        33,
        "https://cards.scryfall.io/large/front/2/4/24d750b4-b58f-4465-8648-c86d678e0936.jpg?1595438247",
        MagicRarity.COMMON,
        MagicCardType.SORCERY,
        listOf(),
        "0",
        "0",
        "Sam Wood"
    )

    /**
     * This is a valid list of [MagicCard] objects
     */
    val validListOfCards: List<MagicCard> = listOf(
        aeronautTinkererCard,
        venomousHierophantCard,
        solemnOfferingCard
    )

}
