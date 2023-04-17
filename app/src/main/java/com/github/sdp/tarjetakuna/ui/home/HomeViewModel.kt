package com.github.sdp.tarjetakuna.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.sdp.tarjetakuna.database.FBCardPossession
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    // TODO remove this when we can research the cards and open them in the single card fragment
    // TODO the things to remove are: localDatabase, cards, generateCards, getRandomCard, the button
    var localDatabase: AppDatabase? = null

    private val cards = generateCards()

    fun addRandomCard() {
        val toInsert = cards.random()
        viewModelScope.launch {
            localDatabase?.magicCardDao()
                ?.insertCard(toInsert.toMagicCardEntity(FBCardPossession.OWNED))
        }.invokeOnCompletion {
            println("set: ${toInsert.set}, number: ${toInsert.number}")
        }
    }
}
