package com.github.sdp.tarjetakuna.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock

open class ScannerViewModel : ViewModel() {

    private val _textDetected = MutableLiveData<Text>()
    val textDetected: LiveData<Text> = _textDetected

    private val _objectDetected = MutableLiveData<Text>()
    val objectDetected: LiveData<Text> = _objectDetected

    /**
     * Callback for object detection errors. Should be safe, and not called often at all.
     */
    fun detectObjectError(exception: Exception) {
        _objectDetected.postValue(Text("Error $exception", emptyList<TextBlock>()))
    }

    /**
     * Callback for object detection success.
     */
    fun detectObjectSuccess(text: String) {
        _objectDetected.postValue(Text("Success $text", emptyList<TextBlock>()))
    }

    /**
     * Callback for text detection success.
     */
    fun detectTextSuccess(text: Text) {
        _textDetected.postValue(text)
    }

    /**
     * Callback for text detection errors. Should be safe, and not called often at all.
     */
    fun detectTextError(exception: Exception) {
        _textDetected.postValue(Text("Error $exception", emptyList<TextBlock>()))
    }
}
