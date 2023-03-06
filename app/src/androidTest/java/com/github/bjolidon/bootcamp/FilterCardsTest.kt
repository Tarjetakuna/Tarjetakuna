package com.github.bjolidon.bootcamp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.github.bjolidon.bootcamp.model.MagicSet
import com.google.gson.Gson
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FilterCardsTest {

//    @get:Rule
//    val activityRule = ActivityScenarioRule(FilterCardsActivity::class.java)
    private lateinit var activityRule: ActivityScenario<FilterCardsActivity>;

    private val validName = "Meandering Towershell"
    private val validText = "Islandwalk"
    private val validLayout = MagicLayout.Normal
    private val validCMC = 5
    private val validManaCost = "{3}{G}{G}"
    private val validSet = MagicSet("MT15", "Magic 2015")
    private val validNumber = 141
    private val validImageUrl = "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602"

    @Before
    public fun setUp() {
        val card: MagicCard = MagicCard("Test Card", "Test text",
            MagicLayout.Normal, 7, "{5}{W}{W}",
            MagicSet("MT15", "Magic 2015"), 56,
            "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card")
        val card2: MagicCard = MagicCard(validName, validText,
            validLayout, validCMC, validManaCost,
            validSet, validNumber, validImageUrl)

        val arrayCard: ArrayList<MagicCard> = arrayListOf(card, card2)
        val gson = Gson()
        val arrayCardJson = gson.toJson(arrayCard)
        val intent = Intent(ApplicationProvider.getApplicationContext(), FilterCardsActivity::class.java)
        intent.putExtra("cards", arrayCardJson)
        activityRule = ActivityScenario.launch<FilterCardsActivity>(intent)
    }

    @Test
    fun testContainsTextView() {
        onView(withId(R.id.languageTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun testContainsCardsInIntents() {
        activityRule.onActivity { activity ->
            assert(activity.intent.getStringExtra("cards") != null)
        }
    }

    @Test
    fun testClickOnLanguageShowsLanguageList() {
        onView(withId(R.id.languageTextView)).perform(click())
        onView(withText("Language")).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun testCanClickOnChoices() {
        onView(withId(R.id.languageTextView)).perform(click())
        onView(withText("Language")).inRoot(isDialog()).perform(click())
        onView(withText("Kotlin")).inRoot(isDialog()).perform(click())
        onView(withText("C++")).inRoot(isDialog()).perform(click())
        //button 1 = OK, button 3 = Cancel
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.languageTextView)).check(matches(withText("C++, Kotlin")))
    }

//    @Test
//    fun testValuesUpdatedInValuesMap() {
//        onView(withId(R.id.languageTextView)).perform(click())
//        onView(withText("Language")).inRoot(isDialog()).perform(click())
//        onView(withText("Kotlin")).inRoot(isDialog()).perform(click())
//        onView(withId(android.R.id.button1)).perform(click())
//        activityRule.onActivity { activity ->
//            assert(activity.valuesMap["Language"] == arrayListOf<String>("Kotlin"))
//        }
//    }

    @Test
    fun testValuesAllCleared() {
        onView(withId(R.id.languageTextView)).perform(click())
        onView(withText("Language")).inRoot(isDialog()).perform(click())
        onView(withText("Kotlin")).inRoot(isDialog()).perform(click())
        onView(withText("Java")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.languageTextView)).check(matches(withText("Java, Kotlin")))

        onView(withId(R.id.languageTextView)).perform(click())
        onView(withId(android.R.id.button3)).perform(click())
        onView(withId(R.id.languageTextView)).check(matches(withText("Language")))
    }

    @Test
    fun testCheckBoxUnchecked() {
        onView(withId(R.id.languageTextView)).perform(click())
        onView(withText("Language")).inRoot(isDialog()).perform(click())
        onView(withText("Kotlin")).inRoot(isDialog()).perform(click())
        onView(withText("Kotlin")).inRoot(isDialog()).perform(click())
        onView(withText("Java")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.languageTextView)).check(matches(withText("Java")))
    }

    @Test
    fun testCheckBoxStillCheckedAfterConfirmingAndReopening() {
        onView(withId(R.id.languageTextView)).perform(click())
        onView(withText("Language")).inRoot(isDialog()).perform(click())
        onView(withText("Kotlin")).inRoot(isDialog()).perform(click())
        onView(withText("Java")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.languageTextView)).check(matches(withText("Java, Kotlin")))

        onView(withId(R.id.languageTextView)).perform(click())
        onView(withText("Language")).inRoot(isDialog()).perform(click())
        onView(withText("Kotlin")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.languageTextView)).check(matches(withText("Java")))
    }
}
