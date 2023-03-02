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
class GreetingActivityTest {

    @Test
    fun testGreetingActivity() {
        val intent = Intent(getApplicationContext(), GreetingActivity::class.java)
        intent.putExtra("name2", "testName")
        val activity = ActivityScenario.launch<GreetingActivity>(intent)
        onView(withId(R.id.greetingMessage)).check(matches(withText("Hello testName!")))
        activity.close()
    }
}