package com.github.sdp.tarjetakuna.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.model.*
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    private val _titleText = MutableLiveData<String>().apply {
        value = "Welcome to Tarjetakuna!\n"
    }
    private val _descriptionText = MutableLiveData<String>().apply {
        value =
            "Start browsing right away, or sign in to view and manage your Magic: The Gathering collection\n"
    }
    val titleText: LiveData<String> = _titleText
    val descriptionText: LiveData<String> = _descriptionText

    // TODO remove this when we can research the cards and open them in the single card fragment
    // TODO the things to remove are: localDatabase, cards, generateCards, getRandomCard, the button
    var localDatabase: AppDatabase? = null

    private val cards = generateCards()

    fun addRandomCard() {
        val toInsert = listOf(
            MagicCard(
                "Venomous Hierophant",
                "Deathtouch\nWhen Venomous Hierophant enters the battlefield, mill three cards.",
                MagicLayout.NORMAL,
                4,
                "{3}{B}",
                MagicSet(
                    "THB",
                    "Theros Beyond Death",
                    "expansion",
                    "Theros Beyond Death",
                    LocalDate.parse("2020-01-24")
                ),
                122,
                "https://cards.scryfall.io/large/front/9/d/9dc2b661-2f42-419d-837f-bbf097c1153c.jpg?1581480034",
                MagicRarity.COMMON,
                MagicCardType.CREATURE,
                listOf("Gorgon", "Cleric"),
                "3",
                "3",
                "Johannes Voss"
            ),
            MagicCard(
                "Aeronaut Tinkerer",
                "Aeronaut Tinkerer has flying as long as you control an artifact. (It can’t be blocked except by creatures with flying or reach.)",
                MagicLayout.NORMAL,
                3,
                "{2}{U}",
                MagicSet("M15", "Magic 2015", "core", "Core Set", LocalDate.parse("2014-07-18")),
                43,
                "https://cards.scryfall.io/large/front/e/1/e145e85d-1eaa-4ec6-9208-ca6491577302.jpg?1562795701",
                MagicRarity.COMMON,
                MagicCardType.CREATURE,
                listOf("Human", "Artificer"),
                "2",
                "3",
                "William Murai"
            ),
            MagicCard(
                "Solemn Offering",
                "Destroy target artifact or enchantment. You gain 4 life.",
                MagicLayout.NORMAL,
                3,
                "{2}{W}",
                MagicSet("M15", "Magic 2015", "core", "Core Set", LocalDate.parse("2014-07-18")),
                33,
                "https://cards.scryfall.io/large/front/2/4/24d750b4-b58f-4465-8648-c86d678e0936.jpg?1595438247",
                MagicRarity.COMMON,
                MagicCardType.SORCERY,
                listOf(),
                "0",
                "0",
                "Sam Wood"
            )
        )
        for (card in toInsert) {
            viewModelScope.launch {
                val lCard =
                    localDatabase?.magicCardDao()
                        ?.getCard(card.set.code, card.number.toString())
                if (lCard != null) {
                    localDatabase?.magicCardDao()?.insertCard(
                        DBMagicCard(card, CardPossession.OWNED, lCard.quantity + 1)
                    )
                } else {
                    localDatabase?.magicCardDao()
                        ?.insertCard(DBMagicCard(card, CardPossession.OWNED, 1))
                }

            }.invokeOnCompletion {
                Log.i("Database", "set: ${card.set}, number: ${card.number} added to database")
            }
        }
    }
}
