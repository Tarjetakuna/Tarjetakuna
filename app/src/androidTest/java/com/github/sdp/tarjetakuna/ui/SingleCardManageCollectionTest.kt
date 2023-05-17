package com.github.sdp.tarjetakuna.ui

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.ui.authentication.Authenticator
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.github.sdp.tarjetakuna.ui.singlecard.SingleCardFragment
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class SingleCardManageCollectionTest {

    private lateinit var scenario: FragmentScenario<SingleCardFragment>

    private val cardQuantityText = onView(withId(R.id.singleCard_quantity_text))
    private val wantedButton = onView(withId(R.id.singleCard_add_wanted_button))
    private val addCardButton = onView(withId(R.id.singleCard_add_card_button))
    private val removeCardButton = onView(withId(R.id.singleCard_remove_card_button))

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
                    ?.magicCardDao()?.insertCard(
                        DBMagicCard(CommonMagicCard.aeronautTinkererCard, CardPossession.OWNED, 2),
                    )
            }
        }

        val bundleArgs =
            Bundle().apply {
                putString(
                    "card",
                    Gson().toJson(CommonMagicCard.aeronautTinkererCard)
                )
            }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)
        scenario.moveToState(Lifecycle.State.STARTED)

        Thread.sleep(1000)
        wantedButton.perform(scrollTo())
    }

    @After
    fun tearDown() {
        Intents.release()
        scenario.close()
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
    }

    @Test
    fun checkButtonsAreDisplayed() {
        cardQuantityText.check(matches(isDisplayed()))
        addCardButton.check(matches(isDisplayed()))
        removeCardButton.check(matches(isDisplayed()))
        wantedButton.check(matches(isDisplayed()))
    }

    @Test
    fun cardQuantityIsCorrect() {
        // check that the quantity displayed is the one in the database
        cardQuantityText.check(matches(withText("2")))
    }

    @Test
    fun cardQuantityWhenAddingCardIsCorrect() {
        addCardButton.perform(click())
        cardQuantityText.check(matches(withText("3")))
        addCardButton.perform(click())
        cardQuantityText.check(matches(withText("4")))
    }

    @Test
    fun cardQuantityWhenRemovingCardIsCorrect() {
        removeCardButton.perform(click())
        cardQuantityText.check(matches(withText("1")))
        removeCardButton.perform(click())
        cardQuantityText.check(matches(withText("0")))
        removeCardButton.perform(click())
        cardQuantityText.check(matches(withText("0")))
    }

    @Test
    fun cardQuantityWhenAddingAndRemovingCardIsCorrect() {
        addCardButton.perform(click())
        removeCardButton.perform(click())
        addCardButton.perform(click())
        removeCardButton.perform(click())
        removeCardButton.perform(click())
        cardQuantityText.check(matches(withText("1")))
    }

    @Test
    fun addToWantedCardsChangeText() {
        wantedButton.perform(click())
        wantedButton.check(matches(withText(R.string.single_card_showing_remove_wanted)))
        wantedButton.perform(click())
        wantedButton.check(matches(withText(R.string.single_card_showing_add_to_wanted)))
    }

    @Test
    fun addToWantedPutQuantityToZero() {
        wantedButton.perform(click())
        cardQuantityText.check(matches(withText("0")))
    }

}

@RunWith(AndroidJUnit4::class)
class SingleCardManageCollectionNotLoggedInTest {

    @Test
    fun buttonAreNotDisplayedWhenNotLoggedIn() {
        // mock the authentication
        val mockedAuth = Mockito.mock(Authenticator::class.java)
        Mockito.`when`(mockedAuth.isUserLoggedIn()).thenReturn(false)
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
                    ?.magicCardDao()?.insertCard(
                        DBMagicCard(CommonMagicCard.aeronautTinkererCard, CardPossession.OWNED, 2),
                    )
            }
        }

        val bundleArgs =
            Bundle().apply {
                putString(
                    "card",
                    Gson().toJson(CommonMagicCard.aeronautTinkererCard)
                )
            }
        val scenario = launchFragmentInContainer<SingleCardFragment>(fragmentArgs = bundleArgs)
        scenario.moveToState(Lifecycle.State.STARTED)

        Thread.sleep(4000)


        onView(withId(R.id.singleCard_askConnection_text)).perform(scrollTo())
        onView(withId(R.id.singleCard_quantity_text)).check(matches(not(isDisplayed())))
        onView(withId(R.id.singleCard_add_card_button)).check(matches(not(isDisplayed())))
        onView(withId(R.id.singleCard_remove_card_button)).check(matches(not(isDisplayed())))
        onView(withId(R.id.singleCard_add_wanted_button)).check(matches(not(isDisplayed())))
        onView(withId(R.id.singleCard_askConnection_text)).check(matches(isDisplayed()))

        Intents.release()
        scenario.close()
        LocalDatabaseProvider.closeDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)
    }
}
