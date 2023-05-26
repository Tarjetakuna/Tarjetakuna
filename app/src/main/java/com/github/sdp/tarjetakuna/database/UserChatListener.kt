package com.github.sdp.tarjetakuna.database

interface UserChatListener {
    fun onChatRemoved()
    fun onChatChanged(messagesChanged: List<DBMessage>)
}
