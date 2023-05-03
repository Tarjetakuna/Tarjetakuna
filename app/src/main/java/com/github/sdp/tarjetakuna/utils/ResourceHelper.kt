package com.github.sdp.tarjetakuna.utils

import android.graphics.BitmapFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.google.mlkit.vision.common.InputImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Helper class to load resources, usable for unit tests that don't have a context
 */
class ResourceHelper {
    object ResourceHelper {

        fun loadImage(name: String): InputImage {
            val context =
                InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
            val istr = context.assets.open(name)
            val bitmap = BitmapFactory.decodeStream(istr)
            return InputImage.fromBitmap(bitmap, 0)
        }

        /**
         * Load the file [name] as a string
         */
        fun loadString(name: String?): String? {
            return loadString(ResourceHelper::class.java.classLoader, name)
        }

        /**
         * Load the file [name] as a string using the class loader
         */
        fun loadString(loader: ClassLoader?, name: String?): String? {
            if (loader == null) {
                return null
            }
            try {
                loader.getResourceAsStream(name).use { inputStream ->
                    return loadString(
                        inputStream
                    )
                }
            } catch (e: IOException) {
                return null
            } catch (e: NullPointerException) {
                return null
            }
        }

        /**
         * Read the input stream into a string
         */
        fun loadString(inputStream: InputStream?): String? {
            if (inputStream == null) {
                return null
            }
            try {
                ByteArrayOutputStream().use { result ->
                    val buffer = ByteArray(4096)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        result.write(buffer, 0, length)
                    }
                    return result.toString("UTF-8")
                }
            } catch (e: IOException) {
                return null
            }
        }
    }
}
