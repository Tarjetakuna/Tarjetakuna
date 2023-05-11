package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.Chat

class ChatListViewModel : ViewModel() {
    fun setChats(chats: ArrayList<Chat>) {
        _chats.postValue(chats)
    }

    private val _chats = MutableLiveData<ArrayList<Chat>>()
    val chats: LiveData<ArrayList<Chat>> = _chats
}
