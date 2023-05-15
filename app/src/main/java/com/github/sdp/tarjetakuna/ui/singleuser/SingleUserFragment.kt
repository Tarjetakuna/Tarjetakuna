package com.github.sdp.tarjetakuna.ui.singleuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.databinding.FragmentSingleUserBinding
import com.github.sdp.tarjetakuna.model.User
import com.google.gson.Gson

/**
 * Fragment which displays a single set
 */
class SingleUserFragment : Fragment() {

    private var _binding: FragmentSingleUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleUserBinding.inflate(inflater, container, false)

        loadUserFromJson()

        return binding.root
    }

    /**
     * Loads the user from the json string passed in the arguments.
     */
    private fun loadUserFromJson() {
        try {
            val user = Gson().fromJson(arguments?.getString(ARG_USER), User::class.java)
            binding.singleUserUserNameText.text = user.username
            binding.singleUserUserEmailText.text = user.email
            binding.singleUserUserNumberOwnedCards.text =
                getString(
                    R.string.single_user_owned_cards_number,
                    user.cards.filter { it.possession == CardPossession.OWNED }.size
                )
            binding.singleUserUserNumberWantedCards.text =
                getString(
                    R.string.single_user_wanted_cards_number,
                    user.cards.filter { it.possession == CardPossession.WANTED }.size
                )

        } catch (e: Exception) {
            binding.singleUserUserNameText.text =
                getString(R.string.single_user_error_loading)
            binding.singleUserUserEmailText.text = ""
            binding.singleUserUserNumberOwnedCards.text = ""
            binding.singleUserUserNumberWantedCards.text = ""
        }
    }

    companion object {
        const val ARG_USER = "user"
    }
}
