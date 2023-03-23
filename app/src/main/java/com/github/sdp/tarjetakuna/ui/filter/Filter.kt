package com.github.sdp.tarjetakuna.ui.filter

import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout

/**
 * Class to represent a filter.
 */
class Filter(val name: String, val layout: ArrayList<MagicLayout>, val convertedManaCost: ArrayList<Int>) {

    fun doesContain(other: MagicCard): Boolean {
        var isInFilter: Boolean = true
        if (name.isNotBlank()) {
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
