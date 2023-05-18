package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.Message
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the global collection of cards contained by all users (no duplicates).
 */
class MessagesRTDB(database: Database = FirebaseDB()) {

    private var messagesTable: DatabaseReference

    init {
        messagesTable = database.messagesTable() //Firebase.database.reference.child("messages")
    }

    /**
     * Add a [DBMessage] to the messages table.
     */
    fun addMessage(message: DBMessage): Task<Void> {
        return messagesTable.child(message.uid).setValue(messageToDBFormat(message))
    }

    /**
     * Get a [DBMessage] from the messages table.
     */
    fun getMessage(messageUID: String): CompletableFuture<DBMessage> {
        val future = CompletableFuture<DBMessage>()
        messagesTable.child(messageUID).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("message $messageUID is not in messages table"))
            } else {
                future.complete(messageFromDBFormat(it))
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Remove a message from the messages table.
     */
    fun removeMessage(messageUID: String): Task<Void> {
        return messagesTable.child(messageUID).removeValue()
    }

    /**
     * Add a [Message] to the messages table.
     */
    fun addMessageToDatabase(message: Message): Task<Void> {
        return addMessage(DBMessage.toDBMessage(message))
    }

    /**
     * Get a [Message] from the messages table.
     */
    fun getMessageFromDatabase(messageUID: String): CompletableFuture<Message> {
        val future = CompletableFuture<Message>()
        getMessage(messageUID).thenAccept {
            future.complete(DBMessage.fromDBMessage(it))
        }.exceptionally {
            future.completeExceptionally(it)
            return@exceptionally null
        }
        return future
    }

    /**
     * Get a list of [Message] from the messages table.
     */
    fun getMessagesFromDatabase(messagesUID: List<String>): CompletableFuture<List<Message>> {
        val messagesFuture: List<CompletableFuture<Message>> =
            messagesUID.map { getMessageFromDatabase(it) }

        return CompletableFuture.allOf(*messagesFuture.toTypedArray())
            .thenApply { messagesFuture.map { it.get() } }
    }

    /**
     * Convert a message to a string to store in the database.
     */
    private fun messageToDBFormat(message: DBMessage): String {
        return Gson().toJson(message).toString()
    }

    /**
     * Convert a string from the database to a chat.
     */
    private fun messageFromDBFormat(data: DataSnapshot): DBMessage {
        return Gson().fromJson(data.value as String, DBMessage::class.java)
    }

    /**
     * Clear all messages from the messages table.
     */
    fun clearMessages(): Task<Void> {
        return messagesTable.removeValue()
    }
}
