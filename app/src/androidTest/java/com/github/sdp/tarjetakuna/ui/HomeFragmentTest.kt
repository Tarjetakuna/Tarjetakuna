package com.github.sdp.tarjetakuna

import androidx.test.espresso.Espresso.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    val cards: ArrayList<MagicCard> = arrayListOf(
        MagicCard(
            "Meandering Towershell", "Islandwalk",
            MagicLayout.Normal, 5, "{3}{G}{G}",
            MagicSet("MT15", "Magic 2015"), 141,
            "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602"
        ),
        MagicCard(
            "Angel of Mercy", "Flying",
            MagicLayout.Normal, 5, "{4}{W}",
            MagicSet("MT15", "Magic 2015"), 1,
            "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=82992"
        )
    )

    @get:Rule
    public val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun emptyTest() {
        
    }

}
