package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.User

class ChatViewModel : ViewModel() {
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
