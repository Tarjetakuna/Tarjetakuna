package com.github.bjolidon.bootcamp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FilterCardsTest {

    @get:Rule
    public val activityRule = ActivityScenarioRule(FilterCardsActivity::class.java)
    @Test
    fun testContainsTextView() {
        onView(withId(R.id.languageTextView)).check(matches(isDisplayed()))
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

    @Test
    fun testValuesUpdatedInValuesMap() {
        onView(withId(R.id.languageTextView)).perform(click())
        onView(withText("Language")).inRoot(isDialog()).perform(click())
        onView(withText("Kotlin")).inRoot(isDialog()).perform(click())
        onView(withId(android.R.id.button1)).perform(click())
        activityRule.scenario.onActivity { activity ->
            assert(activity.valuesMap["Language"] == arrayListOf<String>("Kotlin"))
        }
    }

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
