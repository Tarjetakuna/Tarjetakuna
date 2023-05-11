package com.github.sdp.tarjetakuna.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.databinding.FragmentChatListBinding
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import com.google.gson.Gson
import java.util.*

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatListViewModel

    private val fakeCoordinates: Coordinates = Coordinates(0.0f, 0.0f)
    private val fakeCards: ArrayList<DBMagicCard> = ArrayList()
    private val fakeUser1: User =
        User("fakeUser1.email@gmail.com", "Fake User1", fakeCards, fakeCoordinates)
    private val fakeUser2: User =
        User("fakeUser2.email@gmail.com", "Fake User2", fakeCards, fakeCoordinates)

    private val fakeMessage1: Message =
        Message(1325, fakeUser1, fakeUser2, "Hello", Date(1683828633000))
    private val fakeMessage2: Message =
        Message(1326, fakeUser2, fakeUser1, "Hello you too", Date(1683828661000))
    private val fakeMessage3: Message =
        Message(1327, fakeUser1, fakeUser2, "What are you doing ?", Date(1683828691000))

    private val fakeMessages1 =
        arrayListOf(fakeMessage1, fakeMessage2, fakeMessage3)
    private val fakeChat1: Chat = Chat(1200, fakeUser1, fakeUser2, fakeMessages1)

    private val fakeMessage4: Message =
        Message(1328, fakeUser2, fakeUser1, "Bonjour", Date(1683828633000))
    private val fakeMessage5: Message =
        Message(1329, fakeUser1, fakeUser2, "Bonjour you too", Date(1683828661000))
    private val fakeMessage6: Message =
        Message(1330, fakeUser2, fakeUser1, "Qu est ce que tu fais?", Date(1683828691000))

    private val fakeMessages2 =
        arrayListOf(fakeMessage4, fakeMessage5, fakeMessage6)
    private val fakeChat2: Chat = Chat(1201, fakeUser1, fakeUser2, fakeMessages2)

    private val fakeChats: ArrayList<Chat> = arrayListOf(fakeChat1, fakeChat2)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChatListViewModel::class.java]

        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.chats.observe(viewLifecycleOwner) {
            binding.chatsRecyclerView.layoutManager = LinearLayoutManager(context)
            val sortedChats = it.sortedByDescending { chat -> chat.timestamp }
            binding.chatsRecyclerView.adapter = ChatListAdapter(sortedChats, fakeUser1)
            initOnChatClickListener(binding.chatsRecyclerView.adapter as ChatListAdapter)
        }

        viewModel.setChats(fakeChats)

        return root
    }

    private fun initOnChatClickListener(adapter: ChatListAdapter) {
        adapter.onChatClickListener = object : ChatListAdapter.OnChatClickListener {
            override fun onClick(position: Int) {
                val bundle = Bundle()
                bundle.putString("chat", Gson().toJson(adapter.chats[position]))
                (requireActivity() as MainActivity).changeFragment(R.id.nav_chat, bundle)
            }
        }
    }

}
