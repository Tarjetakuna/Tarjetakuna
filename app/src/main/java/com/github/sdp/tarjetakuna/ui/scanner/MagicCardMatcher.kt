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

        //
        val name = text.textBlocks[0].text
        Log.w(TAG, "name is: " + name)

        val type = text.textBlocks[1].text
        Log.w(TAG, "type is: " + type)

        val power = findPower(text)
        Log.w(TAG, "Power is: " + power)
        val card =
            MagicCard(
                text.textBlocks[0].text,
                power = power[0].first,
                toughness = power[0].second,
            )

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

    private fun findPower(text: Text): List<Pair<String, String>> {
        val power = emptyList<Pair<String, String>>().toMutableList()
        for (textBlock in text.textBlocks) {
            for (line in textBlock.lines) {
                for (element in line.elements) {
                    if (element.text.matches(Regex("\\d/\\d"))) {
                        val split = element.text.split("/")
                        if (split.size != 2) {
                            continue
                        }
                        power.add(Pair(split[0], split[1]))
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
