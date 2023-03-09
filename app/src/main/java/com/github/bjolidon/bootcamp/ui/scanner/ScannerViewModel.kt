package com.github.bjolidon.bootcamp.ui.scanner

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScannerViewModel : ViewModel() {

    private val _textInformation = MutableLiveData<String>()
    val textInformation: LiveData<String> = _textInformation

    private val _image = MutableLiveData<Bitmap>()
    val image: LiveData<Bitmap> = _image

    fun setTextInformation(activityResult: Int) {
        when (activityResult) {
            RESULT_OK -> {
                _textInformation.value = "Success"
            }
            RESULT_CANCELED -> {
                _textInformation.value = "The operation has been cancelled. Please try again."
            }
            else -> {
                _textInformation.value = "An error has occurred. Please try again."
            }
        }
    }

    fun setImage(image: Bitmap) {
        _image.value = image
    }

}