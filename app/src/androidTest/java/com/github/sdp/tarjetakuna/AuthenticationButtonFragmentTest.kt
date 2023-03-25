package com.github.sdp.tarjetakuna

import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthenticationButtonFragmentTest {

    @get:Rule
    public val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        activityRule.scenario.onActivity { activity ->
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
            navController.navigate(R.id.nav_authentication_button)
        }
    }

    @Test
    fun testAuthenticationButtonActivity() {
        onView(withId(R.id.connectionButton)).perform(click())
        // Verify that we've navigated back to the right destination
        activityRule.scenario.onActivity { activity ->
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
            ViewMatchers.assertThat(
                navController.currentDestination?.id,
                Matchers.equalTo(R.id.nav_authentication)
            )
        }
    }

    @Test
    fun testAuthenticationButtonActivityBackToHome() {
        onView(withId(R.id.button_back_home)).perform(click())

        // Verify that we've navigated back to the right destination
        activityRule.scenario.onActivity { activity ->
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
            ViewMatchers.assertThat(
                navController.currentDestination?.id,
                Matchers.equalTo(R.id.nav_home)
            )
        }
    }
}
