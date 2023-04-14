package com.github.sdp.tarjetakuna.ui.scanner

/**
 * ObjectDetectedListener is an interface that is used to communicate between the ImageAnalyzer and the ScannerViewModel.
 */
interface ObjectDetectedListener {
    fun callback(text: String) {}
    fun errorCallback(exception: Exception) {}
}
