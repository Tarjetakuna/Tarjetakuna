package com.github.bjolidon.bootcamp.ui

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.github.bjolidon.bootcamp.model.MagicSet
import com.github.bjolidon.bootcamp.ui.singlecard.SingleCardFragment
import com.google.gson.Gson
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class is used to test the SingleCardFragment.
 */
@RunWith(AndroidJUnit4::class)
class SingleCardTest {

    private lateinit var scenario: FragmentScenario<SingleCardFragment>
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val validMagicCard = MagicCard(
        "Name",
        "Text",
        MagicLayout.Normal,
        1,
        "{1}",
        MagicSet("TS", "TestSet"),
        1,
        "https://cards.scryfall.io/large/front/c/f/cfa00c0e-163d-4f59-b8b9-3ee9143d27bb.jpg?1674420138")

    private val validJson = Gson().toJson(validMagicCard)
    private val invalidJson = "This is not a valid json string"

    //ViewMatchers of the fragment
    private val textCardName = onView(withId(R.id.singleCard_text_cardName))
    private val textCardSet = onView(withId(R.id.singleCard_text_cardSet))
    private val textCardNumber = onView(withId(R.id.singleCard_text_cardNumber))
    private val textCardText = onView(withId(R.id.singleCard_text_cardText))
    private val imageCard = onView(withId(R.id.singleCard_image))

    @Before
    fun setUp() {

    }

    @After
    fun after() {
        scenario.close()
    }

    /**
     * This test is used to check if the fragment is able to manage
     * the case where no arguments are passed to the fragment.
     */
    @Test
    fun testNoJsonInArgumentsIsInvalid() {
        scenario = launchFragmentInContainer()

        textCardName.check(matches(withText(R.string.error_load_card)))
        textCardSet.check(matches(withText("")))
        textCardNumber.check(matches(withText("")))
        textCardText.check(matches(withText("")))
        imageCard.check(matches(not(isDisplayed())))
    }

    /**
     * This test is used to check if the fragment is able to manage
     * the case where invalid arguments are passed to the fragment.
     * For example : a json string that is not a MagicCard.
     */
    @Test
    fun testInvalidJsonInArgumentsIsInvalid() {
        val bundleArgs = Bundle().apply { putString("card", invalidJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textCardName.check(matches(withText(R.string.error_load_card)))
        textCardSet.check(matches(withText("")))
        textCardNumber.check(matches(withText("")))
        textCardText.check(matches(withText("")))
        imageCard.check(matches(not(isDisplayed())))
    }

    /**
     * This test is used to check if the fragment is able to display a valid card.
     */
    @Test
    fun testValidJsonInArgumentsIsValid() {
        val bundleArgs = Bundle().apply { putString("card", validJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textCardName.check(matches(withText(validMagicCard.name)))
        textCardSet.check(matches(withText(context.getString(R.string.single_card_showing_set, validMagicCard.set.name, validMagicCard.set.code))))
        textCardNumber.check(matches(withText(context.getString(R.string.single_card_showing_number, validMagicCard.number))))
        textCardText.check(matches(withText(validMagicCard.text)))
        //TODO: check if the image is the same as the one in the card
        //imageCard.check(matches(isDisplayed()))
    }
}
