package com.github.bjolidon.bootcamp.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.ui.browser.BrowserFragment
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
        Intents.release()
        scenario.close()
    }


    @Test
    fun textIsDisplayed() {
        onView(withId(R.id.text_browser)).check(matches(withText("Coming soon here: a search bar to filter your cards")))
    }

    @Test
    fun cardsAreDisplayed() {
        for (i in 0..39) {
            onView(withId(R.id.listOfCardsRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(i))
                .check(matches(hasDescendant(withText("Angel of Mercy ${i+1}"))))
        }

    }
}
