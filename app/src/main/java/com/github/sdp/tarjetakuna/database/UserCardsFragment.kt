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
 * Activity to manage cards in the user's collection.
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

        val setMessage: TextView = binding.textSetCard
        setMessage.text = ""
        val getMessage: TextView = binding.textGetCard
        getMessage.text = ""

        val backButton = binding.buttonBackHome2
        backButton.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.changeFragment(R.id.nav_home)
        }

        // Adding a card to the user's collection (database)
        val setButton = binding.setCardButton
        setButton.setOnClickListener {
            //TODO replace this with a function call
            //this is for now hardcoded but will be replaced by a function call
            val msg = viewModel.usc.addCardToCollection(viewModel.card2)
            setMessage.text = msg

        }

        // observe the message from the view model and display it in the UI when it changes
        viewModel.message.observe(viewLifecycleOwner) {
            if (it != null) {
                putMessage(it)
            }
        }
        val getButton = binding.getCardButton
        getButton.setOnClickListener {
            viewModel.onCardButtonClick()
        }

        return root
    }

    /**
     * This function is used to display a message in the UI. Should be written UserCardsRTDB
     */
    private fun putMessage(msg: String) {
        val getMessage: TextView = binding.textGetCard
        getMessage.text = msg
    }

}
