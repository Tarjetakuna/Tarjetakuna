package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import java.util.Date

data class DBMessage(
    val uid: String,
    val sender: String,
    val receiver: String,
    val content: String,
    val timestamp: Date,
) {
    companion object {
        /**
         * Conversion from Message object.
         */
        fun toDBMessage(message: Message): DBMessage {
            return DBMessage(
                message.uid,
                message.sender.uid,
                message.receiver.uid,
                message.content,
                message.timestamp
            )
        }

        /**
         * Conversion to Message object.
         */
        fun fromDBMessage(dbMessage: DBMessage): Message {
            return Message(
                dbMessage.uid,
                User(dbMessage.sender),
                User(dbMessage.receiver),
                dbMessage.content,
                dbMessage.timestamp,
                valid = false
            )
        }
    }
}
