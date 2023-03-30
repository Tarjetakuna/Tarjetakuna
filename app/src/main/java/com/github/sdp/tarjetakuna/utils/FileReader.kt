package com.github.sdp.tarjetakuna.utils


import android.content.Context
import android.content.res.AssetManager.AssetInputStream
import android.content.res.loader.AssetsProvider
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
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

    fun readFile(context: Context, fileName: String): String {
        val builder = StringBuilder()
        val reader = InputStreamReader(context.assets.open(fileName), "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    }
}
