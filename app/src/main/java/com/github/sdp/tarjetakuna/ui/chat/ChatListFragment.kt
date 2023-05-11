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
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.User
import com.google.gson.Gson

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatListViewModel

    private val fakeCoordinates: Coordinates = Coordinates(0.0f, 0.0f)
    private val fakeCards: ArrayList<DBMagicCard> = ArrayList()
    private val fakeUSer: User =
        User("fakeUser.email@gmail.com", "Fake User", fakeCards, fakeCoordinates)

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
            binding.chatsRecyclerView.adapter = ChatListAdapter(it, fakeUSer)
            initOnChatClickListener(binding.chatsRecyclerView.adapter as ChatListAdapter)
        }

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
