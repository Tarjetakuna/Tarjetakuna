package com.github.sdp.tarjetakuna.ui.singlecard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.FBCardPossesion
import com.github.sdp.tarjetakuna.database.UserCardsRTDB
import com.github.sdp.tarjetakuna.model.MagicCard

class SingleCardViewModel : ViewModel() {

    lateinit var card: MagicCard

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private val _buttonAddText = MutableLiveData<Boolean>()
    val buttonAddText: LiveData<Boolean> = _buttonAddText

    private var user = UserCardsRTDB()

    fun checkUserConnected() {
        //TODO : Check if the user is connected
        _isConnected.value = user.isConnected()
    }

    /**
     * Check if the card is in the collection of the user
     */
    fun checkCardInCollection() {
        if (!user.isConnected()) {
            return
        }
        val data = user.getCardFromFirebase(card, FBCardPossesion.OWNED.name)
        data.thenAccept {
            _buttonAddText.value = false
        }.exceptionally {
            _buttonAddText.value = true
            null
        }
    }

    /**
     * Check if the card is in the wanted cards of the user
     */
    fun checkCardInWanted() {
        //TODO : Check if the card is in the wanted cards of the user
    }

    /**
     * Manage the collection of the user,
     * add the card if it's not in the collection,
     * remove it if it's in the collection
     */
    fun manageOwnedCollection() {
        val data = user.getCardFromFirebase(card, FBCardPossesion.OWNED.name)
        data.thenAccept {
            removeCardFromFirebase(FBCardPossesion.OWNED.name)
        }.exceptionally {
            addCardToFirebase(FBCardPossesion.OWNED.name)
            null
        }
    }

    /**
     * Add the card to the collection of the user
     */
    private fun addCardToFirebase(nodeName: String) {
        user.addCardToFirebase(card, nodeName)
        val data = user.getCardFromFirebase(card, nodeName)
        data.thenAccept {
            _buttonAddText.value = false
        }
    }

    /**
     * Remove the card from the collection of the user
     */
    private fun removeCardFromFirebase(nodeName: String) {
        user.removeCardFromFirebase(card, nodeName)
        val data = user.getCardFromFirebase(card, nodeName)
        data.exceptionally {
            _buttonAddText.value = true
            null
        }
    }

    /**
     * Add the card to the wanted cards of the user
     */
    fun addCardToWanted() {
        //TODO : Add the card to the wanted cards
    }
}
