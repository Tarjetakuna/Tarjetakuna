package com.github.sdp.tarjetakuna.ui

import android.Manifest
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
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.DatabaseSync
import com.github.sdp.tarjetakuna.utils.FBEmulator
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys
import junit.framework.TestCase.assertEquals
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentInActivityTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>
    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    companion object {
        @get:ClassRule
        @JvmStatic
        val fbEmulator = FBEmulator()
    }

    @Before
    fun setUp() {
        DatabaseSync.activateSync = false
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
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        onView(withId(R.id.authentication_profile_icon)).perform(ViewActions.click())
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
        val username = "Jane"
        val userDescription = "I am a collector of rare cards"
        activityRule.onActivity { activity ->
            val sharedPref = activity.getSharedPreferences(
                SharedPreferencesKeys.shared_pref_name,
                Context.MODE_PRIVATE
            )

            with(sharedPref.edit()) {
                putString(SharedPreferencesKeys.user_name, username)
                putString(SharedPreferencesKeys.user_description, userDescription)
                apply()
            }
        }
        onView(withId(R.id.nav_header_name_textview)).check(matches(withText(username)))
        onView(withId(R.id.navHeaderDescriptionText)).check(matches(withText(userDescription)))
    }
}
