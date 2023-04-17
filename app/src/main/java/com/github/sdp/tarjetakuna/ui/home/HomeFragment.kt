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
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
import com.google.gson.Gson

class HomeFragment : Fragment() {

    private val magicSet = MagicSet("MT15", "Magic 2015")
    private val cmc = 5
    private val magicLayout = MagicLayout.Normal
    private val link = "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid="
    private val cards: ArrayList<MagicCard> = arrayListOf(
        MagicCard(
            "Meandering Towershell", "Islandwalk",
            magicLayout, cmc, "{3}{G}{G}",
            magicSet, 141,
            "${link}386602"
        ),
        MagicCard(
            "Angel of Mercy", "Flying",
            magicLayout, cmc, "{4}{W}",
            magicSet, 1,
            "${link}82992"
        )
    )

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it

        }

        // TODO remove when not owned card search is implemented
        homeViewModel.localDatabase = LocalDatabaseProvider.setDatabase(requireContext(), "cards")
        //val plainText: EditText = binding.mainName2
        val button: Button = binding.mainGoButton2
        button.setOnClickListener {
            // call changeFragment()
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeFragment(R.id.nav_browser)

        }

        val filterFragmentButton: Button = binding.filterFragmentButton
        filterFragmentButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cards", Gson().toJson(cards))
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changeFragment(R.id.nav_filter, bundle)
        }

        val authenticationButton: Button = binding.authenticationButton
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
