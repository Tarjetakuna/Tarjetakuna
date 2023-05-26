package com.github.sdp.tarjetakuna.ui.singlecard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.database.*
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the SingleCardFragment
 */
class SingleCardViewModel : ViewModel() {

    var localDatabase: AppDatabase? = null
    var card: MagicCard? = null

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private val _buttonWantedText = MutableLiveData<Boolean>()
    val buttonWantedText: LiveData<Boolean> = _buttonWantedText

    private val _currentQuantity = MutableLiveData<String>()
    val currentQuantity: LiveData<String> = _currentQuantity

    private var userDB = UserRTDB(
        FirebaseDB()
    )

    /**
     * Check if the user is connected to the app with a google account
     */
    fun checkUserConnected() {
        _isConnected.value = SignIn.getSignIn().isUserLoggedIn()
    }


    /**
     * Check if the card is in the collection of the user, either wanted or owned
     */
    fun checkCardInCollection() {
        if (!SignIn.getSignIn().isUserLoggedIn()) {
            return
        }
        //DatabaseSync.sync()
        viewModelScope.launch {
            val lCard =
                card?.set?.let {
                    localDatabase?.magicCardDao()?.getCard(it.code, card!!.number.toString())
                }
            if (lCard != null) {
                _currentQuantity.value = lCard.quantity.toString()
                _buttonWantedText.value = lCard.possession != CardPossession.WANTED
                Log.i("SingleCardViewModel", "checkCardInCollection: card found in local database")
            }
        }
    }

    /**
     * Manage the collection of cards wanted by the user,
     * add the card if it's not in the collection,
     * remove it only if it's not owned
     */
    fun manageWantedCollection() {
        viewModelScope.launch {
            val lCard =
                card?.set?.let {
                    localDatabase?.magicCardDao()?.getCard(it.code, card!!.number.toString())
                }
            if (lCard != null) {
                // card not wanted -> make it unwanted from the collection
                if (lCard.possession == CardPossession.WANTED) {
                    card?.let { manageCardsInDatabase(it, CardPossession.NONE) }
                    updateValues(CardPossession.NONE, 0)
                } else {
                    // card wanted -> make it wanted in the collection
                    card?.let { manageCardsInDatabase(it, CardPossession.WANTED) }
                    updateValues(CardPossession.WANTED, 0)
                }
            } else {
                // card not in the database -> add it as wanted in the local database
                card?.let { manageCardsInDatabase(it, CardPossession.WANTED) }
                updateValues(CardPossession.WANTED, 0)
            }
            // sync the local database with the firebase if possible
            DatabaseSync.sync()
        }
    }

    /**
     * Add a copy of the card to the owned collection
     */
    fun addCardToOwnedCollection() {
        viewModelScope.launch {
            val lCard =
                card?.set?.let {
                    localDatabase?.magicCardDao()?.getCard(it.code, card!!.number.toString())
                }
            if (lCard != null) {
                // card not owned -> make it owned from the collection
                if (lCard.possession != CardPossession.OWNED) {
                    card?.let { manageCardsInDatabase(it, CardPossession.OWNED, 1) }
                    updateValues(CardPossession.OWNED, 1)
                } else {
                    card?.let {
                        manageCardsInDatabase(
                            it,
                            CardPossession.OWNED,
                            lCard.quantity + 1
                        )
                    }
                    updateValues(CardPossession.OWNED, lCard.quantity + 1)
                }
            } else {
                // card not in the database -> add it as owned in the local database
                card?.let { manageCardsInDatabase(it, CardPossession.OWNED, 1) }
                updateValues(CardPossession.OWNED, 1)
            }
            // sync the local database with the firebase if possible
            DatabaseSync.sync()
        }
    }

    /**
     * Remove a copy of the card from the owned collection
     */
    fun removeCardFromOwnedCollection() {
        viewModelScope.launch {
            val lCard =
                card?.set?.let {
                    localDatabase?.magicCardDao()?.getCard(it.code, card!!.number.toString())
                }
            if (lCard != null) {
                if (lCard.possession == CardPossession.OWNED && lCard.quantity > 1) {
                    card?.let {
                        manageCardsInDatabase(
                            it,
                            CardPossession.OWNED,
                            lCard.quantity - 1
                        )
                    }
                    updateValues(CardPossession.OWNED, lCard.quantity - 1)
                } else {
                    card?.let { manageCardsInDatabase(it, CardPossession.NONE, 0) }
                    updateValues(CardPossession.NONE, 0)
                }
            } else {
                card?.let { manageCardsInDatabase(it, CardPossession.NONE, 0) }
                updateValues(CardPossession.NONE, 0)
            }
            // sync the local database with the firebase if possible
            DatabaseSync.sync()
        }
    }

    /**
     * Manage the cards in the local database
     * @param card the card to manage
     * @param p the possession of the card (owned or wanted)
     * @param quantity the quantity of the card
     */
    private suspend fun manageCardsInDatabase(
        card: MagicCard,
        p: CardPossession,
        quantity: Int = 0
    ) {
        localDatabase?.magicCardDao()?.insertCard(DBMagicCard(card, p, quantity))
    }

    /**
     * Updates the wanted button text and the quantity of the card
     */
    private fun updateValues(p: CardPossession, quantity: Int) {
        when (p) {
            CardPossession.WANTED -> {
                _buttonWantedText.value = false
                _currentQuantity.value = 0.toString()
            }
            CardPossession.OWNED -> {
                _buttonWantedText.value = true
                _currentQuantity.value = quantity.toString()
            }
            else -> {
                _buttonWantedText.value = true
                _currentQuantity.value = 0.toString()
            }
        }
    }
}
