package com.github.sdp.tarjetakuna.database

interface UserChatsListener {
    fun onChatsRemoved()
    fun onChatsChanged(chatsChanged: List<DBChat>)
}
