package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.ChatsRTDB
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.CurrentUser
import com.github.sdp.tarjetakuna.model.CurrentUserInterface

class ChatListViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"
        var currentUser: CurrentUserInterface = CurrentUser

        val o_ChatRTDB = ChatsRTDB()
        val o_chats = MutableLiveData<MutableList<Chat>>()

        /**
         * This method is used to set the current user interface to be used by the view model.
         * FOR TESTING PURPOSES ONLY.
         */
        fun setCurrentUserInterface(newCurrentUser: CurrentUserInterface) {
            currentUser = newCurrentUser
        }
    }

    fun attachChatsListener() {
        if (currentUser.isUserLoggedIn()) return

        currentUser.attachChatsListener(listener = {
            o_ChatRTDB.getChatsFromDatabase(currentUser.getCurrentUser().chats.map { it.uid })
                .thenAccept { chats ->
                    _chats.postValue(chats as MutableList<Chat>)
                }
        })
    }

    fun detachChatsListener() {
        if (currentUser.isUserLoggedIn()) return
        currentUser.detachChatsListener()
    }

    private val _chats = o_chats
    val chats: LiveData<MutableList<Chat>> = _chats
}
