package com.github.sdp.tarjetakuna.ui

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentInActivityTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        Intents.init()
        activityRule = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun after() {
        activityRule.close()
        Intents.release()
    }

    @Test
    fun nameEntryChangesSharedPref() {
        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_profile)
        }
        val newName = "John"
        // First clear text; okay, Copilote ? ;)
        Espresso.onView(withId(R.id.nameEntry)).perform(ViewActions.clearText())
        Espresso.onView(withId(R.id.nameEntry)).perform(ViewActions.typeText(newName))
        Espresso.closeSoftKeyboard()
        Espresso.onView(withId(R.id.nameEntry)).check(ViewAssertions.matches(withText(newName)))

        val sharedPref = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("com.github.sdp.tarjetakuna", Context.MODE_PRIVATE)
        TestCase.assertEquals(newName, sharedPref.getString("user_name", ""))
    }

    @Test
    fun descriptionEntryChangesSharedPref() {
        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_profile)
        }
        val newDescription = "I like hiking"
        Espresso.onView(withId(R.id.descriptionEntry)).perform(ViewActions.clearText())
        Espresso.onView(withId(R.id.descriptionEntry)).perform(ViewActions.typeText(newDescription))
        Espresso.closeSoftKeyboard()
        Espresso.onView(withId(R.id.descriptionEntry))
            .check(ViewAssertions.matches(withText(newDescription)))

        val sharedPref = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("com.github.sdp.tarjetakuna", Context.MODE_PRIVATE)
        TestCase.assertEquals(newDescription, sharedPref.getString("user_description", ""))
    }
}
