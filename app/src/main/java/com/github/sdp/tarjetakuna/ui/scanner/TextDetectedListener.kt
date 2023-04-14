package com.github.sdp.tarjetakuna.ui.scanner

import com.google.mlkit.vision.text.Text

/**
 * TextDetectedListener is an interface that is used to communicate between the ImageAnalyzer and the ScannerViewModel.
 */
interface TextDetectedListener {
    fun callback(text: Text) {}
    fun errorCallback(exception: Exception) {}
}
