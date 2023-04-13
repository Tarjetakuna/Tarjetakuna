package com.github.sdp.tarjetakuna.ui.scanner

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ImageAnalyzer {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun detectText(bitmap: Bitmap, callback: (Text) -> Unit, errorCallback: (Exception) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = recognizer.process(image)
            .addOnSuccessListener { visionText -> callback(visionText) }
            .addOnFailureListener { e -> errorCallback(e) }
    }
}
