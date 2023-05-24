package com.github.sdp.tarjetakuna.model

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
}
