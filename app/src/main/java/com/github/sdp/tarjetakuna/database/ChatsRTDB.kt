package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the chats table in the database.
 */
class ChatsRTDB(database: Database = FirebaseDB()) {

    private var chatsTable: DatabaseReference
    private var messagesRTDB: MessagesRTDB
    private var chatsEventListener: HashMap<String, ValueEventListener> = HashMap()

    companion object {
        private const val TAG = "ChatsRTDB"
    }

    init {
        chatsTable = database.chatsTable() //Firebase.database.reference.child("chats")
        messagesRTDB = MessagesRTDB(database)
    }

    /**
     * Add a chat to the chats table.
     */
    fun addChat(chat: DBChat): CompletableFuture<DBChat> {
        val future = CompletableFuture<DBChat>()
        val tasks = chatsTable.child(chat.uid).setValue(chatToDBFormat(chat))
        tasks.addOnSuccessListener {
            future.complete(chat)
            Log.d(TAG, "Chat ${chat.uid} added to chat table")
        }.addOnFailureListener {
            future.completeExceptionally(it)
            Log.w(TAG, "Chat ${chat.uid} failed to be added to chat table")
        }
        return future
    }

    /**
     * Get a chat from the chats table.
     */
    fun getChat(chatUID: String): CompletableFuture<DBChat> {
        val future = CompletableFuture<DBChat>()
        chatsTable.child(chatUID).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("chat $chatUID is not in chats table"))
            } else {
                future.complete(chatFromDBFormat(it))
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Get some chats from the chats table.
     */
    fun getChats(chatsUID: List<String>): CompletableFuture<List<DBChat>> {
        val chatsFuture: List<CompletableFuture<DBChat>> = chatsUID.map { getChat(it) }

        return CompletableFuture.allOf(*chatsFuture.toTypedArray())
            .thenApply { chatsFuture.map { it.get() } }
    }

    /**
     * Remove a chat from the chats table.
     */
    fun removeChat(chatUID: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val task = chatsTable.child(chatUID).removeValue()
        task.addOnSuccessListener {
            future.complete(chatUID)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Add a chat to the database along with its messages.
     * in chats and messages nodes.
     */
    fun addChatToDatabase(chat: Chat): CompletableFuture<Chat> {
        val future = CompletableFuture<Chat>()
        val futureMessages: MutableList<CompletableFuture<Message>> = ArrayList()
        val futureDBChat = addChat(DBChat.toDBChat(chat))

        for (message in chat.messages) {
            val futureMessage = messagesRTDB.addMessageToDatabase(message)
            futureMessages.add(futureMessage)
        }
        futureDBChat.thenCompose {
            CompletableFuture.allOf(*futureMessages.toTypedArray()).thenAccept {
                future.complete(chat)
                Log.d(TAG, "all messages for ${chat.uid} added to messages table")
            }.exceptionally {
                future.completeExceptionally(it)
                Log.w(TAG, "some messages for ${chat.uid} failed to be added to messages table")
                return@exceptionally null
            }
        }.exceptionally {
            future.completeExceptionally(it)
            Log.w(TAG, "dbChat ${chat.uid} failed to be added to chats table")
            return@exceptionally null
        }

        return future
    }

    /**
     * Get a chat from the database along with its messages.
     * in chat and messages nodes.
     */
    fun getChatFromDatabase(chatUID: String): CompletableFuture<Chat> {
        val future = CompletableFuture<Chat>()
        getChat(chatUID).thenAccept {
            val chat = DBChat.fromDBChat(it)
            messagesRTDB.getMessagesFromDatabase(it.messages).thenAccept { it2 ->
                chat.messages = it2 as ArrayList<Message>
                future.complete(chat)
            }.exceptionally { it2 ->
                future.completeExceptionally(it2)
                return@exceptionally null
            }
        }.exceptionally {
            future.completeExceptionally(it)
            return@exceptionally null
        }
        return future
    }

    /**
     * Get some chats from the database along with their messages.
     * in chat and messages nodes.
     */
    fun getChatsFromDatabase(chatsUID: List<String>): CompletableFuture<List<Chat>> {
        val chatsFuture: List<CompletableFuture<Chat>> = chatsUID.map { getChatFromDatabase(it) }

        return CompletableFuture.allOf(*chatsFuture.toTypedArray())
            .thenApply { chatsFuture.map { it.get() } }
    }

    /**
     * Add a message to a chat.
     */
    fun addMessageToChat(
        chatUID: String,
        message: DBMessage
    ): CompletableFuture<Pair<String, DBMessage>> {
        return getChat(chatUID).thenCompose {
            if (!it.messages.contains(message.uid) && (
                        (it.user1 == message.sender && it.user2 == message.receiver) ||
                                (it.user1 == message.receiver && it.user2 == message.sender))
            ) {
                // add message to chat
                it.messages.add(message.uid)

                val future = CompletableFuture<Pair<String, DBMessage>>()
                // add chat and message to database
                addChat(it).thenAccept { _ ->
                    messagesRTDB.addMessage(message).thenAccept {
                        future.complete(Pair(chatUID, message))
                    }.exceptionally { it3 ->
                        future.completeExceptionally(it3)
                        return@exceptionally null
                    }
                }
                return@thenCompose future
            } else {
                val future = CompletableFuture<Pair<String, DBMessage>>()
                future.completeExceptionally(
                    NoSuchFieldException("message $message is not add to chat $chatUID")
                )
                Log.d(TAG, "Message not added to chat")
                return@thenCompose future
            }
        }
    }

    /**
     * Add a listener for when a chat changes
     */
    fun addChatListener(chatUID: String, listener: (DBChat) -> Unit) {
        chatsEventListener[chatUID] =
            chatsTable.child(chatUID).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.w(TAG, "ValueEventListener:onDataChange snapshot: $snapshot")
                    if (snapshot.value == null) {
                        return
                    }
                    listener(chatFromDBFormat(snapshot))
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
    }

    /**
     * Remove a listener on a chat
     */
    fun removeChatListener(chatUID: String) {
        if (chatsEventListener.containsKey(chatUID)) {
            chatsTable.child(chatUID).removeEventListener(chatsEventListener[chatUID]!!)
            chatsEventListener.remove(chatUID)
        }
    }

    /**
     * Convert a chat to a string to store in the database.
     */
    private fun chatToDBFormat(chat: DBChat): String {
        return Gson().toJson(chat).toString()
    }

    /**
     * Convert a string from the database to a chat.
     */
    private fun chatFromDBFormat(data: DataSnapshot): DBChat {
        return Gson().fromJson(data.value as String, DBChat::class.java)
    }

    /**
     * Clear all chats from the database.
     */
    fun clearChats(): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()
        chatsTable.removeValue().addOnSuccessListener {
            future.complete(null)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }
}
