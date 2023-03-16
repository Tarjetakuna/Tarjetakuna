package com.github.bjolidon.bootcamp.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.github.bjolidon.bootcamp.model.MagicSet

class BrowserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Coming soon here: a search bar to filter your cards"
    }
    val text: LiveData<String> = _text


    val cards: ArrayList<MagicCard> = generateCards()

    /**
     * Generate cards in order to simulate the display of the cards
     */
    private fun generateCards(): ArrayList<MagicCard> {
        val cardsArray = ArrayList<MagicCard>()
        for (i in 0..39) {
            val name = if (i+1 < 10) { "Magic 190${i+1}" } else { "Magic 19${i+1}" }
            val card = MagicCard(
                "Angel of Mercy ${i+1}", "Flying",
                MagicLayout.Normal, 7, "{5}{W}{W}",
                MagicSet("MT15", name), 56,
                "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
            )
            cardsArray.add(card)
        }
        return cardsArray
    }
}