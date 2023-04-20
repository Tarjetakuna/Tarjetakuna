package com.github.sdp.tarjetakuna.ui.browser

import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicCardType
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicRarity
import com.github.sdp.tarjetakuna.model.MagicSet
import java.time.LocalDate

class BrowserViewModel : ViewModel() {

    val initialCards: ArrayList<MagicCard> = generateCards()

    /**
     * TODO change it when we have the web api to get the cards
     * Generate cards in order to simulate the display of the cards
     */
    private fun generateCards(): ArrayList<MagicCard> {
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
                MagicLayout.Normal,
                2,
                "{1}{W}",
                MagicSet("BRO", name, "Core", "Core Block", LocalDate.of(2019, 3, 10)),
                1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.Common,
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
                MagicLayout.Normal,
                2,
                "{1}{W}",
                MagicSet("M15", "Magic 2015", "Core", "Core", LocalDate.now()),
                1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.Common,
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
