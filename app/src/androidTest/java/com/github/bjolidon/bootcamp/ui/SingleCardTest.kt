package com.github.bjolidon.bootcamp.ui

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.model.*
import com.github.bjolidon.bootcamp.ui.singlecard.SingleCardFragment
import com.github.bjolidon.bootcamp.utils.CustomGlide
import com.github.bjolidon.bootcamp.utils.CustomTypeSafeMatcher.withDrawable
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
        "https://cards.scryfall.io/large/front/c/f/cfa00c0e-163d-4f59-b8b9-3ee9143d27bb.jpg?1674420138",
        MagicRarity.Common,
        MagicType.Artifact,
        listOf("Human", "Soldier"),
    )

    private val validJson = Gson().toJson(validMagicCard)
    private val invalidJson = "This is not a valid json string"

    //ViewMatchers of the fragment
    private val textCardName = onView(withId(R.id.singleCard_text_cardName))
    private val textCardSet = onView(withId(R.id.singleCard_text_cardSet))
    private val textCardNumber = onView(withId(R.id.singleCard_text_cardNumber))
    private val textCardText = onView(withId(R.id.singleCard_text_cardText))
    private val textCardRarity = onView(withId(R.id.singleCard_text_cardRarity))
    private val textCardType = onView(withId(R.id.singleCard_text_cardType))
    private val imageCard = onView(withId(R.id.singleCard_image))

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(CustomGlide.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(CustomGlide.countingIdlingResource)
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
        textCardRarity.check(matches(withText("")))
        textCardType.check(matches(withText("")))
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
        textCardRarity.check(matches(withText("")))
        textCardType.check(matches(withText("")))
        imageCard.check(matches(not(isDisplayed())))
    }

    /**
     * This test is used to check if the fragment is able to display a valid card.
     */
    @Test
    fun testValidJsonInArgumentsIsValid() {

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(validMagicCard.imageUrl)
            .submit()
            .get()

        val bundleArgs = Bundle().apply { putString("card", validJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        val strSubType = if (validMagicCard.subtypes.isNotEmpty()) context.getString(R.string.single_card_showing_type_subtypes, validMagicCard.subtypes.joinToString(", ")) else ""

        textCardName.check(matches(withText(validMagicCard.name)))
        textCardSet.check(matches(withText(context.getString(R.string.single_card_showing_set, validMagicCard.set.name, validMagicCard.set.code))))
        textCardNumber.check(matches(withText(context.getString(R.string.single_card_showing_number, validMagicCard.number))))
        textCardText.check(matches(withText(validMagicCard.text)))
        textCardRarity.check(matches(withText(validMagicCard.rarity.toString())))
        textCardType.check(matches(withText(validMagicCard.type.toString() + strSubType)))
        imageCard.check(matches(isDisplayed()))
        imageCard.check(matches(withDrawable(bitmap)))
    }
}
