package com.github.sdp.tarjetakuna.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to Tarjetakuna!\n Sign in to manage your collection"
    }
    val text: LiveData<String> = _text

}
