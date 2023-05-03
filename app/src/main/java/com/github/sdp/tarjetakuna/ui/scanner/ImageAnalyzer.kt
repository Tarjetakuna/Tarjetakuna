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
    private val textDetectedListener: TextDetectedListener,
    private val objectDetectedListener: ObjectDetectedListener
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    /**
     * Detects text in the image.
     */
    private fun detectText(
        image: InputImage
    ): Task<Text> {
        return recognizer.process(image)
            .addOnSuccessListener { visionText -> textDetectedListener.callback(visionText) }
            .addOnFailureListener { e -> textDetectedListener.errorCallback(e) }
    }

    /**
     * Detects objects in the image.
     */
    private fun detectObject(image: InputImage) {
        // TODO not implemented yet
    }

    /**
     * Analyzes the image, called by the camera.
     */
    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            analyze(image)
        }
    }

    fun analyze(image: InputImage) {
//        detectObject(image)
        detectText(image)
    }
}
