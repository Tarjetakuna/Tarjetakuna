package com.github.sdp.tarjetakuna.ui.singlecard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.FBCardPossession
import com.github.sdp.tarjetakuna.database.FBMagicCard
import com.github.sdp.tarjetakuna.database.UserCardsRTDB
import com.github.sdp.tarjetakuna.model.MagicCard
import com.google.gson.Gson

/**
 * ViewModel for the SingleCardFragment
 */
class SingleCardViewModel : ViewModel() {

    lateinit var card: MagicCard

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private val _buttonAddText = MutableLiveData<Boolean>()
    val buttonAddText: LiveData<Boolean> = _buttonAddText

    private val _buttonWantedText = MutableLiveData<Boolean>()
    val buttonWantedText: LiveData<Boolean> = _buttonWantedText

    private var userDB = UserCardsRTDB()

    /**
     * Check if the user is connected to the app with a google account
     */
    fun checkUserConnected() {
        _isConnected.value = userDB.isConnected()
    }

    /**
     * Convert a json string to a FBMagicCard
     * @param json the json string to convert
     */
    private fun convertJsonToFBMagicCard(json: String): FBMagicCard {
        return Gson().fromJson(json, FBMagicCard::class.java)
    }

    /**
     * Check if the card is in the collection of the user, either wanted or owned
     */
    fun checkCardInCollection() {
        if (!userDB.isConnected()) {
            return
        }
        val data = userDB.getCardFromCollection(FBMagicCard(card))
        data.thenAccept {
            val fbCard = convertJsonToFBMagicCard(it.value as String)
            _buttonAddText.value = fbCard.possession != FBCardPossession.OWNED
            _buttonWantedText.value = fbCard.possession != FBCardPossession.WANTED
        }.exceptionally {
            _buttonAddText.value = true
            _buttonWantedText.value = true
            null
        }
    }

    /**
     * Manage the collection of cards wanted by the user,
     * add the card if it's not in the collection,
     * remove it only if it's not owned
     */
    fun manageWantedCollection() {
        val data = userDB.getCardFromCollection(FBMagicCard(card))
        data.thenAccept {
            val fbCard = convertJsonToFBMagicCard(it.value as String)
            if (fbCard.possession == FBCardPossession.WANTED) {
                removeCardFromFirebase()
            }
        }.exceptionally {
            addCardToFirebase(FBCardPossession.WANTED)
            null
        }
    }

    /**
     * Manage the collection of cards owned by the user,
     * add the card if it's not in the collection,
     * remove it if it's in the collection
     */
    fun manageOwnedCollection() {
        val data = userDB.getCardFromCollection(FBMagicCard(card))
        data.thenAccept {
            val fbCard = convertJsonToFBMagicCard(it.value as String)
            if (fbCard.possession == FBCardPossession.OWNED) {
                removeCardFromFirebase()
            } else {
                addCardToFirebase(FBCardPossession.OWNED)
            }
        }.exceptionally {
            addCardToFirebase(FBCardPossession.OWNED)
            null
        }
    }

    /**
     * Add the card to the collection of the user
     * @param p the possession of the card (owned or wanted)
     */
    private fun addCardToFirebase(p: FBCardPossession) {
        val fbCard = FBMagicCard(card, p)
        userDB.addCardToCollection(fbCard)
        val data = userDB.getCardFromCollection(fbCard)
        data.thenAccept {
            if (p == FBCardPossession.OWNED) {
                _buttonAddText.value = false
                _buttonWantedText.value = true
            } else {
                _buttonWantedText.value = false

            }
        }
    }

    /**
     * Remove the card from the collection of the user
     */
    private fun removeCardFromFirebase() {
        val fbCard = FBMagicCard(card)
        userDB.removeCardFromCollection(fbCard)
        val data = userDB.getCardFromCollection(fbCard)
        data.exceptionally {
            _buttonAddText.value = true
            _buttonWantedText.value = true
            null
        }
    }
}
