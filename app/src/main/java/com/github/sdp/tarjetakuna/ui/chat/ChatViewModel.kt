package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.User
import com.github.sdp.tarjetakuna.utils.ChatsData

class ChatViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"

        var o_currentUser: User = ChatsData.fakeUser1
        var o_chat: Chat = ChatsData.fakeChat1
    }

    // TODO : link to database - for now, use mock data
    fun updateData() {
        _currentUser.postValue(o_currentUser)
        _chat.postValue(o_chat)
    }

    fun setChat(chat: Chat) {
        _chat.postValue(chat)
    }

    fun setCurrentUser(user: User) {
        _currentUser.postValue(user)
    }

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private val _chat = MutableLiveData<Chat>()
    val chat: LiveData<Chat> = _chat
}
