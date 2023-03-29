package com.github.sdp.tarjetakuna.utils

import androidx.test.platform.app.InstrumentationRegistry
import java.io.IOException
import java.io.InputStreamReader

/**
 * Reads a file from the assets folder
 */
object FileReader {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = InstrumentationRegistry.getInstrumentation().targetContext
                .applicationContext.assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}
