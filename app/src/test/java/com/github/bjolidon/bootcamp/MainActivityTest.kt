package com.github.bjolidon.bootcamp

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.junit.Assert.assertEquals
import org.junit.Test

class MainActivityTest {
    @Test
    fun testSetThenGetSame() {
        val emailtext = " testSend@gmail.com"
        val phonetext = "7777777"
        val db = Firebase.database
        db.useEmulator("10.0.2.2", 9000)
        val activity = MainActivity()
        activity.sendToDatabase(phonetext, emailtext)
        assertEquals(emailtext, activity.getFromDatabase(phonetext))
    }
}