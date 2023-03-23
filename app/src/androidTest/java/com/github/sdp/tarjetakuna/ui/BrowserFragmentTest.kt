package com.github.sdp.tarjetakuna.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.ui.browser.BrowserFragment
import com.github.sdp.tarjetakuna.ui.browser.DisplayCardsAdapter
import com.github.sdp.tarjetakuna.ui.singlecard.SingleCardFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrowserFragmentTest {

    private lateinit var scenario: FragmentScenario<BrowserFragment>

    @Before
    fun setUp() {
        Intents.init()
        scenario = launchFragmentInContainer()
    }

    @After
    fun after() {
        scenario.close()
        Intents.release()
    }

    @Test
    fun textIsDisplayed() {
        onView(withId(R.id.text_browser)).check(matches(withText("Coming soon here: a search bar to filter your cards")))
    }

    @Test
    fun cardsAreDisplayed() {
        for (i in 0..39) {
            onView(withId(R.id.listOfCardsRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(i))
                .check(matches(hasDescendant(withText("Ambush Paratrooper ${i+1}"))))
        }
    }

    @Test
    fun clickOnItemChangeFragment() {
        onView(withId(R.id.listOfCardsRecyclerView)).perform(actionOnItemAtPosition<DisplayCardsAdapter.ViewHolder>(0, click()))

        //Intents.intended(hasComponent(SingleCardFragment::class.java.name))
    }
}
