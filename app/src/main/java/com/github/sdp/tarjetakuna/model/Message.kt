package com.github.sdp.tarjetakuna.model

import java.util.Date

data class Message(
    /**
     * Message id
     */
    val uid: String,

    /**
     * Sender
     */
    var sender: User,

    /**
     * Receiver
     */
    var receiver: User,

    /**
     * Message content
     */
    var content: String,

    /**
     * Message timestamp
     */
    var timestamp: Date,

    /**
     * If the message is valid (invalid if one of the users is invalid)
     * Used to check if the data is complete before using it (coming from Database, it might not be complete)
     */
    var valid: Boolean = true
) : Cloneable {
    constructor(uid: String) : this(uid, User(""), User(""), "", Date(0), false)

    public override fun clone(): Message {
        return Message(uid, sender.clone(), receiver.clone(), content, timestamp, valid)
    }
}
