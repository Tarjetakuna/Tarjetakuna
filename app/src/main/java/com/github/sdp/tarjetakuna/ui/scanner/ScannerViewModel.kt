package com.github.sdp.tarjetakuna.ui.scanner

import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.utils.Compatibility
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock

class ScannerViewModel : ViewModel() {

    private val _textInformationId = MutableLiveData<Int>()
    val textInformationId: LiveData<Int> = _textInformationId

    private val _picture = MutableLiveData<Bitmap>()
    val picture: LiveData<Bitmap> = _picture

    private val _textDetected = MutableLiveData<Text>()
    val textDetected: LiveData<Text> = _textDetected

    private val _objectDetected = MutableLiveData<Text>()
    val objectDetected: LiveData<Text> = _objectDetected


    fun setActivityResult(activityResult: ActivityResult) {
        setTextInformation(activityResult.resultCode)
        setPicture(Compatibility.getDataActivityResult(activityResult, "data", Bitmap::class.java))
    }

    private fun setTextInformation(activityResult: Int) {
        if (activityResult == RESULT_OK) {
            _textInformationId.value = R.string.operation_success
        } else {
            _textInformationId.value = R.string.operation_failed
        }
    }

    private fun setPicture(picture: Bitmap?) {
        if (picture != null) {
            _picture.value = picture!!
        }
    }

    fun detectText() {
        if (picture.value == null) return

        val imageAnalyzer = ImageAnalyzer()
        imageAnalyzer.detectText(bitmap = picture.value!!, ::detectTextSuccess, ::detectTextError)
    }

    private fun detectTextSuccess(text: Text) {
        _textDetected.value = text
    }

    private fun detectTextError(exception: Exception) {
        _textDetected.value = Text("Error $exception", emptyList<TextBlock>())
    }

    fun detectObject() {

    }
}
