package com.github.sdp.tarjetakuna.ui.scanner

import android.app.Activity.RESULT_OK
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.R

class ScannerViewModel : ViewModel() {

    private val _textInformationId = MutableLiveData<Int>()
    val textInformationId: LiveData<Int> = _textInformationId

    fun setTextInformation(activityResult: Int) {
        if (activityResult == RESULT_OK) {
            _textInformationId.value = R.string.operation_success
        }
        else  {
            _textInformationId.value = R.string.operation_failed
        }
    }
}
