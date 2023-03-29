package com.github.sdp.tarjetakuna.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentInActivityTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>
    val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )

    @Before
    fun setUp() {
        activityRule = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun after() {
        activityRule.close()
    }

    @Test
    fun navigatingToProfileFragment() {
        // Click on the profile icon in the navigation header
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.profileIcon)).perform(ViewActions.click())
        val id = navController.currentDestination?.id
        if (id != null) {
            assertEquals(R.id.nav_profile, id)
        } else {
            onView(withId(R.id.profile_picture)).check(matches(isDisplayed()))
        }
    }
}
