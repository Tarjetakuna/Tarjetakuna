package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import java.util.Date

data class DBChat(
    /**
     * Chat id.
     */
    val uid: String,

    /**
     * User 1 id.
     */
    val user1: String,

    /**
     * User 2 id.
     */
    val user2: String,

    /**
     * List of messages ids.
     */
    val messages: MutableList<String>,

    /**
     * Last read timestamp by user 1.
     */
    var user1LastRead: Date,

    /**
     * Last read timestamp by user 2.
     */
    var user2LastRead: Date,
) : Cloneable {
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
         * Chat will be invalid as we are only having the ids of the messages and users.
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

        fun newChat(user1: String, user2: String): DBChat {
            val dbChat = DBChat("", user1, user2, ArrayList(), Date(), Date())
            val uid = dbChat.hashCode().toUInt().toString()
            return dbChat.copy(uid = uid)
        }
    }

    public override fun clone(): DBChat {
        return DBChat(
            uid,
            user1,
            user2,
            messages.map { it } as ArrayList<String>,
            user1LastRead,
            user2LastRead,
        )
    }

}
