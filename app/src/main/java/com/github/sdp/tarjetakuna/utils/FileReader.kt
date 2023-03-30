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
    /**
     * Reads a file from the assets folder in the application (need a context)
     */
    fun readStringFromFile(fileName: String): String {
        try {
            val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            return readFile(context, fileName)
        } catch (e: IOException) {
            throw e
        }
    }

    /**
     * Reads a file from the assets folder in the application (using the context)
     */
    fun readFile(context: Context, fileName: String): String {
        val builder = StringBuilder()
        val reader = InputStreamReader(context.assets.open(fileName), "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    }
}
