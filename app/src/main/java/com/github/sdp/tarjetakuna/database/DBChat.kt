package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import java.util.Date

data class DBChat(
    val uid: String,
    val user1: String,
    val user2: String,
    val messages: ArrayList<String>,
    val user1LastRead: Date,
    val user2LastRead: Date,
) {
    companion object {

        /**
         * Conversion from Chat object.
         */
        fun toDBChat(chat: Chat): DBChat {
            return DBChat(
                chat.uid,
                chat.user1.uid,
                chat.user2.uid,
                chat.messages.map { it.uid } as ArrayList<String>,
                chat.user1LastRead,
                chat.user2LastRead
            )
        }

        /**
         * Conversion to Chat object.
         */
        fun fromDBChat(dbChat: DBChat): Chat {
            return Chat(
                dbChat.uid,
                User(dbChat.user1),
                User(dbChat.user2),
                dbChat.messages.map { Message(it) } as ArrayList<Message>,
                dbChat.user1LastRead,
                dbChat.user2LastRead,
                valid = false
            )
        }
    }

}
