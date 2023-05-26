package com.github.sdp.tarjetakuna.ui

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.DatabaseSync
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.mockdata.CommonFirebase
import com.github.sdp.tarjetakuna.mockdata.CommonMagicCard
import com.github.sdp.tarjetakuna.model.*
import com.github.sdp.tarjetakuna.ui.singlecard.SingleCardFragment
import com.github.sdp.tarjetakuna.utils.CustomGlide
import com.github.sdp.tarjetakuna.utils.Utils
import com.github.sdp.tarjetakuna.utils.WithDrawableSafeMatcher
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
class SingleCardFragmentTest {

    private lateinit var scenario: FragmentScenario<SingleCardFragment>
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val validMagicCard = CommonMagicCard.aeronautTinkererCard

    private val validJson = Gson().toJson(validMagicCard)
    private val invalidJson = "This is not a valid json string"

    //ViewMatchers of the fragment
    private val textCardName = onView(withId(R.id.singleCard_card_name_text))
    private val textCardSet = onView(withId(R.id.singleCard_set_text))
    private val textCardNumber = onView(withId(R.id.singleCard_card_number_text))
    private val textCardText = onView(withId(R.id.singleCard_card_text_text))
    private val textCardRarity = onView(withId(R.id.singleCard_rarity_text))
    private val textCardType = onView(withId(R.id.singleCard_type_subtype_stats_text))
    private val imageCard = onView(withId(R.id.singleCard_image))
    private val textCardArtist = onView(withId(R.id.singleCard_artist_text))
    private val textCardManaCost = onView(withId(R.id.singleCard_mana_cost_text))

    @Before
    fun setup() {
        Utils.useFirebaseEmulator()
        DatabaseSync.activateSync = false
        FirebaseDB().returnDatabaseReference().updateChildren(CommonFirebase.goodFirebase)
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
        textCardArtist.check(matches(withText("")))
        textCardManaCost.check(matches(withText("")))
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
        textCardArtist.check(matches(withText("")))
        textCardManaCost.check(matches(withText("")))
        imageCard.check(matches(not(isDisplayed())))
    }

