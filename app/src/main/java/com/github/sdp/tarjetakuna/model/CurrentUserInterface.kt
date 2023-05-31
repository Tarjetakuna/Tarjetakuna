package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.database.DBChat
import com.github.sdp.tarjetakuna.database.DBMessage
import java.util.concurrent.CompletableFuture

interface CurrentUserInterface {
    fun getCurrentUser(): User
    fun setCurrentUser(user: User)
    fun attachChatsListener(listener: () -> Unit)
    fun detachChatsListener()
    fun setChatUID(chatUID: String)
    fun getChatUID(): String
    fun removeChatUID()
    fun attachChatListener(listener: () -> Unit)
    fun detachChatListener()
    fun removeCurrentUser()
    fun isUserLoggedIn(): Boolean

    fun sendMessageToUser(
        message: String,
        userUID: String
    ): CompletableFuture<Pair<DBChat, DBMessage>>

    fun updateLastRead(otherUserUID: String)
}
