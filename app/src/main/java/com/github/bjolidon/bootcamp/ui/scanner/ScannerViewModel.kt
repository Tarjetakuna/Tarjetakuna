package com.github.bjolidon.bootcamp.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScannerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Coming soon here: a scanner to scan your cards"
    }
    val text: LiveData<String> = _text
}