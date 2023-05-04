package com.github.sdp.tarjetakuna.ui.scanner

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock

open class ScannerViewModel : ViewModel() {

    private val _textDetected = MutableLiveData<Text>()
    val textDetected: LiveData<Text> = _textDetected

    /**
     * Callback for text detection success.
     */
    fun detectTextSuccess(text: Text) {
        _textDetected.postValue(text)
        Log.d("ScannerViewModel detectTextSuccess", "Success ${text.text}")
    }

    /**
     * Callback for text detection errors. Should be safe, and not called often at all.
     */
    fun detectTextError(exception: Exception) {
        _textDetected.postValue(Text("Error $exception", emptyList<TextBlock>()))
        Log.d("ScannerViewModel detectTextError", "Error $exception")
    }
}
