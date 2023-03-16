package com.github.bjolidon.bootcamp.ui.browser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BrowserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Coming soon here: a search bar to filter your cards"
    }
    val text: LiveData<String> = _text
}