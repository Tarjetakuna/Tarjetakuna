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
import com.github.sdp.tarjetakuna.mockdata.CommonMagicSet
import com.github.sdp.tarjetakuna.model.*
import com.github.sdp.tarjetakuna.ui.singleset.SingleSetFragment
import com.github.sdp.tarjetakuna.utils.Utils
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This class is used to test the SingleSetFragment.
 */
@RunWith(AndroidJUnit4::class)
class SingleSetFragmentTest {

    private lateinit var scenario: FragmentScenario<SingleSetFragment>
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val validMagicSet = CommonMagicSet.magic2015Set

    private val validJson = Gson().toJson(validMagicSet, MagicSet::class.java)
    private val invalidJson = "This is not a valid json string"

    //ViewMatchers of the fragment
    private val textSetName = onView(withId(R.id.singleSet_set_name_text))
    private val textSetCode = onView(withId(R.id.singleSet_set_code_text))
    private val textSetType = onView(withId(R.id.singleSet_set_type_text))
    private val textSetBlock = onView(withId(R.id.singleSet_set_block_text))
    private val textSetReleaseDate = onView(withId(R.id.singleSet_set_release_date_text))

    @Before
    fun setUp() {
        Utils.useFirebaseEmulator()
    }

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

        textSetName.check(matches(withText(R.string.single_set_error_loading)))
        textSetCode.check(matches(withText("")))
        textSetType.check(matches(withText("")))
        textSetBlock.check(matches(withText("")))
        textSetReleaseDate.check(matches(withText("")))
    }

    /**
     * This test is used to check if the fragment is able to manage
     * the case where invalid arguments are passed to the fragment.
     * For example : a json string that is not a MagicSet.
     */
    @Test
    fun testInvalidJsonInArgumentsIsInvalid() {
        val bundleArgs = Bundle().apply { putString("card", invalidJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textSetName.check(matches(withText(R.string.single_set_error_loading)))
        textSetCode.check(matches(withText("")))
        textSetType.check(matches(withText("")))
        textSetBlock.check(matches(withText("")))
        textSetReleaseDate.check(matches(withText("")))
    }

    /**
     * This test is used to check if the fragment is able to display a valid set.
     */
    @Test
    fun testValidJsonInArgumentsIsValid() {
        val bundleArgs = Bundle().apply { putString("set", validJson) }
        scenario = launchFragmentInContainer(fragmentArgs = bundleArgs)

        textSetName.check(matches(withText(validMagicSet.name)))
        textSetCode.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_set_code,
                        validMagicSet.code
                    )
                )
            )
        )
        textSetType.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_set_type,
                        validMagicSet.type
                    )
                )
            )
        )
        textSetBlock.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_set_block,
                        validMagicSet.block
                    )
                )
            )
        )
        textSetReleaseDate.check(
            matches(
                withText(
                    context.getString(
                        R.string.single_set_release_date,
                        validMagicSet.releaseDate
                    )
                )
            )
        )
    }
}
