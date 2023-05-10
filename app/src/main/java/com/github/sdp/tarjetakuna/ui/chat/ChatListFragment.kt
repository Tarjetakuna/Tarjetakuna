package com.github.sdp.tarjetakuna.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
            binding.chatsRecyclerView.adapter = ChatListAdapter(it)
        }

        return root
    }

}
