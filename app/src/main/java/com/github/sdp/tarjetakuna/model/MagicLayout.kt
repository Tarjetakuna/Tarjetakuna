package com.github.sdp.tarjetakuna.model

/**
 * Represents the layout of a Magic card.
 */
enum class MagicLayout(val apiString: String) {
    NORMAL("normal"),
    SPLIT("split"),
    FLIP("flip"),
    TRANSFORM("transform"),
    MODAL_DFC("modal_dfc"),
    MELD("meld"),
    LEVELER("leveler"),
    CLASS("class"),
    SAGA("saga"),
    ADVENTURE("adventure"),
    BATTLE("battle"),
    PLANAR("planar"),
    SCHEME("scheme"),
    VANGUARD("vanguard"),
    TOKEN("token"),
    DOUBLE_FACED_TOKEN("double_faced_token"),
    EMBLEM("emblem"),
    AUGMENT("augment"),
    HOST("host"),
    ART_SERIES("art_series"),
    REVERSIBLE_CARD("reversible_card");

    companion object {
        fun fromApiString(apiString: String): MagicLayout {
            return values().firstOrNull { it.apiString == apiString } ?: NORMAL
        }
    }
}
