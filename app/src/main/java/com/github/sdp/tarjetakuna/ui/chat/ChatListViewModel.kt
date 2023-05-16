package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.User
import com.github.sdp.tarjetakuna.utils.ChatsData

class ChatListViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"

        var o_currentUser: User = ChatsData.fakeUser1
        var o_chats: ArrayList<Chat> = ChatsData.fakeChats1
    }

    // TODO : link to database - for now, use mock data
    fun updateData() {
        _currentUser.postValue(o_currentUser)
        _chats.postValue(o_chats)
    }

    fun setChats(chats: ArrayList<Chat>) {
        _chats.postValue(chats)
    }

    fun setCurrentUser(user: User) {
        _currentUser.postValue(user)
    }

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private val _chats = MutableLiveData<ArrayList<Chat>>()
    val chats: LiveData<ArrayList<Chat>> = _chats
}
