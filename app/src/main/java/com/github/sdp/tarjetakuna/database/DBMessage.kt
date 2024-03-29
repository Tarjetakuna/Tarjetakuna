package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import java.util.Date

data class DBMessage(
    /**
     * Message id
     */
    val uid: String,

    /**
     * Sender id
     */
    val sender: String,

    /**
     * Receiver id
     */
    val receiver: String,

    /**
     * Message content
     */
    val content: String,

    /**
     * Message timestamp
     */
    val timestamp: Date,
) : Cloneable {
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
         * Message will be invalid as we are only having the ids of the users.
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

        fun newMessage(sender: String, receiver: String, content: String): DBMessage {
            val dbMessage = DBMessage("", sender, receiver, content, Date())
            val uid = dbMessage.hashCode().toUInt().toString()
            return dbMessage.copy(uid = uid)
        }
    }

    public override fun clone(): DBMessage {
        return DBMessage(uid, sender, receiver, content, timestamp)
    }
}
