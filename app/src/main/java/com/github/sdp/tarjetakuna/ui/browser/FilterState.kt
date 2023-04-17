package com.github.sdp.tarjetakuna.ui.browser

data class FilterState(
    val filterType: FilterType = FilterType.NAME,
    val filterValue: String = ""
)

enum class FilterType {
    NONE,
    NAME,
    SET
}
