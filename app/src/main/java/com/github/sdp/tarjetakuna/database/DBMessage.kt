package com.github.sdp.tarjetakuna.database

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
         * Conversion from Chat object.
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
         * Conversion to Chat object.
         */
        fun fromDBMessage(dbMessage: DBMessage): Message {
            return Message(
                dbMessage.uid,
                dbMessage.sender,
                dbMessage.receiver,
                dbMessage.content,
                dbMessage.timestamp
            )
        }
    }
}
