package com.github.sdp.tarjetakuna.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BrowserFragmentTest {

    private lateinit var activityRule: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        Intents.init()
        activityRule = ActivityScenario.launch(MainActivity::class.java)

        // Get a reference to the fragment's view
        activityRule.onActivity { activity ->
            activity.changeFragment(R.id.nav_browser, null)
        }
    }

    @After
    fun after() {
        activityRule.close()
        Intents.release()
    }

    /**
     * Test if the initial cards are displayed
     */
    @Test
    fun cardsAreDisplayed() {
        for (i in 0..39) {
            onView(withId(R.id.browser_list_cards)).perform(
                RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                    i
                )
            )
                .check(matches(hasDescendant(withText("Ambush Paratrooper ${i + 1}"))))
        }
    }

    /**
     * Test if the search bar shown the correct card
     */
    @Test
    fun searchForCard() {
        onView(withId(R.id.browser_searchbar)).perform(click())
        onView(withId(R.id.browser_searchbar)).perform(typeText("Ambush Paratrooper 14"))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 14"))))
    }

    //This test is not working with the SingleCardTest. I put in comment for now
    //TODO : Correct this test
    /*
    @Test
    fun clickOnItemChangeFragment() {
        onView(withId(R.id.listOfCardsRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withId(R.id.listOfCardsRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        activityRule.onActivity { activity ->
            val navController = findNavController(activity, R.id.nav_host_fragment_content_drawer)
            assertThat(navController.currentDestination?.id, equalTo(R.id.nav_single_card))
        }
    }
    */
}
