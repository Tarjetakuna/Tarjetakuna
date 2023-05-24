package com.github.sdp.tarjetakuna.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.chat.observe(viewLifecycleOwner) {
            binding.chatMessagesRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.chatMessagesRecyclerView.adapter =
                MessageListAdapter(it, ChatViewModel.currentUser.getCurrentUser())
            binding.chatUsernameText.text =
                if (it.user1.username == ChatViewModel.currentUser.getCurrentUser().username) it.user2.username else it.user1.username
        }

        viewModel.attachChatListener()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.detachChatListener()
        _binding = null
    }

}
