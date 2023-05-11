package com.github.sdp.tarjetakuna.model

import java.util.*


data class Chat(
    val id: Int,
    val user1: User,
    val user2: User,
    val messages: ArrayList<Message>,
    var timestamp: Date = Date(0)
) {
    init {
        messages.sortBy { it.timestamp }
        timestamp = messages.firstOrNull()?.timestamp ?: Date(0)
    }
}
