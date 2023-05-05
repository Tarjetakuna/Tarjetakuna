package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.database.DBMagicCard

/**
 * Represents a user.
 */
data class User(
    val email: String,
    val username: String,
    val cards: List<DBMagicCard>,
    val location: Coordinates
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(
            email.matches(
                Regex(
                    "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*\$"
                )
            )
        ) { "Email is not valid" }
        require(username.isNotBlank()) { "username cannot be blank" }
    }
}
