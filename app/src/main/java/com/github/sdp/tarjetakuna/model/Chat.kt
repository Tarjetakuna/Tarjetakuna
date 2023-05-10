package com.github.sdp.tarjetakuna.model


data class Chat(
    val id: Int,
    val user1: User,
    val user2: User,
    val messages: List<Message>
) {
    init {

    }
}
