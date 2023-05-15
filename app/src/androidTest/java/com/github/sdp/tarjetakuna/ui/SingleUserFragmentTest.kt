package com.github.sdp.tarjetakuna.ui

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.mockdata.CommonUser
import com.github.sdp.tarjetakuna.model.*
import com.github.sdp.tarjetakuna.ui.singleuser.SingleUserFragment
import com.google.gson.Gson
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class is used to test the SingleSetFragment.
 */
@RunWith(AndroidJUnit4::class)
class SingleUserFragmentTest {

    private lateinit var scenario: FragmentScenario<SingleUserFragment>
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val validUser = CommonUser.testUser

    private val validJson = Gson().toJson(validUser, User::class.java)
    private val invalidJson = "This is not a valid json string"

    //ViewMatchers of the fragment
    private val textUserUsername = onView(withId(R.id.singleUser_user_name_text))
    private val textUserEmail = onView(withId(R.id.singleUser_user_email_text))
    private val textNumberWantedCards = onView(withId(R.id.singleUser_user_number_wanted_cards))
    private val textNumberOwnedCards = onView(withId(R.id.singleUser_user_number_owned_cards))

    @After
    fun tearDown() {
        scenario.close()
    }

    /**
     * This test is used to check if the fragment is able to manage
     * the case where no arguments are passed to the fragment.
     */
    @Test
    fun testNoJsonInArgumentsIsInvalid() {
        scenario = launchFragmentInContainer()

        textUserUsername.check(matches(withText(R.string.single_user_error_loading)))
        textUserEmail.check(matches(withText("")))
        textNumberWantedCards.check(matches(withText("")))
        textNumberOwnedCards.check(matches(withText("")))
    }

    /**
     * This test is used to check if the fragment is able to manage
     * the case where invalid arguments are passed to the fragment.
     * For example : a json string that is not a User.
     */
    @Test
    fun testInvalidJsonInArgumentsIsInvalid() {
        val bundleArgs = Bundle().apply { putString(SingleUserFragment.ARG_USER, invalidJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textUserUsername.check(matches(withText(R.string.single_user_error_loading)))
        textUserEmail.check(matches(withText("")))
        textNumberWantedCards.check(matches(withText("")))
        textNumberOwnedCards.check(matches(withText("")))
    }

    /**
     * This test is used to check if the fragment is able to display a valid set.
     */
    @Test
    fun testValidJsonInArgumentsIsValid() {
        val bundleArgs = Bundle().apply { putString(SingleUserFragment.ARG_USER, validJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textUserUsername.check(matches(withText(validUser.username)))
        textUserEmail.check(
            matches(
                withText(
                    validUser.email
                )
            )
        )
        textNumberOwnedCards.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_user_owned_cards_number,
                        validUser.cards.filter { it.possession == CardPossession.OWNED }.size
                    )
                )
            )
        )
        textNumberWantedCards.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_user_wanted_cards_number,
                        validUser.cards.filter { it.possession == CardPossession.WANTED }.size
                    )
                )
            )
        )
    }
}
