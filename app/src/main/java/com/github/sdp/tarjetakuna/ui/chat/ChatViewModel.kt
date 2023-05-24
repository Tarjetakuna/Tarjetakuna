package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.ChatsRTDB
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.CurrentUser
import com.github.sdp.tarjetakuna.model.CurrentUserInterface

class ChatViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"
        var currentUser: CurrentUserInterface = CurrentUser

        val o_ChatsRTDB = ChatsRTDB()
        val o_chat = MutableLiveData<Chat>()

        /**
         * This method is used to set the current user interface to be used by the view model.
         * FOR TESTING PURPOSES ONLY.
         */
        fun setCurrentUserInterface(newCurrentUser: CurrentUserInterface) {
            currentUser = newCurrentUser
        }
    }

    fun attachChatListener() {
        if (currentUser.isUserLoggedIn()) return

        currentUser.attachChatListener(listener = {
            o_ChatsRTDB.getChatFromDatabase(currentUser.getChatUID())
                .thenAccept { chat -> _chat.postValue(chat) }
        })
    }

    fun detachChatListener() {
        if (currentUser.isUserLoggedIn()) return
        currentUser.detachChatListener()
    }

    private val _chat = o_chat
    val chat: LiveData<Chat> = _chat
}
