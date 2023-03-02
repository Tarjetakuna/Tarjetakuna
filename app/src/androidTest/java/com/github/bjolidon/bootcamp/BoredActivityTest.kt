package com.github.bjolidon.bootcamp

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoredActivityTest {


    @Test
    fun testBoredActivity() {
        val intent = Intent(getApplicationContext(), BoredActivity::class.java)
        val activity = ActivityScenario.launch<BoredActivity>(intent)
//        onView(withId(R.id.bored_txtview)).check(matches(withText(getString(R.string.txt_getting_bored))))
        onView(withId(R.id.bored_txtview)).check(matches(withText("click to get boring info")))
        activity.close()
    }
}