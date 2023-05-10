package com.github.sdp.tarjetakuna.model

class Message(
    val id: Int,
    val sender: User,
    val receiver: User,
    val content: String,
    val read: Boolean
) {
    init {

    }
}
