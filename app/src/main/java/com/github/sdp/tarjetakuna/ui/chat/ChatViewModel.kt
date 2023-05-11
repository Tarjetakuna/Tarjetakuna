package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.Chat

class ChatViewModel : ViewModel() {
    fun setChat(chat: Chat) {
        _chat.postValue(chat)
    }

    private val _chat = MutableLiveData<Chat>()
    val chat: LiveData<Chat> = _chat
}
