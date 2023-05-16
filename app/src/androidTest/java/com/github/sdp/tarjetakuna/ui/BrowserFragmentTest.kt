package com.github.sdp.tarjetakuna.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.ui.browser.BrowserFragment
import com.github.sdp.tarjetakuna.utils.TemporaryCards.generateCards
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class BrowserFragmentTest {

    private lateinit var scenario: FragmentScenario<BrowserFragment>


    @Before
    fun setUp() {
        Intents.init()

        // mock the authentication
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(true)
        SignIn.setSignIn(mockedAuth)

        // close the database that could have been opened because of the previous tests
        LocalDatabaseProvider.closeDatabase("test")
        LocalDatabaseProvider.closeDatabase("test2")
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)

        LocalDatabaseProvider.setDatabase(
            ApplicationProvider.getApplicationContext(),
            LocalDatabaseProvider.CARDS_DATABASE_NAME,
            true
        )
        runBlocking {
            withTimeout(5000) {
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
                    ?.magicCardDao()?.insertCards(
                        generateCards().map { DBMagicCard.fromMagicCard(it, CardPossession.OWNED) }
                    )
            }
        }

        scenario = launchFragmentInContainer()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @After
    fun after() {
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
        scenario.close()
        Intents.release()
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

    @Test
    fun checkIfClearFilterButtonWorks() {
        onView(withId(R.id.browser_filter_button)).perform(click())
        onView(withId(R.id.browser_filter_by_set_edittext)).perform(typeText("M15"))
        onView(withId(R.id.browser_sort_button)).perform(click())
        onView(withId(R.id.browser_sort_by_rarity_button)).perform(click())
        onView(withId(R.id.browser_clear_filters)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 01"))))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                9
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 10"))))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                40
            )
        ).check(matches(hasDescendant(withText("Pégase solgrâce"))))
    }

    @Test
    fun checkSwapFromFilterToSortView() {
        onView(withId(R.id.browser_filter_button)).perform(click())
        onView(withId(R.id.browser_sort_button)).perform(click())
        onView(withId(R.id.browser_sort_box)).check(matches(isDisplayed()))
        onView(withId(R.id.browser_filter_box)).check(matches(not(isDisplayed())))
        onView(withId(R.id.browser_filter_button)).perform(click())
        onView(withId(R.id.browser_filter_box)).check(matches(isDisplayed()))
        onView(withId(R.id.browser_sort_box)).check(matches(not(isDisplayed())))
    }

    @Test
    fun checkFilterAndSortBoxDisappearAfterClickingTwice() {
        onView(withId(R.id.browser_sort_button)).perform(click())
        onView(withId(R.id.browser_sort_button)).perform(click())
        onView(withId(R.id.browser_filter_box)).check(matches(not(isDisplayed())))
        onView(withId(R.id.browser_sort_box)).check(matches(not(isDisplayed())))
        onView(withId(R.id.browser_filter_button)).perform(click())
        onView(withId(R.id.browser_filter_button)).perform(click())
        onView(withId(R.id.browser_filter_box)).check(matches(not(isDisplayed())))
        onView(withId(R.id.browser_sort_box)).check(matches(not(isDisplayed())))
    }

    @Test
    fun filterByManaCostTest() {
        onView(withId(R.id.browser_filter_button)).perform(click())
        onView(withId(R.id.browser_filter_by_mana_edittext)).perform(click())
        onView(withId(R.id.browser_filter_by_mana_edittext)).perform(typeText("2"))
        onView(withId(R.id.browser_filter_by_mana_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 01"))))
        onView(withId(R.id.browser_filter_by_mana_edittext)).perform(clearText())
        onView(withId(R.id.browser_filter_by_mana_edittext)).perform(typeText("3"))
        onView(withId(R.id.browser_filter_by_mana_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Pégase solgrâce"))))
    }

    @Test
    fun filterWithAIllegalManaValueShouldRemoveFilter() {
        onView(withId(R.id.browser_filter_button)).perform(click())
        onView(withId(R.id.browser_filter_by_mana_edittext)).perform(click())
        onView(withId(R.id.browser_filter_by_mana_edittext)).perform(typeText("3"))
        onView(withId(R.id.browser_filter_by_mana_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Pégase solgrâce"))))
        onView(withId(R.id.browser_filter_by_mana_edittext)).perform(clearText())
        onView(withId(R.id.browser_filter_by_mana_edittext)).perform(typeText("a"))
        onView(withId(R.id.browser_filter_by_mana_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 01"))))
    }

    @Test
    fun filterBySet() {
        onView(withId(R.id.browser_filter_button)).perform(click())
        onView(withId(R.id.browser_filter_by_set_edittext)).perform(click())
        onView(withId(R.id.browser_filter_by_set_edittext)).perform(typeText("M15"))
        onView(withId(R.id.browser_filter_by_set_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Pégase solgrâce"))))
    }

    @Test
    fun sortByManaCost() {
        onView(withId(R.id.browser_sort_button)).perform(click())
        onView(withId(R.id.browser_sort_by_mana_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 01"))))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                40
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 40"))))
    }

    @Test
    fun sortByName() {
        onView(withId(R.id.browser_sort_button)).perform(click())
        onView(withId(R.id.browser_sort_by_name_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 01"))))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 11"))))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                40
            )
        ).check(matches(hasDescendant(withText("Pégase solgrâce"))))
    }

    @Test
    fun sortByRarity() {
        onView(withId(R.id.browser_sort_button)).perform(click())
        onView(withId(R.id.browser_sort_by_rarity_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Pégase solgrâce"))))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                40
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 40"))))
    }

    @Test
    fun sortBySet() {
        onView(withId(R.id.browser_sort_button)).perform(click())
        onView(withId(R.id.browser_sort_by_set_button)).perform(click())
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                0
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 01"))))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                2
            )
        ).check(matches(hasDescendant(withText("Ambush Paratrooper 03"))))
        onView(withId(R.id.browser_list_cards)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                40
            )
        ).check(matches(hasDescendant(withText("Pégase solgrâce"))))
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
