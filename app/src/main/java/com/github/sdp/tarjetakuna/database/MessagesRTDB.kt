package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the global collection of cards contained by all users (no duplicates).
 */
class MessagesRTDB(database: Database) {

    private var messagesTable: DatabaseReference

    init {
        messagesTable = database.chatsTable() //Firebase.database.reference.child("messages")
    }

    /**
     * Add a message to the messages table.
     */
    fun addMessage(message: DBMessage) {
        messagesTable.child(message.uid).setValue(message)
    }

    /**
     * Get a message from the messages table.
     */
    fun getMessage(messageUID: String): CompletableFuture<DBMessage> {
        val future = CompletableFuture<DBMessage>()
        messagesTable.child(messageUID).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("message $messageUID is not in messages table"))
            } else {
                future.complete(it.value as DBMessage)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Remove a message from the messages table.
     */
    fun removeMessage(messageUID: String) {
        messagesTable.child(messageUID).removeValue()
    }

    fun addMessageToDatabase(message: Message) {
        addMessage(DBMessage.toDBMessage(message))
    }

    fun getMessageFromDatabase(messageUID: String): CompletableFuture<Message> {
        val future = CompletableFuture<Message>()
        getMessage(messageUID).thenAccept {
            future.complete(DBMessage.fromDBMessage(it))
        }.exceptionally {
            future.completeExceptionally(it)
        }
        return future
    }

    fun getMessagesFromDatabase(messagesUID: List<String>): CompletableFuture<List<Message>> {
        val messagesFuture: List<CompletableFuture<Message>> =
            messagesUID.map { getMessageFromDatabase(it) }

        return CompletableFuture.allOf(*messagesFuture.toTypedArray())
            .thenApply { messagesFuture.map { it.get() } }
    }

}
