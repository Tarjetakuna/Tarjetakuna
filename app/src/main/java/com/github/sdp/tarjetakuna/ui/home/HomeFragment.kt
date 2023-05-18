package com.github.sdp.tarjetakuna.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.databinding.FragmentHomeBinding
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicCardType
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicRarity
import com.github.sdp.tarjetakuna.model.MagicSet
import com.google.gson.Gson
import java.time.LocalDate

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val arrayCards = ArrayList<String>()
        arrayCards.add("cards")
        LocalDatabaseProvider.deleteDatabases(requireContext(), arrayCards)

        val textView: TextView = binding.homeWelcomeText
        homeViewModel.titleText.observe(viewLifecycleOwner) {
            textView.text = it

        }
        // TODO remove when not owned card search is implemented
        homeViewModel.localDatabase = LocalDatabaseProvider.setDatabase(
            requireContext(),
            LocalDatabaseProvider.CARDS_DATABASE_NAME
        )
        val descTextView: TextView = binding.homeWelcomeDescription
        homeViewModel.descriptionText.observe(viewLifecycleOwner) {
            descTextView.text = it
        }

        val authenticationButton: Button = binding.homeAuthenticationButton
        authenticationButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeFragment(R.id.nav_authentication_button)
        }

        val addRandomCardButton: Button = binding.addRandomCardButton
        addRandomCardButton.setOnClickListener {
            homeViewModel.addRandomCard()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
