package com.github.sdp.tarjetakuna.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

}
