package com.github.sdp.tarjetakuna.ui

import android.content.Context
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
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys
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

    /**
     * Test that the profile fragment is displayed when the profile icon is clicked
     */
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

    /**
     * Test that the user's name and description in the navigation header are updated when the
     * shared preferences are updated
     */
    @Test
    fun sharedPreferencesAreUpdated() {
        val user_name = "Jane"
        val user_description = "I am a collector of rare cards"
        activityRule.onActivity { activity ->
            val sharedPref = activity.getSharedPreferences(
                SharedPreferencesKeys.shared_pref_name,
                Context.MODE_PRIVATE
            )

            with(sharedPref.edit()) {
                putString(SharedPreferencesKeys.user_name, user_name)
                putString(SharedPreferencesKeys.user_description, user_description)
                apply()
            }
        }
        onView(withId(R.id.nav_header_name_textview)).check(matches(withText(user_name)))
        onView(withId(R.id.navHeaderDescriptionText)).check(matches(withText(user_description)))
    }
}
