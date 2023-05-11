package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.Chat

class ChatListViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val _chats = MutableLiveData<ArrayList<Chat>>()
    val chats: LiveData<ArrayList<Chat>> = _chats
}
