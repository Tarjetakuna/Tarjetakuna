package com.github.sdp.tarjetakuna.model

import java.util.Date


data class Chat(
    val id: Int,
    val user1: User,
    val user2: User,
    val messages: ArrayList<Message>,
    val user1LastRead: Date,
    val user2LastRead: Date,
    var timestamp: Date = Date(0)
) {
    init {
        messages.sortBy { it.timestamp }
        timestamp = messages.lastOrNull()?.timestamp ?: Date(0)
    }
}
