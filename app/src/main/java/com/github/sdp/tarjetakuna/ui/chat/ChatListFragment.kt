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

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatListViewModel

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
            binding.chatsRecyclerView.adapter =
                ChatListAdapter(sortedChats, ChatListViewModel.currentUser.getCurrentUser())
            initOnChatClickListener(binding.chatsRecyclerView.adapter as ChatListAdapter)
        }

        viewModel.autoChatActive.observe(viewLifecycleOwner) {
            if (it != null) {
                if(it) {
                    ChatListViewModel.currentUser.setChatUID(viewModel.autoChat!!.uid)
                    viewModel.resetAutoChat()
                    (requireActivity() as MainActivity).changeFragment(R.id.nav_chat)
                }
            }
        }

        viewModel.attachChatsListener()

        // This is the userUID of the user we want to chat with, loaded from the arguments
        arguments?.getString("userUID")?.let { it1 -> viewModel.loadChatWithUser(it1) }
        arguments?.clear()

        return root
    }

    private fun initOnChatClickListener(adapter: ChatListAdapter) {
        adapter.onChatClickListener = object : ChatListAdapter.OnChatClickListener {
            override fun onClick(position: Int) {
                ChatListViewModel.currentUser.setChatUID(adapter.chats[position].uid)
                (requireActivity() as MainActivity).changeFragment(R.id.nav_chat)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.detachChatsListener()
        _binding = null
    }

}
