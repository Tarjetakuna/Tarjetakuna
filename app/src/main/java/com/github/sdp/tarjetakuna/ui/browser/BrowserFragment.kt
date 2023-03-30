package com.github.sdp.tarjetakuna.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.UserCardsRTDB
import com.github.sdp.tarjetakuna.databinding.FragmentBrowserBinding
import com.github.sdp.tarjetakuna.model.*
import com.google.gson.Gson

/**
 * Fragment that displays the cards in a recycler view
 */
class BrowserFragment : Fragment() {

    private var _binding: FragmentBrowserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val browserViewModel = ViewModelProvider(this)[BrowserViewModel::class.java]

        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.browserListCards.layoutManager = LinearLayoutManager(context)
        val card = MagicCard(
            "Gwenna, Eyes of Gaea",
            "<-: Add two mana in any combination of colors. Spend this mana only to cast creature spells or activate abilities of a creature or creature card.\n" +
                    "Whenever you cast a creature spell with power 5 or greater, put a +1/+1 counter on Gwenna, Eyes of Gaea and untap it.",
            MagicLayout.Normal,
            3,
            "{2}{W}",
            MagicSet("BRO", "The Brothers War"),
            185,
            "https://cards.scryfall.io/large/front/7/e/7ee387b7-18e4-41b7-aefe-f2b5954e3051.jpg?1674421589",
            MagicRarity.Rare,
            MagicType.Creature,
            listOf("Elf", "Druid", "Scout"),
            "2",
            "3",
            "Steve Prescott"
        )
        var cards: ArrayList<MagicCard> = ArrayList()
        var adapter = DisplayCardsAdapter(ArrayList())
        if (Demo.canBeShown) {
            cards = browserViewModel.initialCards
            adapter = DisplayCardsAdapter(browserViewModel.initialCards)
        } else {
            cards = ArrayList()
            adapter = DisplayCardsAdapter(ArrayList())
        }

        binding.browserListCards.adapter = adapter

        initSearchBar(cards)
        initOnCardClickListener(adapter)

        return root
    }

    /**
     * Initialize the search bar
     */
    private fun initSearchBar(cards: ArrayList<MagicCard>) {
        binding.browserSearchbar.isIconified = false
        binding.browserSearchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = DisplayCardsAdapter(
                    cards.filter { card ->
                        card.name.contains(newText!!, true)
                    } as ArrayList<MagicCard>
                )
                initOnCardClickListener(adapter)
                binding.browserListCards.adapter = adapter
                return true
            }
        })
        //Close keyboard when the search bar is closed
        binding.browserSearchbar.setOnCloseListener {
            getSystemService(
                requireContext(),
                InputMethodManager::class.java
            )?.hideSoftInputFromWindow(
                binding.browserSearchbar.windowToken,
                0
            )
            true
        }
    }
    /**
     * Initialize the listener for the click on a card-item of the recycler view
     */
    private fun initOnCardClickListener(
        adapter: DisplayCardsAdapter,
    ) {
        adapter.onCardClickListener =
            object : DisplayCardsAdapter.OnCardClickListener {
                override fun onCardClick(position: Int) {
                    val bundle = Bundle()
                    bundle.putString("card", Gson().toJson(adapter.cards[position]))
                    //TODO : Should be changed to remove the dependency on MainActivity
                    (requireActivity() as MainActivity).changeFragment(R.id.nav_single_card, bundle)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
