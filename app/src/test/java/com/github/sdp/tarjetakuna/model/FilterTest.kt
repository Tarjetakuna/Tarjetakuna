package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.ui.filter.Filter
import org.junit.Test

class FilterTest {

    private val card1 = MagicCard(
        "Angel of Mercy", "Flying",
        MagicLayout.Normal, 7, "{5}{W}{W}",
        MagicSet("MT15", "Magic 2015"), 56,
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
    )

    @Test
    fun filterInitializedCorrectly() {
        val filter = Filter("", arrayListOf(), arrayListOf())
        assert(filter.name == "")
        assert(filter.convertedManaCost.isEmpty())
        assert(filter.layout.isEmpty())
    }

    @Test
    fun filterInitializedCorrectly2() {
        val magicLayoutArray =
            arrayListOf(MagicLayout.Normal, MagicLayout.Leveler, MagicLayout.Aftermath)
        val cmcArray = arrayListOf(1, 5, 7, 9)
        val filter = Filter("Angel of Mercy", magicLayoutArray, cmcArray)
        assert(filter.name == "Angel of Mercy")
        assert(filter.convertedManaCost.isNotEmpty())
        assert(filter.convertedManaCost == cmcArray)
        assert(filter.layout.isNotEmpty())
        assert(filter.layout == magicLayoutArray)
    }

    @Test
    fun filterDoesContainWorksCorrectly() {
        val magicLayoutArray =
            arrayListOf(MagicLayout.Normal, MagicLayout.Leveler, MagicLayout.Aftermath)
        val cmcArray = arrayListOf(1, 5, 7, 9)
        val filter = Filter("Angel of Mercy", magicLayoutArray, cmcArray)
        assert(filter.doesContain(card1))
    }
}
