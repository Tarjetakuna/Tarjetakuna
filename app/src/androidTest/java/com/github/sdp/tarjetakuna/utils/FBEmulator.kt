package com.github.sdp.tarjetakuna.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.junit.rules.ExternalResource

/**
 * Firebase Emulator for Testing
 */
object FBEmulator : ExternalResource() {
    val fb = Firebase.database
    override fun before() {
        try {
            fb.useEmulator("10.0.2.2", 9000)
        } catch (e: Exception) {
            println("emulator already running: ${e.message}")
        }
    }
}
