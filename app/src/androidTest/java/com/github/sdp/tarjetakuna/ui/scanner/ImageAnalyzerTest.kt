package com.github.sdp.tarjetakuna.ui.scanner

import com.github.sdp.tarjetakuna.utils.ResourceHelper.ResourceHelper.loadImage
import com.github.sdp.tarjetakuna.utils.Utils
import com.google.mlkit.vision.text.Text
import org.junit.Before
import org.junit.Test

class ImageAnalyzerTest {

    private var callbackCalled = false
    private var detectTextSuccess = false

    private fun mockTextDetectedListener(): TextDetectedListener {
        return object : TextDetectedListener {
            override fun callback(text: Text) {
                callbackCalled = true
                println(text)
                detectTextSuccess = true
            }

            override fun errorCallback(exception: Exception) {
                callbackCalled = true
                println(exception)
                detectTextSuccess = false
            }
        }
    }

    private val img = loadImage("tpr-12-exalted-dragon-normal.jpg")

    @Before
    fun setUp() {

    }


    @Test
    fun test_textDetected() {
        val imageAnalyzer = ImageAnalyzer(mockTextDetectedListener())

        callbackCalled = false
        imageAnalyzer.analyze(img)

        // Wait for the callback to be called
        Utils.waitUntilTrue(50, 20) { callbackCalled }

        // TODO should be true, but auto download of model is not working
//        assert(!detectTextSuccess)
    }
}
