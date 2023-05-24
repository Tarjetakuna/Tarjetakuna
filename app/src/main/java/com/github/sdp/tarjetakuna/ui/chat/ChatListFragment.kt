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
import com.github.sdp.tarjetakuna.databinding.FragmentChatListBinding
import com.github.sdp.tarjetakuna.model.User

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatListViewModel
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChatListViewModel::class.java]

        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.currentUser.observe(viewLifecycleOwner) {
            currentUser = it
        }

        viewModel.chats.observe(viewLifecycleOwner) {
            binding.chatsRecyclerView.layoutManager = LinearLayoutManager(context)
            val sortedChats = it.sortedByDescending { chat -> chat.timestamp }
            binding.chatsRecyclerView.adapter = ChatListAdapter(sortedChats, currentUser)
            initOnChatClickListener(binding.chatsRecyclerView.adapter as ChatListAdapter)
        }

        viewModel.attachChatsListener()

        return root
    }

    private fun initOnChatClickListener(adapter: ChatListAdapter) {
        adapter.onChatClickListener = object : ChatListAdapter.OnChatClickListener {
            override fun onClick(position: Int) {
                val bundle = Bundle()
                bundle.putString("chat", adapter.chats[position].uid)
                (requireActivity() as MainActivity).changeFragment(R.id.nav_chat, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.detachChatsListener()
        _binding = null
    }

}
