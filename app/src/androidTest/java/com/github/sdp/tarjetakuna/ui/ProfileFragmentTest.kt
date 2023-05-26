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
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.ui.profile.ProfileFragment
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys
import com.github.sdp.tarjetakuna.utils.SharedPreferencesKeys.shared_pref_name
import com.github.sdp.tarjetakuna.utils.Utils
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
        Utils.useFirebaseEmulator()
        scenario = launchFragmentInContainer()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @After
    fun after() {
        scenario.close()
    }

    @Test
    fun nameEntryDisplaysCorrectHint() {
        onView(withId(R.id.profile_name_edittext)).check(matches(withHint(R.string.name_entry_hint)))
    }

    @Test
    fun descriptionEntryDisplaysCorrectHint() {
        onView(withId(R.id.profile_description_edittext)).check(matches(withHint(R.string.description_entry_hint)))
    }

    @Test
    fun nameEntryChangesSharedPref() {
        val newName = "John"
        onView(withId(R.id.profile_name_edittext)).perform(ViewActions.clearText())
        onView(withId(R.id.profile_name_edittext)).perform(typeText(newName))
        closeSoftKeyboard()
        onView(withId(R.id.profile_name_edittext)).check(matches(withText(newName)))

        val sharedPref = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE)
        assertEquals(newName, sharedPref.getString(SharedPreferencesKeys.user_name, ""))
    }

    @Test
    fun descriptionEntryChangesSharedPref() {
        val newDescription = "I like hiking"
        onView(withId(R.id.profile_description_edittext)).perform(ViewActions.clearText())
        onView(withId(R.id.profile_description_edittext)).perform(typeText(newDescription))
        closeSoftKeyboard()
        onView(withId(R.id.profile_description_edittext)).check(matches(withText(newDescription)))

        val sharedPref = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences(shared_pref_name, Context.MODE_PRIVATE)
        assertEquals(
            newDescription,
            sharedPref.getString(SharedPreferencesKeys.user_description, "")
        )
    }
}
