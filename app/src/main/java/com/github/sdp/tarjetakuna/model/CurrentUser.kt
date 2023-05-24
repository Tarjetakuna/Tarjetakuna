package com.github.sdp.tarjetakuna.model

object CurrentUser : CurrentUserInterface {
    private var currentUser: User? = null
    private var chatUID: String? = null

    override fun getCurrentUser(): User {
        return currentUser!!
    }

    override fun setCurrentUser(user: User) {
        currentUser = user
        currentUser!!.addChatsListener()
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

    override fun removeCurrentUser() {
        currentUser!!.removeChatsListener()
        currentUser!!.removeChatListener()
        currentUser = null
    }

    override fun isUserLoggedIn(): Boolean {
        return currentUser != null
    }
}
