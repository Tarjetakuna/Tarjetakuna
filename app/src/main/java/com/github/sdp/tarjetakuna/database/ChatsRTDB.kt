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
    private var chatListener: ValueEventListener? = null
    private var chatUIDListening: String? = null

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
            Log.d(TAG, "Chat ${chat.uid} added to chat table")
            future.complete(chat)
        }.addOnFailureListener {
            Log.w(TAG, "Chat ${chat.uid} failed to be added to chat table")
            future.completeExceptionally(it)
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
     * first in the messages table the in chats tables.
     */
    fun addChatToDatabase(chat: Chat): CompletableFuture<Chat> {
        val future = CompletableFuture<Chat>()
        val futureMessages: MutableList<CompletableFuture<Message>> = ArrayList()

        // Add all messages to messages table
        for (message in chat.messages) {
            val futureMessage = messagesRTDB.addMessageToDatabase(message)
            futureMessages.add(futureMessage)
        }
        // wait for all messages to be added to messages table then add the chat to chats table
        CompletableFuture.allOf(*futureMessages.toTypedArray()).thenAccept {
            Log.d(TAG, "all messages for ${chat.uid} added to messages table")
            val futureDBChat = addChat(DBChat.toDBChat(chat))
            futureDBChat.thenAccept {
                Log.d(TAG, "dbChat ${chat.uid} added to chats table")
                future.complete(chat)
            }.exceptionally {
                future.completeExceptionally(it)
                return@exceptionally null
            }
        }.exceptionally {
            future.completeExceptionally(it)
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
     * Add a message to a chat into messages then chats nodes.
     */
    fun addMessageToChat(
        chatUID: String,
        message: DBMessage
    ): CompletableFuture<Pair<DBChat, DBMessage>> {
        return getChat(chatUID).thenCompose {
            if (!it.messages.contains(message.uid) && (
                        (it.user1 == message.sender && it.user2 == message.receiver) ||
                                (it.user1 == message.receiver && it.user2 == message.sender))
            ) {
                // add message to chat
                it.messages.add(message.uid)

                // update last read of sender
                if (it.user1 == message.sender) {
                    it.user1LastRead = message.timestamp
                } else {
                    it.user2LastRead = message.timestamp
                }

                val future = CompletableFuture<Pair<DBChat, DBMessage>>()
                // add message then chat to database
                messagesRTDB.addMessage(message).thenAccept { _ ->
                    addChat(it).thenAccept { _ ->
                        future.complete(Pair(it, message))
                    }.exceptionally { it3 ->
                        future.completeExceptionally(it3)
                        return@exceptionally null
                    }
                }
                return@thenCompose future
            } else {
                val future = CompletableFuture<Pair<DBChat, DBMessage>>()
                future.completeExceptionally(
                    NoSuchFieldException("message $message is not add to chat $chatUID")
                )
                return@thenCompose future
            }
        }
    }

    /**
     * Add a listener for when a chat changes
     */
    fun addChatListener(chatUID: String, listener: (DBChat?) -> Unit) {
        chatsEventListener[chatUID] =
            chatsTable.child(chatUID).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.w(TAG, "ValueEventListener:onDataChange snapshot: $snapshot")
                    if (snapshot.value == null) {
                        listener(null)
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

    /**
     * Add a listener to the a chat to get updates
     * from the chat and messages tables.
     */
    fun addChatListener(chatUID: String, listener: UserChatListener) {
        if (chatListener != null && chatUIDListening != null) {
            chatsTable.child(chatUIDListening!!).removeEventListener(chatListener!!)
        }
        chatListener =
            chatsTable.child(chatUID).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.w(TAG, "ValueEventListener:onDataChange snapshot: $snapshot")
                    if (snapshot.value == null) {
                        listener.onChatRemoved()
                    } else {
                        val chat = chatFromDBFormat(snapshot)
                        messagesRTDB.getMessages(chat.messages).thenAccept { it ->
                            val messages = it as ArrayList<DBMessage>
                            listener.onChatChanged(messages)
                        }.exceptionally { it2 ->
                            Log.w(TAG, "Error getting messages for chat ${chat.uid}", it2)
                            return@exceptionally null
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        chatUIDListening = chatUID
    }

    /**
     * Remove the listener from the chat table.
     */
    fun removeChatListener() {
        if (chatListener == null || chatUIDListening == null) return
        chatListener?.let { chatsTable.child(chatUIDListening!!).removeEventListener(it) }
    }
}
