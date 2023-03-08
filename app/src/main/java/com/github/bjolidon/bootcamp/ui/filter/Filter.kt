package com.github.bjolidon.bootcamp.ui.filter

import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout

/*
    * Class to represent a filter.
 */
data class Filter(val name: String, val layout: ArrayList<MagicLayout>, val convertedManaCost: ArrayList<Int>) {

    fun doesContain(other: MagicCard): Boolean {
        var isInFilter: Boolean = true
        if (name != "") {
            isInFilter = isInFilter && (other.name == name)
        }
        if (layout.isNotEmpty()) {
            isInFilter = isInFilter && (other.layout in layout)
        }
        if (convertedManaCost.isNotEmpty()) {
            isInFilter = isInFilter && (other.convertedManaCost in convertedManaCost)
        }
        return isInFilter
    }
}
