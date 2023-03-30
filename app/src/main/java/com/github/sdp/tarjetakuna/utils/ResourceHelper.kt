package com.github.sdp.tarjetakuna.utils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class ResourceHelper {
    object ResourceHelper {
        fun loadString(name: String?): String? {
            return loadString(ResourceHelper::class.java.classLoader, name)
        }

        fun loadString( loader: ClassLoader?, name: String?): String? {
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

        fun loadString( inputStream: InputStream?): String? {
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
