package com.github.bjolidon.bootcamp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.bjolidon.bootcamp.ui.filter.Filter
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.github.bjolidon.bootcamp.model.MagicSet
import com.github.bjolidon.bootcamp.ui.filter.FilterCardsActivity
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FilterCardsTest {

    private lateinit var activityRule: ActivityScenario<FilterCardsActivity>
    private val card1 = MagicCard("Angel of Mercy", "Flying",
        MagicLayout.Normal, 7, "{5}{W}{W}",
        MagicSet("MT15", "Magic 2015"), 56,
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card")

    private val card2 = MagicCard("Meandering Towershell", "Islandwalk",
        MagicLayout.DoubleFaced, 5, "{3}{G}{G}",
        MagicSet("MT15", "Magic 2015"), 141,
        "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602")

    private val arrayCard = arrayListOf(card1, card2)


    @Before
    public fun setUp() {
        val gson = Gson()
        val arrayCardJson = gson.toJson(arrayCard)
        val intent = Intent(ApplicationProvider.getApplicationContext(), FilterCardsActivity::class.java)
        intent.putExtra("cards", arrayCardJson)
        activityRule = ActivityScenario.launch(intent)
    }
    @Test
    fun testContainsTextView() {
        onView(withId(R.id.layoutTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun testContainsCardsInIntents() {
        activityRule.onActivity { activity ->
            assert(activity.intent.getStringExtra("cards") != null)
        }
    }

    @Test
    fun testClickOnLayoutShowsLayoutList() {
        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withText("Layout")).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun testCanClickOnChoices() {
        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withText("Layout")).inRoot(isDialog()).perform(click())
        onView(withText("Normal")).inRoot(isDialog()).perform(click())
        onView(withText("Phenomenon")).inRoot(isDialog()).perform(click())
        onView(withText("Token")).inRoot(isDialog()).perform(click())
        //button 1 = OK, button 3 = Cancel
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.layoutTextView)).check(matches(withText("Normal, Token, Phenomenon")))
    }

    @Test
    fun testValuesAllCleared() {
        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withText("Layout")).inRoot(isDialog()).perform(click())
        onView(withText("Normal")).inRoot(isDialog()).perform(click())
        onView(withText("Phenomenon")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.layoutTextView)).check(matches(withText("Normal, Phenomenon")))

        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withId(android.R.id.button3)).perform(click())
        onView(withId(R.id.layoutTextView)).check(matches(withText("")))
    }

    @Test
    fun testCheckBoxUnchecked() {
        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withText("Layout")).inRoot(isDialog()).perform(click())
        onView(withText("Normal")).inRoot(isDialog()).perform(click())
        onView(withText("Normal")).inRoot(isDialog()).perform(click())
        onView(withText("Leveler")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.layoutTextView)).check(matches(withText("Leveler")))
    }

    @Test
    fun testCheckBoxStillCheckedAfterConfirmingAndReopening() {
        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withText("Layout")).inRoot(isDialog()).perform(click())
        onView(withText("Normal")).inRoot(isDialog()).perform(click())
        onView(withText("Leveler")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.layoutTextView)).check(matches(withText("Normal, Leveler")))

        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withText("Layout")).inRoot(isDialog()).perform(click())
        onView(withText("Normal")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.layoutTextView)).check(matches(withText("Leveler")))
    }

    @Test
    fun noChoiceSelectedMultipleChoiceWorks() {
        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.layoutTextView)).check(matches(withText("")))
    }

    @Test
    fun testSingleChoiceWorks() {
        onView(withId(R.id.cardNameTextView)).perform(click())
        onView(withText("Angel of Mercy")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.cardNameTextView)).check(matches(withText("Angel of Mercy")))
    }

    @Test
    fun testNoSelectedChoiceWorks() {
        onView(withId(R.id.cardNameTextView)).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.cardNameTextView)).check(matches(withText("")))
    }
    @Test
    fun testOnlySingleChoicePossible() {
        onView(withId(R.id.cardNameTextView)).perform(click())
        onView(withText("Angel of Mercy")).inRoot(isDialog()).perform(click())
        onView(withText("Angel of Serenity")).inRoot(isDialog()).perform(click())

        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.cardNameTextView)).check(matches(withText("Angel of Serenity")))
    }

    @Test
    fun testSingleChoiceCleared() {
        onView(withId(R.id.cardNameTextView)).perform(click())
        onView(withText("Angel of Mercy")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button3)).perform(click())
        onView(withId(R.id.cardNameTextView)).check(matches(withText("")))
    }

    @Test
    fun testSingleChoiceStays() {
        onView(withId(R.id.cardNameTextView)).perform(click())
        onView(withText("Angel of Mercy")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.cardNameTextView)).check(matches(withText("Angel of Mercy")))

        onView(withId(R.id.cardNameTextView)).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.cardNameTextView)).check(matches(withText("Angel of Mercy")))
    }

    @Test
    fun testFilterButtonWithEmptyFilterWorks() {
        onView(withId(R.id.filterButton)).perform(click())
        onView(withText(arrayCard.toString())).inRoot(isDialog()).check(matches(withText(arrayCard.toString())))

    }

    @Test
    fun testFilterButtonWithEmptyName() {
        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withText("Layout")).inRoot(isDialog()).perform(click())
        onView(withText("DoubleFaced")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.filterButton)).perform(click())
        onView(withText("[$card2]")).inRoot(isDialog()).check(matches(withText("[$card2]")))

    }
    @Test
    fun testFilterButtonWorks() {
        onView(withId(R.id.cardNameTextView)).perform(click())
        onView(withText("Angel of Mercy")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.layoutTextView)).perform(click())
        onView(withText("Layout")).inRoot(isDialog()).perform(click())
        onView(withText("Normal")).inRoot(isDialog()).perform(click())
        onView(withText("Leveler")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.filterButton)).perform(click())
        onView(withText("[$card1]")).inRoot(isDialog()).check(matches(withText("[$card1]")))
    }

     @Test
     fun testFilterOnlyWithCMC() {
         onView(withId(R.id.cmcTextView)).perform(click())
         onView(withText("5")).perform(click())
         onView(withText("7")).perform(click())
         onView(withId(android.R.id.button1)).perform(click())

         onView(withId(R.id.filterButton)).perform(click())
         onView(withText(arrayCard.toString())).inRoot(isDialog()).check(matches(withText(arrayCard.toString())))
     }
}
