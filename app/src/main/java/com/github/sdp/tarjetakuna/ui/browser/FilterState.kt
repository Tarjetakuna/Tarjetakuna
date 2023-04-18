package com.github.sdp.tarjetakuna.ui.browser

/**
 * Represents the state of the filter.
 */
data class FilterState(
    val filterType: FilterType = FilterType.NONE,
    val filterValue: String = ""
)

/**
 * Represents the type of the filter.
 */
enum class FilterType {
    NONE,
    SET,
    MANA
}
