package com.github.sdp.tarjetakuna.ui.browser

import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.*

class BrowserViewModel : ViewModel() {

    val cards: ArrayList<MagicCard> = generateCards()

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
                MagicSet("BRO", name),
                1,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card",
                MagicRarity.Common,
                MagicType.Creature,
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
