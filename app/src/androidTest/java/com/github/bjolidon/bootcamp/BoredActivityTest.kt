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
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoredActivityTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testBoredActivity_startActivity_initText() {
        val intent = Intent(getApplicationContext(), BoredActivityTestApp::class.java)
        val activity = ActivityScenario.launch<BoredActivityTestApp>(intent)
        onView(withId(R.id.bored_txtview)).check(matches(withText(R.string.txt_getting_bored)))
        activity.close()
    }

    @Test
    fun testBoredActivity_startActivity_onClick() {
        val intent = Intent(getApplicationContext(), BoredActivityTestApp::class.java)
        val activity = ActivityScenario.launch<BoredActivityTestApp>(intent)
        onView(withId(R.id.so_bored_btn)).perform(click())
        onView(withId(R.id.bored_txtview)).check(matches(withText("click to get boring info")))
        activity.close()
    }
}