package com.github.sdp.tarjetakuna

import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class AuthenticationButtonFragmentTest {

    @get:Rule
    public val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        SignIn.setSignIn(mockedAuth)

        activityRule.scenario.onActivity { activity ->
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
            navController.navigate(R.id.nav_authentication_button)
        }
    }

    // TODO fix this test because it is not working on cirrus CI for some reason
    @Test
    fun testAuthenticationButtonActivity() {
        onView(withId(R.id.authenticationButton_sign_in_button)).perform(click())
        // Verify that we've navigated back to the right destination
        activityRule.scenario.onActivity { activity ->
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)

            var isRightWindow = false
            var index = 0
            // Check every one second if it works, maybe it is a race condition
            while (!isRightWindow) {
                if (navController.currentDestination?.id == R.id.nav_authentication) {
                    isRightWindow = true
                } else {
                    Thread.sleep(1000)
                    index += 1
                }
                if (index == 5) {
                    Assert.fail("The fragment is not the right one")
                }
            }
        }
    }

    @Test
    fun testAuthenticationButtonActivityBackToHome() {
        onView(withId(R.id.authenticationButton_home_button)).perform(click())
        // Verify that we've navigated back to the right destination
        activityRule.scenario.onActivity { activity ->
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
            assertEquals(navController.currentDestination?.id, R.id.nav_home)
        }
    }

}
