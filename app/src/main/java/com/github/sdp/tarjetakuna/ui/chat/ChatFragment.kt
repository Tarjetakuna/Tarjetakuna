package com.github.sdp.tarjetakuna.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UsernamesRTDB
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

            // This is the username of the user we are chatting with
            val otherUserId = if (it.user1.uid == ChatViewModel.currentUser.getCurrentUser().uid) it.user2.uid else it.user1.uid
            setUserName(binding.chatUsernameText, otherUserId)
                viewModel.updateLastRead()
        }

        binding.chatSendButton.setOnClickListener {
            viewModel.sendMessage(binding.chatMessageText.text.toString())
            binding.chatMessageText.text.clear()
        }

        viewModel.attachChatListener()

        viewModel.updateLastRead()
        return root
    }

    private fun setUserName(textview: TextView, userID: String) {
        UsernamesRTDB(FirebaseDB()).getUsernameFromUID(userID).thenAccept {
            if(it == null) {
                textview.text = "Unknown user"
            }else{
                textview.text = it.value.toString()
            }
        }.exceptionally {
            textview.text = "Unknown user"
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.updateLastRead()
        viewModel.detachChatListener()
        _binding = null
    }

}
