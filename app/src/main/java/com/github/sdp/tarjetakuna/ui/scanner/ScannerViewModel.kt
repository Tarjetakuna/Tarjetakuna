package com.github.sdp.tarjetakuna.ui.scanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock

class ScannerViewModel : ViewModel() {

    private val _textDetected = MutableLiveData<Text>()
    val textDetected: LiveData<Text> = _textDetected

    private val _objectDetected = MutableLiveData<Text>()
    val objectDetected: LiveData<Text> = _objectDetected

    /**
     * Callback for object detection errors. Should be safe, and not called often at all.
     */
    fun detectObjectError(exception: Exception) {
        _objectDetected.value = Text("Error $exception", emptyList<TextBlock>())
    }

    /**
     * Callback for object detection success.
     */
    fun detectObjectSuccess(text: String) {
        _objectDetected.value = Text("Success $text", emptyList<TextBlock>())
        TODO("Not yet implemented")
    }

    /**
     * Callback for text detection success.
     */
    fun detectTextSuccess(text: Text) {
        _textDetected.value = text
    }

    /**
     * Callback for text detection errors. Should be safe, and not called often at all.
     */
    fun detectTextError(exception: Exception) {
        _textDetected.value = Text("Error $exception", emptyList<TextBlock>())
    }
}
