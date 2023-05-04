package com.github.sdp.tarjetakuna.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentUserCardsBinding

/**
 * Fragment to manage cards in the user's collection.
 */
class UserCardsFragment : Fragment() {

    private lateinit var viewModel: UserCardsViewModel

    private var _binding: FragmentUserCardsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[UserCardsViewModel::class.java]
        _binding = FragmentUserCardsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val setMessage: TextView = binding.userCardsSetCardText
        setMessage.text = ""
        val getMessage: TextView = binding.userCardsGetCardText
        getMessage.text = ""
        val removeMessage: TextView = binding.userCardsRemoveCardText
        removeMessage.text = ""

        //go back to home fragment
        val backButton = binding.userCardsHomeButton
        backButton.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.changeFragment(R.id.nav_home)
        }

        // Adding a card to the user's collection (database)
        val setButton = binding.userCardsSetCardButton
        setButton.setOnClickListener {
            //TODO replace this with a function call
            viewModel.onSetButtonClick(viewModel.card1)
        }
        // Retrieving card from the user's collection (database)
        val getButton = binding.userCardsGetCardButton
        getButton.setOnClickListener {
            //TODO replace this with a function call
            viewModel.onGetButtonClick(viewModel.card1)
        }
        //Removing card from user's collection (database)
        val removeButton = binding.userCardsRemoveCardButton
        removeButton.setOnClickListener {
            //TODO replace this with a function call
            viewModel.onRemoveButtonClick(viewModel.card1)
        }
        // observe the message from the view model and display it in the UI when it changes
        viewModel.setMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                putSetMessage(getString(it.first, it.second))
            }
        }
        viewModel.getMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                putGetMessage(getString(it.first, it.second))
            }
        }
        viewModel.removeMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                putRemoveMessage(getString(it.first, it.second))
            }
        }
        return root
    }

    /**
     * put the message in the "getMessage" textview.
     * @param msg the message to be displayed
     */
    private fun putGetMessage(msg: String) {
        val getMessage: TextView = binding.userCardsGetCardText
        getMessage.text = msg
    }

    /**
     * put the message in the "setMessage" textview.
     * @param msg the message to be displayed
     */
    private fun putSetMessage(msg: String) {
        val setMessage: TextView = binding.userCardsSetCardText
        setMessage.text = msg
    }

    /**
     * put the message in the "removeMessage" textview.
     * @param msg the message to be displayed
     */
    private fun putRemoveMessage(msg: String) {
        val removeMessage: TextView = binding.userCardsRemoveCardText
        removeMessage.text = msg
    }
}
