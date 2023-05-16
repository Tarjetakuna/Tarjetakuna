package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the global collection of cards contained by all users (no duplicates).
 */
class ChatsRTDB(database: Database) {

    private var chatsTable: DatabaseReference
    private var messagesRTDB: MessagesRTDB

    init {
        chatsTable = database.chatsTable() //Firebase.database.reference.child("chats")
        messagesRTDB = MessagesRTDB(database)
    }

    /**
     * Add a chat to the chats table.
     */
    fun addChat(chat: DBChat) {
        chatsTable.child(chat.uid).setValue(chat)
    }

    /**
     * Get a chat from the chats table.
     */
    fun getChat(chatUID: String): CompletableFuture<DBChat> {
        val future = CompletableFuture<DBChat>()
        chatsTable.child(chatUID).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("message $chatUID is not in chats table"))
            } else {
                future.complete(it.value as DBChat)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Remove a chat from the chats table.
     */
    fun removeChat(chatUID: String) {
        chatsTable.child(chatUID).removeValue()
    }

    /**
     * Add a chat to the database.
     */
    fun addChatToDatabase(chat: Chat) {
        addChat(DBChat.toDBChat(chat))
        for (message in chat.messages) {
            messagesRTDB.addMessageToDatabase(message)
        }
    }

    /**
     * Get a chat from the database.
     */
    fun getChatFromDatabase(chatUID: String): CompletableFuture<Chat> {
        val future = CompletableFuture<Chat>()
        getChat(chatUID).thenAccept {
            val chat = DBChat.fromDBChat(it)
            messagesRTDB.getMessagesFromDatabase(it.messages).thenAccept { it2 ->
                chat.messages = it2
                future.complete(chat)
            }.exceptionally { it2 ->
                future.completeExceptionally(it2)
            }
        }.exceptionally {
            future.completeExceptionally(it)
        }
        return future
    }
}
