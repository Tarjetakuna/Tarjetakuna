package com.github.sdp.tarjetakuna.utils

import com.github.sdp.tarjetakuna.model.*

/**
 * Represents temporary cards that are generated with a loop
 */
object TemporaryCards {
    fun generateCards(): ArrayList<MagicCard> {
        val cardsArray = ArrayList<MagicCard>()
        for (i in 0..39) {
            val number = if (i + 1 < 10) {
                "0${i + 1}"
            } else {
                "${i + 1}"
            }
            val name = "Magic 19$number"
            val card = MagicCard(
                "Ambush Paratrooper $number",
                "Flash, Flying\n5: Creatures you control get +1/+1 until end of turn.",
                MagicLayout.NORMAL,
                2,
                "{1}{W}",
                MagicSet("BRO", name),
                i + 1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.RARE,
                MagicCardType.CREATURE,
                listOf("Human", "Soldier"),
                "1",
                "2",
                "Vladimir Krisetskiy"
            )
            cardsArray.add(card)
        }

        //Example of an another card
        cardsArray.add(
            MagicCard(
                "Pégase solgrâce",
                "Vol\nLien de vie",
                MagicLayout.NORMAL,
                3,
                "{1}{W}{W}",
                MagicSet("M15", "Magic 2015"),
                1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.COMMON,
                MagicCardType.CREATURE,
                listOf("Pégase"),
                "1",
                "2",
                "Phill Simmer"
            )
        )
        return cardsArray
    }
}
