package com.github.sdp.tarjetakuna.model

import java.util.Date

data class Message(
    val uid: String,
    var sender: User,
    var receiver: User,
    var content: String,
    var timestamp: Date,
    var valid: Boolean = true
) {
    constructor(uid: String) : this(uid, User(""), User(""), "", Date(0), false)
}
