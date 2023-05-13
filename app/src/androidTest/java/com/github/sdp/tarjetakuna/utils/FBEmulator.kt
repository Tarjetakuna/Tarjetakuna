package com.github.sdp.tarjetakuna.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Use Firebase Emulator for Testing
 */
class FBEmulator : TestWatcher() {
    val fb = Firebase.database
    override fun starting(description: Description) {
        super.starting(description)
        try {
            fb.useEmulator("10.0.2.2", 9000)
        } catch (e: Exception) {
            println("emulator running: ${e.message}")
        }
    }
}
