package com.github.sdp.tarjetakuna.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.local.AppDatabase
import com.github.sdp.tarjetakuna.ui.authentication.SignIn

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


}
