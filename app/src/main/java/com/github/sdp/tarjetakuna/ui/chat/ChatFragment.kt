package com.github.sdp.tarjetakuna.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.databinding.FragmentChatBinding
import com.github.sdp.tarjetakuna.model.User

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatViewModel

    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.currentUser.observe(viewLifecycleOwner) {
            currentUser = it
        }

        viewModel.chat.observe(viewLifecycleOwner) {
            binding.chatMessagesRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.chatMessagesRecyclerView.adapter = MessageListAdapter(it, currentUser)
            binding.msgUsernameText.text =
                if (it.user1.username == currentUser.username) it.user2.username else currentUser.username
        }

        return root
    }

}
