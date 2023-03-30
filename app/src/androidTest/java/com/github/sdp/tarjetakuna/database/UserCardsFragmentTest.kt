package com.github.sdp.tarjetakuna.database

import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserCardsFragmentTest {
//    @get:Rule
//    public val activityRule = ActivityScenarioRule(MainActivity::class.java)
//
//    @Before
//    fun setUp() {
//        activityRule.scenario.onActivity { activity ->
//            val navController =
//                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
//            navController.navigate(R.id.nav_user_cards)
//        }
//    }
//
//
//    @Test
//    fun testBackHomeFromManageCollection() {
//        onView(withId(R.id.home_button)).perform(click())
//
//        // Verify that we've navigated back to the right destination
//        activityRule.scenario.onActivity { activity ->
//            val navController =
//                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
//            assertEquals(navController.currentDestination?.id, R.id.nav_home)
//        }
//    }
}
