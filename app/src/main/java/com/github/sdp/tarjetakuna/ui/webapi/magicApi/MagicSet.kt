package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.github.sdp.tarjetakuna.utils.Utils

data class MagicSet(
    val code: String,
    val name: String,
    val type: String,
    val border: String?,
    val mkm_id: String?,
    val mkm_name: String?,
    val gathererCode: String,
    val magicCardsInfoCode: String,
    val releaseDate: String,
    val block: String,
    val onlineOnly: Boolean,
//    val booster: List<String>,
    val mkm_idExpansion: String,
    val mkm_nameExpansion: String
) {
    override fun toString(): String {
        return "DataSet(" +
                "code='$code', " +
                "name='$name', " +
                "type='$type', " +
                Utils.printIfNotNullOrEmpty(border, "border=") +
                Utils.printIfNotNullOrEmpty(mkm_id, "mkm_id=") +
                Utils.printIfNotNullOrEmpty(mkm_name, "mkm_name=") +
                Utils.printIfNotNullOrEmpty(gathererCode, "gathererCode=") +
                Utils.printIfNotNullOrEmpty(magicCardsInfoCode, "magicCardsInfoCode=") +
                Utils.printIfNotNullOrEmpty(releaseDate, "releaseDate=") +
                Utils.printIfNotNullOrEmpty(block, "block=") +
                Utils.printIfNotNullOrEmpty(onlineOnly, "onlineOnly=") +
//                "booster=$booster, " +
                Utils.printIfNotNullOrEmpty(mkm_idExpansion, "mkm_idExpansion=") +
                Utils.printIfNotNullOrEmpty(mkm_nameExpansion, "mkm_nameExpansion=") +
                ")"
    }
}
