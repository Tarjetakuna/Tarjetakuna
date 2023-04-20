package com.github.sdp.tarjetakuna.ui.webapi.magicApi

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
)
