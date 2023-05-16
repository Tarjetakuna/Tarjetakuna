package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Message
import com.google.android.gms.tasks.Task
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
    fun addChat(chat: DBChat): Task<Void> {
        return chatsTable.child(chat.uid).setValue(chatToDBFormat(chat))
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
     * Remove a chat from the chats table.
     */
    fun removeChat(chatUID: String): Task<Void> {
        return chatsTable.child(chatUID).removeValue()
    }

    /**
     * Add a chat to the database.
     */
    fun addChatToDatabase(chat: Chat): ArrayList<Task<Void>> {
        val tasks = ArrayList<Task<Void>>()
        val task = addChat(DBChat.toDBChat(chat))
        tasks.add(task)
        for (message in chat.messages) {
            val msgTask = messagesRTDB.addMessageToDatabase(message)
            tasks.add(msgTask)
        }
        return tasks
    }

    /**
     * Get a chat from the database.
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
     * Add a message to a chat.
     */
    fun addMessageToChat(chatUID: String, message: DBMessage): CompletableFuture<Void> {
        return getChat(chatUID).thenCompose {
            if (!it.messages.contains(message.uid) && (
                        (it.user1 == message.sender && it.user2 == message.receiver) ||
                                (it.user1 == message.receiver && it.user2 == message.sender))
            ) {
                // add message to chat
                it.messages.add(message.uid)

                val future = CompletableFuture<Void>()
                // add chat and message to database
                addChat(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Message added to chat")
                        messagesRTDB.addMessage(message)
                        future.complete(null)
                    } else {
                        Log.d(TAG, "Message not added to chat")
                        future.completeExceptionally(task.exception!!)
                    }
                }
                return@thenCompose future
            } else {
                val future = CompletableFuture<Void>()
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
    fun clearChats(): Task<Void> {
        return chatsTable.removeValue()
    }
}
