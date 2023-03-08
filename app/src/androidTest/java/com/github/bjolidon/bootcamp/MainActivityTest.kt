package com.github.bjolidon.bootcamp

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.github.bjolidon.bootcamp.model.MagicSet
import com.github.bjolidon.bootcamp.ui.filter.FilterCardsActivity
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    val cards: ArrayList<MagicCard> = arrayListOf(
        MagicCard("Meandering Towershell", "Islandwalk",
            MagicLayout.Normal, 5, "{3}{G}{G}",
            MagicSet("MT15", "Magic 2015"), 141,
            "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602"),
        MagicCard("Angel of Mercy", "Flying",
            MagicLayout.Normal, 5, "{4}{W}",
            MagicSet("MT15", "Magic 2015"), 1,
            "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=82992")
    )

    @get:Rule
    public val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMainActivity() {
        assertEquals(1 + 1, 2)
    }
    @Test
    fun testFilterButtonWorks() {
        Intents.init()
        onView(withId(R.id.filterActivityButton)).perform(click())
        Intents.intended(hasComponent(FilterCardsActivity::class.java.name))
        val gson = Gson()
        Intents.intended(hasExtra("cards", gson.toJson(cards)))
        Intents.release()
    }
}