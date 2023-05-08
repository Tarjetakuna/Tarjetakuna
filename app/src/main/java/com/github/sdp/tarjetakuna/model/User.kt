package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.database.DBMagicCard

/**
 * Represents a user.
 */
data class User(
    val username: String,
    val cards: List<DBMagicCard>,
    val location: Coordinates
    //val chats: List<Chat>

) {
    init {
        require(
            username.matches(
                Regex(
                    "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*\$"
                )
            )
        ) { "Email is not valid" }
    }
}
