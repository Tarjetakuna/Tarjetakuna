package com.github.sdp.tarjetakuna.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet

/**
 * View model of the user's collection of cards.
 * It does the computations and stores the data for the UI
 */
class UserCardsViewModel : ViewModel() {

    private var retrievedCardJson = ""

    private val _getMessage = MutableLiveData<Pair<Int, String>>()
    val getMessage: LiveData<Pair<Int, String>> = _getMessage

    private val _setMessage = MutableLiveData<Pair<Int, String>>()
    val setMessage: LiveData<Pair<Int, String>> = _setMessage

    private val _removeMessage = MutableLiveData<Pair<Int, String>>()
    val removeMessage: LiveData<Pair<Int, String>> = _removeMessage


    //TODO Remove these hardcoded values and replace them with the web API callss
    val card1 = MagicCard(
        "Angel of Mercy", "Flying",
        MagicLayout.NORMAL, 7, "{5}{W}{W}",
        MagicSet("MT15", "Magic 2015"), 56,
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
    )

    val card2 = MagicCard(
        "Meandering Towershell", "Islandwalk",
        MagicLayout.DOUBLE_FACED, 5, "{3}{G}{G}",
        MagicSet("MT15", "Magic 2015"), 141,
        "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602"
    )
    val usc = UserCardsRTDB()

    /**
     * Add the card to the user's collection
     */
    fun onSetButtonClick(card: MagicCard) {
        usc.addCardToCollection(DBMagicCard(card, CardPossession.OWNED))
        _setMessage.value = Pair(R.string.text_add_success, card.name)

    }

    /**
     * Remove the card from the user's collection
     */
    fun onRemoveButtonClick(card: MagicCard) {
        usc.removeCardFromCollection(DBMagicCard(card, CardPossession.NONE))
        _removeMessage.value = Pair(R.string.text_remove_success, card.name)
    }

    /**
     * Get the card from the user's collection if it exists
     */
    fun onGetButtonClick(card: MagicCard) {
        val data = usc.getCardFromCollection(DBMagicCard(card, CardPossession.NONE))
        data
            .thenAccept {
                retrievedCardJson = it.value.toString()
                putGetMessage(R.string.text_get_success, it.key.toString())
            }.exceptionally { e ->
                putGetMessage(R.string.text_get_fail, e.message.toString())
                null
            }
    }

    /**
     * puts the message to be displayed in the UI, (doesn't work otherwise)
     */
    private fun putGetMessage(rid: Int, msg: String) {
        _getMessage.value = Pair(rid, msg)
    }
}
