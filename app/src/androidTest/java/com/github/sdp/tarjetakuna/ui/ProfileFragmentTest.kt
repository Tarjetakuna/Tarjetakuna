package com.github.sdp.tarjetakuna.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.ui.profile.ProfileFragment
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


}
