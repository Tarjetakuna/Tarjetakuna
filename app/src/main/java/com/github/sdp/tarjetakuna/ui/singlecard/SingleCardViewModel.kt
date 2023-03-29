package com.github.sdp.tarjetakuna.ui.singlecard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.MagicCard
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SingleCardViewModel : ViewModel() {

    lateinit var card: MagicCard

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private var user = Firebase.auth.currentUser

    fun checkUserConnected() {
        //TODO : Check if the user is connected
        user = Firebase.auth.currentUser
        _isConnected.value = user != null
    }

    /**
     * Add the card to the collection of the user
     */
    fun addCardToCollection() {

        //TODO : Add the card to the collection
    }

    /**
     * Add the card to the wanted cards of the user
     */
    fun addCardToWanted() {
        //TODO : Add the card to the wanted cards
    }


}
