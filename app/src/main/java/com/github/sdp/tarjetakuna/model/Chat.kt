package com.github.sdp.tarjetakuna.model


data class Chat(
    val id: Int,
    val user1: User,
    val user2: User,
    val messages: ArrayList<Message>
) {
    init {

    }
}
