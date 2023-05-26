package com.github.sdp.tarjetakuna

import android.Manifest
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.sdp.tarjetakuna.database.DatabaseSync
import com.github.sdp.tarjetakuna.utils.Utils

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignOutFragmentTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()
        DatabaseSync.activateSync = false

        activityRule = ActivityScenario.launch(MainActivity::class.java)

        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_sign_out)
        }
    }


    @Test
    fun testGreetingFragmentSignOut() {
        onView(withId(R.id.signOut_sign_out_button)).perform(click())
        activityRule.onActivity { activity ->
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
            // check if it goes back to nav_authentication first and then to nav_authentication_button
            assertEquals(
                navController.previousBackStackEntry?.destination?.id,
                R.id.nav_authentication
            )
            assertEquals(navController.currentDestination?.id, R.id.nav_authentication_button)
        }
    }

    @Test
    fun testGreetingFragmentBackToHome() {
        onView(withId(R.id.signOut_home_button)).perform(click())
        activityRule.onActivity { activity ->
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_drawer)
            assertEquals(navController.currentDestination?.id, R.id.nav_home)
        }
    }
}
