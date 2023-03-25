package com.github.sdp.tarjetakuna.ui

import android.content.Context
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.ui.profile.ProfileFragment
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    private lateinit var scenario: FragmentScenario<ProfileFragment>

    @Before
    fun setUp() {
        Intents.init()
        scenario = launchFragmentInContainer()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @After
    fun after() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun nameEntryDisplaysCorrectHint() {
        onView(withId(R.id.nameEntry)).check(matches(withHint(R.string.name_entry_hint)))
    }

    @Test
    fun descriptionEntryDisplaysCorrectHint() {
        onView(withId(R.id.descriptionEntry)).check(matches(withHint(R.string.description_entry_hint)))
    }

    @Test
    fun nameEntryChangesSharedPref() {
        val newName = "John"
        // First clear text; okay, Copilote ? ;)
        onView(withId(R.id.nameEntry)).perform(ViewActions.clearText())
        onView(withId(R.id.nameEntry)).perform(typeText(newName))
        closeSoftKeyboard()
        onView(withId(R.id.nameEntry)).check(matches(withText(newName)))

        val sharedPref = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("com.github.sdp.tarjetakuna", Context.MODE_PRIVATE)
        assertEquals(newName, sharedPref.getString("user_name", ""))
    }

    @Test
    fun descriptionEntryChangesSharedPref() {
        val newDescription = "I like hiking"
        onView(withId(R.id.descriptionEntry)).perform(ViewActions.clearText())
        onView(withId(R.id.descriptionEntry)).perform(typeText(newDescription))
        closeSoftKeyboard()
        onView(withId(R.id.descriptionEntry)).check(matches(withText(newDescription)))

        val sharedPref = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("com.github.sdp.tarjetakuna", Context.MODE_PRIVATE)
        assertEquals(newDescription, sharedPref.getString("user_description", ""))
    }

}
