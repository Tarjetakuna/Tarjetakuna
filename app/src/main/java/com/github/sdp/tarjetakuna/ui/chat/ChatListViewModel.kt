package com.github.sdp.tarjetakuna.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.database.ChatsRTDB
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.CurrentUser
import com.github.sdp.tarjetakuna.model.User
import com.github.sdp.tarjetakuna.utils.ChatsData
import com.github.sdp.tarjetakuna.utils.ChatsData.fakeChats1

class ChatListViewModel : ViewModel() {

    companion object {
        private const val TAG = "ChatListViewModel"

        var o_currentUser: User = ChatsData.fakeUser1
        var o_chats: HashMap<String, Chat> = HashMap(fakeChats1.associateBy { it.uid })

        val o_ChatRTDB = ChatsRTDB()
    }

    // TODO : link to database - for now, use mock data
    fun addChatsListener() {
        if (CurrentUser.isUserLoggedIn()) return

        o_currentUser = CurrentUser.getCurrentUser()!!

        o_currentUser.getChats().thenApply { dbChats ->
            dbChats.map { it ->
                o_ChatRTDB.addChatListener(it.uid) { dbChat ->
                    if (dbChat == null) {
                        if (o_chats.containsKey(it.uid)) {
                            o_chats.remove(it.uid)
                        }
                        o_ChatRTDB.removeChatListener(it.uid)
                        val newChats =
                            o_chats.values.sortedBy { it.timestamp }.reversed() as ArrayList<Chat>
                        _chats.postValue(newChats)
                        return@addChatListener
                    }
                    o_ChatRTDB.getChatFromDatabase(dbChat.uid).thenApply { chat ->
                        o_chats[chat.uid] = chat
                        val newChats =
                            o_chats.values.sortedBy { it.timestamp }.reversed() as ArrayList<Chat>
                        _chats.postValue(newChats)
                    }
                }
            }
        }
    }

    fun removeChatsListener() {

    }


    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    private val _chats = MutableLiveData<ArrayList<Chat>>()
    val chats: LiveData<ArrayList<Chat>> = _chats
}
