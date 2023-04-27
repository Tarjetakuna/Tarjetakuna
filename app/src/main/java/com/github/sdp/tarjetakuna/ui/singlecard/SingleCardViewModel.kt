package com.github.sdp.tarjetakuna.ui.singlecard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.UserCardsRTDB
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.model.MagicCard
import com.google.gson.Gson
import kotlinx.coroutines.launch

/**
 * ViewModel for the SingleCardFragment
 */
class SingleCardViewModel : ViewModel() {

    var localDatabase: AppDatabase? = null
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
    private fun convertJsonToFBMagicCard(json: String): DBMagicCard {
        return Gson().fromJson(json, DBMagicCard::class.java)
    }

    /**
     * Check if the card is in the collection of the user, either wanted or owned
     */
    fun checkCardInCollection() {
        println("checking card in collection")
        if (!userDB.isConnected()) {
            return
        }
//        var lCard: MagicCardEntity? = null
//        viewModelScope.launch {
//            lCard =
//                localDatabase?.magicCardDao()?.getCard(card.set.toString(), card.number.toString())
//        }.invokeOnCompletion {
//            if (lCard != null) {
//                _buttonAddText.value = lCard?.possession != FBCardPossession.OWNED
//                _buttonWantedText.value = lCard?.possession != FBCardPossession.WANTED
//                println("card in local database")
//            } else {
//                val data = userDB.getCardFromCollection(FBMagicCard(card))
//                data.thenAccept {
//                    val fbCard = convertJsonToFBMagicCard(it.value as String)
//                    _buttonAddText.value = fbCard.possession != FBCardPossession.OWNED
//                    _buttonWantedText.value = fbCard.possession != FBCardPossession.WANTED
//                }.exceptionally {
//                    _buttonAddText.value = true
//                    _buttonWantedText.value = true
//                    null
//                }
//            }
//        }
        val data = userDB.getCardFromCollection(DBMagicCard(card))
        data.thenAccept {
            val fbCard = convertJsonToFBMagicCard(it.value as String)
            _buttonAddText.value = fbCard.possession != CardPossession.OWNED
            _buttonWantedText.value = fbCard.possession != CardPossession.WANTED
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

//        viewModelScope.launch {
//            val lCard =
//                localDatabase?.magicCardDao()?.getCard(card.set.toString(), card.number.toString())
//            if (lCard != null) {
//                // card not owned -> remove it from the collection
//                if (lCard.possession == FBCardPossession.WANTED) {
//                    removeCardFromDatabase(lCard)
//                }
//            } else {
//                // card not in the local database -> add it as wanted in both the firebase and the local database
//                localDatabase?.magicCardDao()?.insertCard(
//                    MagicCardEntity(card, FBCardPossession.WANTED)
//                )
//                addCardToLocalDatabase(card, FBCardPossession.WANTED)
//                addCardToFirebase(FBCardPossession.WANTED)
//            }
//        }

        val data = userDB.getCardFromCollection(DBMagicCard(card))
        data.thenAccept {
            val fbCard = convertJsonToFBMagicCard(it.value as String)
            if (fbCard.possession == CardPossession.WANTED) {
                // Make the card as None in the firebase
                addCardToFirebase(CardPossession.NONE)
            }
        }.exceptionally {
            addCardToFirebase(CardPossession.WANTED)
            null
        }
    }

    /**
     * Manage the collection of cards owned by the user,
     * add the card if it's not in the collection,
     * remove it if it's in the collection
     */
    fun manageOwnedCollection() {
        val data = userDB.getCardFromCollection(DBMagicCard(card))
        data.thenAccept {
            val fbCard = convertJsonToFBMagicCard(it.value as String)
            if (fbCard.possession == CardPossession.OWNED) {
                // Make the card as None in the firebase
                addCardToFirebase(CardPossession.NONE)
//                removeCardFromFirebase()
            } else {
                addCardToFirebase(CardPossession.OWNED)
            }
        }.exceptionally {
            addCardToFirebase(CardPossession.OWNED)
            null
        }
    }

    /**
     * Add the card to the collection of the user
     * @param p the possession of the card (owned or wanted)
     */
    private fun addCardToFirebase(p: CardPossession) {
        val fbCard = DBMagicCard(card, p)
        userDB.addCardToCollection(fbCard)
        val data = userDB.getCardFromCollection(fbCard)
        data.thenAccept {
            if (p == CardPossession.OWNED) {
                _buttonAddText.value = false
                _buttonWantedText.value = true
            } else {
                _buttonWantedText.value = false

            }
        }
    }

    /**
     * Add the card to the local database
     * @param card the card to add
     * @param p the possession of the card (owned or wanted)
     */
    private fun addCardToLocalDatabase(card: MagicCard, p: CardPossession) {
        viewModelScope.launch {
            localDatabase?.magicCardDao()?.insertCard(DBMagicCard(card, p))
        }
    }

    /**
     * Remove the card from the collection of the user
     */
    private fun removeCardFromFirebase() {
        val fbCard = DBMagicCard(card)
        userDB.removeCardFromCollection(fbCard)
        val data = userDB.getCardFromCollection(fbCard)
        data.exceptionally {
            _buttonAddText.value = true
            _buttonWantedText.value = true
            null
        }
    }

    /**
     * Remove the card from the local database
     * @param card the card to remove
     */
    private fun removeCardFromDatabase(card: DBMagicCard) {
        viewModelScope.launch {
            localDatabase?.magicCardDao()?.deleteCard(card)
        }
    }
}
