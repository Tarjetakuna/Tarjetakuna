package com.github.sdp.tarjetakuna.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

/**
 * View model of the user's collection of cards.
 * It does the computations and stores the data for the UI
 */
class UserCardsViewModel : ViewModel() {

    private var retrievedCardJson = ""

    private val _getMessage = MutableLiveData<String>()
    val getMessage: LiveData<String> = _getMessage

    private val _setMessage = MutableLiveData<String>()
    val setMessage: LiveData<String> = _setMessage

    private val _removeMessage = MutableLiveData<String>()
    val removeMessage: LiveData<String> = _removeMessage


    //TODO Remove these hardcoded values and replace them with the web API callss
    val card1 = MagicCard(
        "Angel of Mercy", "Flying",
        MagicLayout.Normal, 7, "{5}{W}{W}",
        MagicSet("MT15", "Magic 2015"), 56,
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
    )

    private val card1UID = card1.set.code + card1.number

    val card2 = MagicCard(
        "Meandering Towershell", "Islandwalk",
        MagicLayout.DoubleFaced, 5, "{3}{G}{G}",
        MagicSet("MT15", "Magic 2015"), 141,
        "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602"
    )
    val usc = UserCardsRTDB()

    /**
     * Add the card to the user's collection
     */
    fun onSetButtonClick(card: MagicCard) {
        usc.addCardToCollection(card)
        _setMessage.value = "${card.name} was successfully added to your collection"
    }

    /**
     * Remove the card from the user's collection
     */
    fun onRemoveButtonClick(card: MagicCard) {
        usc.removeCardFromCollection(card)
        _removeMessage.value = "${card.name} was successfully removed from your collection"
    }

    /**
     * Get the card from the user's collection if it exists
     */
    fun onGetButtonClick(card: MagicCard) {
        val data = usc.getCardFromCollection(card1)
        data
            .thenAccept {
                retrievedCardJson = it.value.toString()
                putGetMessage("Card ${it.key} was successfully retrieved from your collection:\n ${it.value}")
            }.exceptionally { e ->
                putGetMessage("Failed to retrieve card : ${e.message}")
                null
            }
    }

    /**
     * puts the message to be displayed in the UI, (doesn't work otherwise)
     */
    private fun putGetMessage(msg: String) {
        _getMessage.value = msg
    }
}
