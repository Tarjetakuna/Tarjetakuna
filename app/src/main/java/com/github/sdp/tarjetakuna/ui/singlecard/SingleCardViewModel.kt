package com.github.sdp.tarjetakuna.ui.singlecard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.FBCardPossesion
import com.github.sdp.tarjetakuna.database.FBMagicCard
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
        val data = user.getCardFromCollection(FBMagicCard(card, FBCardPossesion.NONE))
        data.thenAccept {
            val fbCard = it.getValue(FBMagicCard::class.java)
            _buttonAddText.value = fbCard?.possession != FBCardPossesion.OWNED
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
        val data = user.getCardFromCollection(FBMagicCard(card, FBCardPossesion.NONE))
        data.thenAccept {
            removeCardFromFirebase()
        }.exceptionally {
            addCardToFirebase()
            null
        }
    }

    /**
     * Add the card to the collection of the user
     */
    private fun addCardToFirebase() {
        val fbCard = FBMagicCard(card, FBCardPossesion.OWNED)
        user.addCardToCollection(fbCard)
        val data = user.getCardFromCollection(fbCard)
        data.thenAccept {
            _buttonAddText.value = false
        }
    }

    /**
     * Remove the card from the collection of the user
     */
    private fun removeCardFromFirebase() {
        val fbCard = FBMagicCard(card, FBCardPossesion.NONE)
        user.removeCardFromCollection(fbCard)
        val data = user.getCardFromCollection(fbCard)
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
