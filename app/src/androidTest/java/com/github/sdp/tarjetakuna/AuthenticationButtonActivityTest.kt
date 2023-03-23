package com.github.sdp.tarjetakuna

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.ui.authentication.AuthenticationActivity
import com.github.sdp.tarjetakuna.ui.authentication.AuthenticationButtonActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthenticationButtonActivityTest {

        @get:Rule
        public val activityRule = ActivityScenarioRule(AuthenticationButtonActivity::class.java)

        @Before
        fun setUp() {
            Intents.init()
        }

        @After
        fun tearDown() {
            Intents.release()
        }

        @Test
        fun testAuthenticationButtonActivity() {
            onView(withId(R.id.connectionButton)).perform(click())
            intended(hasComponent(AuthenticationActivity::class.java.name))
        }

        @Test
        fun testAuthenticationButtonActivityBackToHome() {
            onView(withId(R.id.button_back_home)).perform(click())
            intended(hasComponent(MainActivity::class.java.name))
        }
}
