package com.github.sdp.tarjetakuna.model

import java.util.Date

class Message(
    val id: Int,
    val sender: User,
    val receiver: User,
    val content: String,
    val timestamp: Date
)
