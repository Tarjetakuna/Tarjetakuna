package com.github.sdp.tarjetakuna.ui.webapi.magicApi

import com.google.gson.Gson

/**
 * Foreign name of a MagicCard (need to match the doc in api.magicthegathering.io/v1/)
 */
data class ForeignName(
    val name: String,
    val text: String,
    val type: String,
    val flavor: String?,
    val imageUrl: String,
    val language: String,
    val multiverseid: Int
) {
    /**
     * Convert to Json
     */
    fun toJson() {
        Gson().toJson(this)
    }

    companion object {
        /**
         * Convert from Json
         */
        fun fromJsonToList(json: String): List<ForeignName> {
            return Gson().fromJson(json, Array<ForeignName>::class.java).toList()
        }
    }
}
