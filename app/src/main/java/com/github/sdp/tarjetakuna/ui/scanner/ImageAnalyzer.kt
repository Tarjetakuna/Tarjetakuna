package com.github.sdp.tarjetakuna.ui.scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

/**
 * ImageAnalyzer is a class that implements the ImageAnalysis.Analyzer interface to analyze the image at runtime.
 * Analyzing is doing text recognition and object detection.
 */
class ImageAnalyzer(
    private val textDetectedListener: TextDetectedListener
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var _imageProxy: ImageProxy? = null

    /**
     * Detects text in the image.
     */
    private fun detectText(
        image: InputImage
    ): Task<Text> {
        return recognizer.process(image)
            .addOnSuccessListener { visionText -> textDetectedListener.callback(visionText); _imageProxy?.close() }
            .addOnFailureListener { e -> textDetectedListener.errorCallback(e); _imageProxy?.close() }
    }

    /**
     * Analyzes the image, called by the camera.
     */
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        _imageProxy = imageProxy
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            analyze(image)
        }
    }

    fun analyze(image: InputImage) {
        detectText(image)
    }
}
