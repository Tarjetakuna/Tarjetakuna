package com.github.sdp.tarjetakuna.model

import java.util.Date


data class Chat(
    val uid: String,
    var user1: User,
    var user2: User,
    var messages: ArrayList<Message>,
    var user1LastRead: Date,
    var user2LastRead: Date,
    var valid: Boolean = true,
    var timestamp: Date = Date(0)
) {

    constructor(uid: String) :
            this(uid, User(""), User(""), ArrayList(), Date(0), Date(0), false)

    init {
        messages.sortBy { it.timestamp }
        timestamp = messages.lastOrNull()?.timestamp ?: Date(0)
    }
}
