package com.github.sdp.tarjetakuna.model

import java.util.Date


data class Chat(
    /**
     * Chat id
     */
    val uid: String,

    /**
     * One of the users
     */
    var user1: User,

    /**
     * The other user
     */
    var user2: User,

    /**
     * List of messages
     */
    var messages: ArrayList<Message>,

    /**
     * Last read timestamp by user 1
     */
    var user1LastRead: Date,

    /**
     * Last read timestamp by user 2
     */
    var user2LastRead: Date,

    /**
     * If the chat is valid (invalid if messages or one of the users is invalid)
     * Used to check if the data is complete before using it (coming from Database, it might not be complete)
     */
    var valid: Boolean = true,

    /**
     * Last message timestamp
     */
    var timestamp: Date = Date()
) : Cloneable {

    constructor(uid: String) :
            this(uid, User(""), User(""), ArrayList(), Date(), Date(), false)

    init {
        messages.sortBy { it.timestamp }
        timestamp = messages.lastOrNull()?.timestamp ?: Date()
    }

    public override fun clone(): Chat {
        return Chat(
            uid,
            user1.clone(),
            user2.clone(),
            messages.clone() as ArrayList<Message>,
            user1LastRead,
            user2LastRead,
            valid,
            timestamp
        )
    }
}
