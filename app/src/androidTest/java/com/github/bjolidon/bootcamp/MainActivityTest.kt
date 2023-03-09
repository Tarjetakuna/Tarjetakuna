package com.github.bjolidon.bootcamp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {



    @Test
    fun testSetThenGetSame() {
        val emailtext = " test2@gmail.com"
        val phonetext = "12345678901"
        val db = Firebase.database
        db.useEmulator("10.0.2.2", 9000)
        val intent = Intent(getApplicationContext(), MainActivity::class.java)
        val activity = ActivityScenario.launch<MainActivity>(intent)
        onView(withId(R.id.phonenumber)).perform(typeText(phonetext))
        onView(withId(R.id.email)).perform(typeText(emailtext))
        onView(withId(R.id.set_button)).perform(click())
        //test the set value is indeed inside the database
        db.reference.child(phonetext).get().addOnSuccessListener {
            assertNotEquals(phonetext, it.value)
            assertEquals(emailtext, it.value)
        }

    }
    // 10.0.2.2 is the special IP address to connect to the 'localhost' of
    // the host computer from an Android emulator.

    // when onDataChange is called, we detect set has occurred
    // and then we can check the value of the email


}