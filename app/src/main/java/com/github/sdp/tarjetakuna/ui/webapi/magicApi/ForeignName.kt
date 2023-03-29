package com.github.sdp.tarjetakuna.ui.webapi.magicApi

data class ForeignName(
    val name: String,
    val text: String,
    val type: String,
    val flavor: String,
    val imageUrl: String,
    val language: String,
    val multiverseid: Int
)
