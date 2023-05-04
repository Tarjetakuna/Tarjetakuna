package com.github.sdp.tarjetakuna.ui.scanner

import android.util.Log
import com.github.sdp.tarjetakuna.model.MagicCard
import com.google.mlkit.vision.text.Text

class MagicCardMatcher(
    private val text: Text
) {
    fun match(): MagicCard {
        return analyseText(text)
    }

    /**
     * Analyse the text in the image and return the likely card information based on position
     * of the text in image
     */
    private fun analyseText(text: Text): MagicCard {

        // TODO : analyse text and return card information
        Log.w(TAG, "Text is: " + text.text)

        // get the whole bounding box around the text (i.e. around the card)
//        val bb = text.textBlocks[0].boundingBox!!
//        for (textBlock in text.textBlocks) {
//            bb.union(textBlock.boundingBox!!)
//        }
//        Log.w(TAG, "Bounding box is: " + bb)

        var card = MagicCard()
        // get the name of the card
        if (text.textBlocks.size < 1) {
            Log.w(TAG, "No text blocks found for name")
        } else {
            val powerToughness = findPowerToughness(text)
            if (text.textBlocks.size < 2) {
                card = MagicCard(
                    name = text.textBlocks[0].text,
                    power = powerToughness.first,
                    toughness = powerToughness.second
                )
                Log.w(TAG, "No text blocks found for type")
            } else {
                val type = text.textBlocks[1].text
                Log.d(TAG, "type is: " + type)
                card = MagicCard(
                    name = text.textBlocks[0].text,
                    power = powerToughness.first,
                    toughness = powerToughness.second
                )
            }
        }

//        for (textBlock in text.textBlocks) {
//            Log.w(TAG, "TextBlock text is: " + textBlock.text)
//            Log.w(TAG, "TextBlock boundingbox is: " + textBlock.boundingBox)
//            Log.w(TAG, "TextBlock cornerpoint is: " + Arrays.toString(textBlock.cornerPoints))
//            bb.union(textBlock.boundingBox!!)
//            for (line in textBlock.lines) {
//                Log.w(TAG, "Line text is: " + line.text)
//                Log.w(TAG, "Line boundingbox is: " + line.boundingBox)
//                Log.w(TAG, "Line cornerpoint is: " + Arrays.toString(line.cornerPoints))
//                Log.w(TAG, "Line confidence is: " + line.confidence)
//                Log.w(TAG, "Line angle is: " + line.angle)
//                for (element in line.elements) {
//                    Log.w(TAG, "Element text is: " + element.text)
//                    Log.w(TAG, "Element boundingbox is: " + element.boundingBox)
//                    Log.w(TAG, "Element cornerpoint is: " + Arrays.toString(element.cornerPoints))
//                    Log.w(TAG, "Element language is: " + element.recognizedLanguage)
//                    Log.w(TAG, "Element confidence is: " + element.confidence)
//                    Log.w(TAG, "Element angle is: " + element.angle)
//                }
//            }
//        }

        return card
    }

    private fun findPowerToughness(text: Text): Pair<String, String> {
        var power = Pair("0", "0")
        for (textBlock in text.textBlocks) {
            for (line in textBlock.lines) {
                for (element in line.elements) {
                    if (element.text.matches(Regex("\\d/\\d"))) {
                        val split = element.text.split("/")
                        if (split.size != 2) {
                            continue
                        }
                        power = (Pair(split[0], split[1]))
                    }
                }
            }
        }
        return power
    }

    companion object CardMatcher {
        fun match(text: Text): MagicCard {
            return MagicCardMatcher(text).match()
        }

        const val TAG = "CardMatcher"
    }
}
