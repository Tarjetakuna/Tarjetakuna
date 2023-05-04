package com.github.sdp.tarjetakuna.ui

import androidx.navigation.Navigation.findNavController
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Test that the authentication fragment is displayed when the corresponding button is clicked
     */
    @Test
    fun testClickOnSignInGoogle() {
        onView(withId(R.id.home_authentication_button)).perform(click())

        activityRule.scenario.onActivity { activity ->
            val navController = findNavController(activity, R.id.nav_host_fragment_content_drawer)
            assertThat(
                navController.currentDestination?.id,
                equalTo(R.id.nav_authentication_button)
            )
        }

    }

//    @Test
//    fun testGreetingFragmentSignOut() {
//        onView(withId(R.id.home_signOut_button)).perform(click())
//        activityRule.scenario.onActivity { activity ->
//            val navController =
//                findNavController(activity, R.id.nav_host_fragment_content_drawer)
//            // check if it goes back to nav_authentication first and then to nav_authentication_button
//            Assert.assertEquals(
//                navController.previousBackStackEntry?.destination?.id,
//                R.id.nav_authentication
//            )
//            Assert.assertEquals(
//                navController.currentDestination?.id,
//                R.id.nav_authentication_button
//            )
//        }
//    }

//    @Test
//    fun testGreetingFragmentBackToHome() {
//        onView(withId(R.id.authenticationButton_signOut_button)).perform(click())
//        activityRule.scenario.onActivity { activity ->
//            val navController =
//                findNavController(activity, R.id.nav_host_fragment_content_drawer)
//            Assert.assertEquals(navController.currentDestination?.id, R.id.nav_home)
//        }
//    }

}
