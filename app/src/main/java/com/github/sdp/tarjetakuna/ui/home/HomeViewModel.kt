package com.github.sdp.tarjetakuna.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _titleText = MutableLiveData<String>()
    val titleText: LiveData<String> = _titleText

    private val _descriptionText = MutableLiveData<String>()
    val descriptionText: LiveData<String> = _descriptionText

    //manage sign in state
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    fun checkUserConnected() {
        _isConnected.value = SignIn.getSignIn().isUserLoggedIn()
    }


    // TODO remove this when we can research the cards and open them in the single card fragment
    // TODO the things to remove are: localDatabase, cards, generateCards, getRandomCard, the button
    var localDatabase: AppDatabase? = null

    private val cards = generateCards()

    fun addRandomCard() {
        val toInsert = cards.random()
        viewModelScope.launch {
            val lCard =
                localDatabase?.magicCardDao()
                    ?.getCard(toInsert.set.code, toInsert.number.toString())
            if (lCard != null) {
                localDatabase?.magicCardDao()?.insertCard(
                    DBMagicCard(toInsert, CardPossession.OWNED, lCard.quantity + 1)
                )
            } else {
                localDatabase?.magicCardDao()
                    ?.insertCard(DBMagicCard(toInsert, CardPossession.OWNED, 1))
            }

        }.invokeOnCompletion {
            Log.i("Database", "set: ${toInsert.set}, number: ${toInsert.number} added to database")
        }
    }
}
