package com.github.sdp.tarjetakuna.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.databinding.FragmentChatBinding
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import java.util.*

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChatViewModel

    private val fakeCoordinates: Coordinates = Coordinates(0.0f, 0.0f)
    private val fakeCards: ArrayList<DBMagicCard> = ArrayList()
    private val fakeUser1: User =
        User("fakeUser2.email@gmail.com", "Fake User1", fakeCards, fakeCoordinates)

    private val fakeUser2: User =
        User("fakeUser2.email@gmail.com", "Fake User2", fakeCards, fakeCoordinates)

    private val fakeMessage1: Message =
        Message(1325, fakeUser1, fakeUser2, "Hello", Date(1683828633000))
    private val fakeMessage2: Message =
        Message(1326, fakeUser2, fakeUser1, "Hello you too", Date(1683828661000))
    private val fakeMessage3: Message =
        Message(1327, fakeUser1, fakeUser2, "What are you doing ?", Date(1683828691000))
    private val fakeMessage4: Message =
        Message(1328, fakeUser2, fakeUser1, "Nothing, you ?", Date(1683828791000))
    private val fakeMessage5: Message =
        Message(
            1329,
            fakeUser1,
            fakeUser2,
            "Chilling in bed doing sdp \n but i m so lazy \n i hate it asd adsfsdf asdasdfs adf asdssd fsd fff  fffff",
            Date(1683828891000)
        )

    private val fakeMessages: ArrayList<Message> =
        arrayListOf(fakeMessage1, fakeMessage2, fakeMessage3, fakeMessage4, fakeMessage5)
    private val fakeChat: Chat = Chat(1200, fakeUser1, fakeUser2, fakeMessages)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.chat.observe(viewLifecycleOwner) {
            binding.chatMessagesRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.chatMessagesRecyclerView.adapter = MessageListAdapter(fakeChat, fakeUser1)
            binding.msgUsernameText.text = fakeUser2.username
        }

        viewModel.setChat(fakeChat)

        return root
    }

}
