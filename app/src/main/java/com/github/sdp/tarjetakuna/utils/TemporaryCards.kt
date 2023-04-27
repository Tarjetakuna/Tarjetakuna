package com.github.sdp.tarjetakuna.utils

import com.github.sdp.tarjetakuna.model.*

/**
 * Represents temporary cards that are generated with a loop
 */
object TemporaryCards {
    fun generateCards(): ArrayList<MagicCard> {
        val cardsArray = ArrayList<MagicCard>()
        for (i in 0..39) {
            val name = if (i + 1 < 10) {
                "Magic 190${i + 1}"
            } else {
                "Magic 19${i + 1}"
            }
            val card = MagicCard(
                "Ambush Paratrooper ${i + 1}",
                "Flash, Flying\n5: Creatures you control get +1/+1 until end of turn.",
                MagicLayout.NORMAL,
                2,
                "{1}{W}",
                MagicSet("BRO", name),
                i + 1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.COMMON,
                MagicCardType.CREATURE,
                listOf("Human", "Soldier"),
                "1",
                "2",
                "Vladimir Krisetskiy"
            )
            cardsArray.add(card)
        }
        return cardsArray
    }
}
