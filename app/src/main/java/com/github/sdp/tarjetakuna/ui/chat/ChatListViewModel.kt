package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.ChatsRTDB
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.CurrentUser
import com.github.sdp.tarjetakuna.model.User
import com.github.sdp.tarjetakuna.utils.ChatsData

class ChatListViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"

        var o_currentUser: User = ChatsData.fakeUser1

        val o_ChatRTDB = ChatsRTDB()
    }

    fun attachChatsListener() {
        if (CurrentUser.isUserLoggedIn()) return
        _currentUser.postValue(CurrentUser.getCurrentUser())
        CurrentUser.attachChatsListener(listener = {
            o_ChatRTDB.getChatsFromDatabase(CurrentUser.getCurrentUser().chats.map { it.uid })
                .thenAccept { chats ->
                    _chats.postValue(chats as MutableList<Chat>)
                }
        })
    }

    fun detachChatsListener() {
        if (CurrentUser.isUserLoggedIn()) return
        CurrentUser.detachChatsListener()
    }

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private val _chats = MutableLiveData<MutableList<Chat>>()
    val chats: LiveData<MutableList<Chat>> = _chats
}
