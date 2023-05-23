package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the global collection of cards contained by all users (no duplicates).
 */
class MessagesRTDB(database: Database = FirebaseDB()) {

    private var messagesTable: DatabaseReference

    companion object {
        private const val TAG = "MessagesRTDB"
    }

    init {
        messagesTable = database.messagesTable() //Firebase.database.reference.child("messages")
    }

    /**
     * Add a [DBMessage] to the messages table.
     */
    fun addMessage(message: DBMessage): CompletableFuture<DBMessage> {
        val future = CompletableFuture<DBMessage>()
        val task = messagesTable.child(message.uid).setValue(messageToDBFormat(message))
        task.addOnSuccessListener {
            future.complete(message)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
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
    fun removeMessage(messageUID: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val task = messagesTable.child(messageUID).removeValue()
        task.addOnSuccessListener {
            future.complete(messageUID)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Add a [Message] to the messages table.
     */
    fun addMessageToDatabase(message: Message): CompletableFuture<Message> {
        val future = CompletableFuture<Message>()
        val futureDBMessage = addMessage(DBMessage.toDBMessage(message))
        futureDBMessage.thenAccept {
            future.complete(message)
            Log.d(TAG, "Message ${message.uid} added to messages table")
        }.exceptionally {
            future.completeExceptionally(it)
            Log.d(TAG, "Message ${message.uid} failed to be added to messages table")
            return@exceptionally null
        }
        return future
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
    fun clearMessages(): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        messagesTable.removeValue().addOnSuccessListener {
            future.complete(null)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }
}