    /**
     * This test is used to check if the fragment is able to display a valid card.
     */
    @Test
    fun testValidJsonInArgumentsIsValid() {
        //TODO : Get the bitmap from a mockWebserver integration
        val bitmap = Glide.with(context)
            .asBitmap()
            .load(validMagicCard.imageUrl)
            .submit()
            .get()

        val bundleArgs = Bundle().apply { putString("card", validJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        val strStatsCreature =
            if (validMagicCard.type == MagicCardType.CREATURE) " " + context.getString(
                R.string.single_card_showing_stats,
                validMagicCard.power,
                validMagicCard.toughness
            ) else ""
        val strSubType = if (validMagicCard.subtypes.isNotEmpty()) " " + context.getString(
            R.string.single_card_showing_subtypes,
            validMagicCard.subtypes.joinToString(", ")
        ) else ""

        textCardName.check(matches(withText(validMagicCard.name)))
        textCardSet.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_card_showing_set,
                        validMagicCard.set.name,
                        validMagicCard.set.code
                    )
                )
            )
        )
        textCardNumber.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_card_showing_number,
                        validMagicCard.number
                    )
                )
            )
        )
        textCardText.check(matches(withText(validMagicCard.text)))
        textCardRarity.check(matches(withText(validMagicCard.rarity.toString())))
        textCardType.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_card_showing_type_subtypes_stats,
                        validMagicCard.type.toString(),
                        strStatsCreature,
                        strSubType
                    )
                )
            )
        )
        textCardArtist.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_card_showing_artist,
                        validMagicCard.artist
                    )
                )
            )
        )
        textCardManaCost.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_card_showing_mana_cost,
                        validMagicCard.convertedManaCost
                    )
                )
            )
        )
        imageCard.check(matches(isDisplayed()))
        imageCard.check(matches(WithDrawableSafeMatcher.withDrawable(bitmap)))
    }

    /**
     * This test is used to check if the fragment is able to display the type text correctly
     * for a creature with no subtypes.
     */
    @Test
    fun testTypeTextWorkCorrectlyWithCreatureNoSubtype() {
        val anotherValidMagicCard =
            validMagicCard.copy(subtypes = listOf())
        val anotherValidJson = Gson().toJson(anotherValidMagicCard)
        val bundleArgs = Bundle().apply { putString("card", anotherValidJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textCardType.check(
            matches(
                withText(
                    anotherValidMagicCard.type.toString() + " " + context.getString(
                        R.string.single_card_showing_stats,
                        validMagicCard.power,
                        validMagicCard.toughness
                    )
                )
            )
        )
    }

    /**
     * This test is used to check if the fragment is able to display the type text correctly
     * for an artifact with no subtypes.
     */
    @Test
    fun testTypeTextWorkCorrectlyWithArtifactNoSubtype() {
        val anotherValidMagicCard =
            validMagicCard.copy(type = MagicCardType.ARTIFACT, subtypes = listOf())
        val anotherValidJson = Gson().toJson(anotherValidMagicCard)
        val bundleArgs = Bundle().apply { putString("card", anotherValidJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textCardType.check(matches(withText(anotherValidMagicCard.type.toString())))
    }

    /**
     * This test is used to check if the fragment is able to display the type text correctly
     * for a creature with subtypes.
     */
    @Test
    fun testTypeTextWorkCorrectlyWithCreatureWithSubtype() {
        val anotherValidJson = Gson().toJson(validMagicCard)
        val bundleArgs = Bundle().apply { putString("card", anotherValidJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textCardType.check(
            matches(
                withText(
                    validMagicCard.type.toString()
                            + " " + context.getString(
                        R.string.single_card_showing_stats,
                        validMagicCard.power,
                        validMagicCard.toughness
                    )
                            + " " + context.getString(
                        R.string.single_card_showing_subtypes,
                        validMagicCard.subtypes.joinToString(", ")
                    )
                )
            )
        )
    }

    //TODO: fix this test
    @Test
    fun testUserCanSeeUsersThatHaveTheCard() {
        val bundleArgs = Bundle().apply { putString("card", validJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)
        onView(withId(R.id.singleCard_scrollView)).perform(swipeUp())

        onView(withText(R.string.single_card_users_have)).check(matches(isDisplayed()))
        onView(withText(R.string.single_card_users_want)).check(matches(isDisplayed()))
        onView(withText(R.string.single_card_users_have)).perform(click())

        /*
        onView(withIndex(withText(CommonFirebase.GoodFirebaseAttributes.email1), 0)).check(
            matches(
                isDisplayed()
            )
        )
        onView(withIndex(withId(R.id.user_adapter_km_text), 0)).check(matches(isDisplayed()))
        onView(withIndex(withId(R.id.user_adapter_message_button), 0)).check(matches(isDisplayed()))
        */
    }

    //TODO: fix this test
    @Test
    fun testUserCanSeeUsersThatWantTheCard() {
        val bundleArgs = Bundle().apply { putString("card", validJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)
        onView(withId(R.id.singleCard_scrollView)).perform(swipeUp())

        onView(withText(R.string.single_card_users_have)).check(matches(isDisplayed()))
        onView(withText(R.string.single_card_users_want)).check(matches(isDisplayed()))

        //onView(withText(R.string.single_card_users_want)).perform(click())
        //onView(withText(CommonFirebase.GoodFirebaseAttributes.email1)).check(matches(isDisplayed()))
        //onView(withIndex(withId(R.id.user_adapter_km_text), 0)).check(matches(isDisplayed()))
        //onView(withIndex(withId(R.id.user_adapter_message_button), 0)).check(matches(isDisplayed()))
    }
}
