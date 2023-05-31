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
        val o_otherUserUID = MutableLiveData<String>()

        /**
         * This method is used to set the current user interface to be used by the view model.
         * FOR TESTING PURPOSES ONLY.
         */
        fun setCurrentUserInterface(newCurrentUser: CurrentUserInterface) {
            currentUser = newCurrentUser
        }
    }

    fun attachChatListener() {
        if (!currentUser.isUserLoggedIn()) return

        currentUser.attachChatListener(listener = {
            o_ChatsRTDB.getChatFromDatabase(currentUser.getChatUID())
                .thenAccept { chat ->
                    _chat.postValue(chat)
                    if (chat.user1.uid == currentUser.getCurrentUser().uid)
                        o_otherUserUID.postValue(chat.user2.uid)
                    else
                        o_otherUserUID.postValue(chat.user1.uid)
                }
        })
    }

    fun detachChatListener() {
        if (!currentUser.isUserLoggedIn()) return
        currentUser.detachChatListener()
    }

    fun sendMessage(message: String) {
        if (!currentUser.isUserLoggedIn()) return
        currentUser.sendMessageToUser(message, o_otherUserUID.value!!)
    }

    fun updateLastRead() {
        if (!currentUser.isUserLoggedIn()) return
        if(o_otherUserUID.value == null) return
        currentUser.updateLastRead(o_otherUserUID.value!!)
    }

    private val _chat = o_chat
    val chat: LiveData<Chat> = _chat
}
