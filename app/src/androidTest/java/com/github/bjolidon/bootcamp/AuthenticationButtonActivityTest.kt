package com.github.bjolidon.bootcamp

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.bjolidon.bootcamp.ui.authentication.AuthenticationActivity
import com.github.bjolidon.bootcamp.ui.authentication.AuthenticationButtonActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthenticationButtonActivityTest {

        @get:Rule
        public val activityRule = ActivityScenarioRule(AuthenticationButtonActivity::class.java)

        @Test
        fun testAuthenticationButtonActivity() {
            Intents.init()
            onView(withId(R.id.connectionButton)).perform(click())
            intended(hasComponent(AuthenticationActivity::class.java.name))
            Intents.release()
        }
}