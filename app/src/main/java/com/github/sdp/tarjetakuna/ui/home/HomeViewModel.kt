package com.github.sdp.tarjetakuna.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.UserCardsRTDB
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import kotlinx.coroutines.launch

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

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected
    private var userDB = UserCardsRTDB()

    fun checkUserConnected() {
        _isConnected.value = userDB.isConnected()
    }


    // TODO remove this when we can research the cards and open them in the single card fragment
    // TODO the things to remove are: localDatabase, cards, generateCards, getRandomCard, the button
    var localDatabase: AppDatabase? = null

    private val cards = generateCards()

    fun addRandomCard() {
        val toInsert = cards.random()
        viewModelScope.launch {
            localDatabase?.magicCardDao()
                ?.insertCard(DBMagicCard.fromMagicCard(toInsert, CardPossession.OWNED))

        }.invokeOnCompletion {
            Log.i("Database", "set: ${toInsert.set}, number: ${toInsert.number} added to database")
        }
    }
}
