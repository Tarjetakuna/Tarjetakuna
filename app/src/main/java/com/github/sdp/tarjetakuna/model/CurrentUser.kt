package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.database.DBChat
import com.github.sdp.tarjetakuna.database.DBMessage
import java.util.concurrent.CompletableFuture

object CurrentUser : CurrentUserInterface {
    private var currentUser: User? = null
    private var chatUID: String? = null

    override fun getCurrentUser(): User {
        return currentUser!!
    }

    override fun setCurrentUser(user: User) {
        if (currentUser != null) removeCurrentUser()

        currentUser = user
        currentUser!!.addChatsListener()
    }

    override fun removeCurrentUser() {
        if (currentUser == null) return
        currentUser!!.removeChatsListener()
        currentUser!!.removeChatListener()
        currentUser = null
    }

    override fun isUserLoggedIn(): Boolean {
        return currentUser != null
    }

    override fun attachChatsListener(listener: () -> Unit) {
        currentUser!!.addChatsListener(listener)
    }

    override fun detachChatsListener() {
        currentUser!!.addChatsListener()
    }

    override fun setChatUID(chatUID: String) {
        this.chatUID = chatUID
    }

    override fun getChatUID(): String {
        return chatUID!!
    }

    override fun removeChatUID() {
        chatUID = null
    }

    override fun attachChatListener(listener: () -> Unit) {
        currentUser!!.addChatListener(chatUID!!, listener)
    }

    override fun detachChatListener() {
        currentUser!!.removeChatListener()
    }

    override fun sendMessageToUser(
        message: String,
        userUID: String
    ): CompletableFuture<Pair<DBChat, DBMessage>> {
        return currentUser!!.sendMessageToUser(message, userUID)
    }

    override fun updateLastRead(otherUserUID: String) {
        currentUser!!.updateLastRead(otherUserUID)
    }
}
