package com.github.sdp.tarjetakuna.database

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
                chat.user1,
                chat.user2,
                chat.messages.map { it.uid },
                chat.user1.lastRead,
                chat.user2.lastRead
            )
        }

        /**
         * Conversion to Chat object.
         */
        fun fromDBChat(dbChat: DBChat): Chat {
            return Chat(
                dbChat.uid,
                dbChat.user1,
                dbChat.user2,
                dbChat.messages.map { Message(it.uid) },
                dbChat.user1.lastRead,
                dbChat.user2.lastRead
            )
        }
    }

}
