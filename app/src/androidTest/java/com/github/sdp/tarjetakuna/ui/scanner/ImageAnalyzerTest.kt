package com.github.sdp.tarjetakuna.ui.scanner

import com.github.sdp.tarjetakuna.utils.ResourceHelper.ResourceHelper.loadImage
import com.github.sdp.tarjetakuna.utils.Utils
import com.google.mlkit.vision.text.Text
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Test

class ImageAnalyzerTest {

    private var _callbackCalled = false
    private var _detectTextSuccess = false
    private var _exception: Exception? = null
    private var _text: Text? = null

    private fun mockTextDetectedListener(): TextDetectedListener {
        return object : TextDetectedListener {
            override fun callback(text: Text) {
                _callbackCalled = true
                println("callback called")
                println(text.text)
                _detectTextSuccess = true
                _text = text
            }

            override fun errorCallback(exception: Exception) {
                _callbackCalled = true
                println(exception)
                _detectTextSuccess = false
                _exception = exception
            }
        }
    }

    private val img = loadImage("tpr-12-exalted-dragon-normal.jpg")
    private val textsInImage = listOf(
        "Exalted Dragon",
        "Creature- Dragon",
        "Flying",
        "Exalted Dragon can't attack unless you",
        "sacrifice a land. (This cost is paid as",
        "attackers are declared.)",
        "If dragons excel at anything more than",
        "vanity, it is greed.",
        "O12/269",
        "TOP FN > MaTTHEW D, WILSON",
        "5/5",
        "IM && 2015 Wizards of the Coast"
    )

    @Before
    fun setUp() {

    }


    @Test
    fun test_textDetected() {
        val imageAnalyzer = ImageAnalyzer(mockTextDetectedListener())

        _callbackCalled = false
        imageAnalyzer.analyze(img)

        // Wait for the callback to be called
        Utils.waitUntilTrue(100, 50) { _callbackCalled }

        // if text detected, check it, else check that it failed with exception
        if (_detectTextSuccess) {
            assert(_text != null)
            assert(_text!!.textBlocks.size > 0)
            for (text in textsInImage) {
                assertThat("part of text is $text", _text!!.text, containsString(text))
            }
        } else {
            assert(_exception != null)
        }

    }
}
