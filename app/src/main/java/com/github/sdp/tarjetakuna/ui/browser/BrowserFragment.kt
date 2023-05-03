package com.github.sdp.tarjetakuna.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentBrowserBinding
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.utils.Utils.Companion.hideKeyboard
import com.google.gson.Gson

/**
 * Fragment that displays the cards in a recycler view
 */
class BrowserFragment : Fragment() {

    private var _binding: FragmentBrowserBinding? = null
    private lateinit var viewModel: BrowserViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[BrowserViewModel::class.java]
        _binding = FragmentBrowserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initObservers()
        initSearchBar(viewModel)
        initFilterButtonsListener()
        initSorterButtonsListener()

        return root
    }

    /**
     * Initialize the different observers to apply the different filters and sorters
     */
    private fun initObservers() {

        viewModel.searchState.observe(viewLifecycleOwner) {
            viewModel.setInitialCards()
        }

        viewModel.filterState.observe(viewLifecycleOwner) {
            viewModel.setInitialCards()
        }

        viewModel.sorterState.observe(viewLifecycleOwner) {
            viewModel.setInitialCards()
        }

        viewModel.initialCards.observe(viewLifecycleOwner) {
            binding.browserListCards.layoutManager = LinearLayoutManager(context)
            val adapter = DisplayCardsAdapter(it)
            binding.browserListCards.adapter = adapter
            initOnCardClickListener(adapter)
        }
    }

    /**
     * Initialize the search bar
     */
    private fun initSearchBar(browserViewModel: BrowserViewModel) {
        binding.browserSearchbar.isIconified = false
        binding.browserSearchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                browserViewModel.setSearchState(newText!!)
                return true
            }
        })
        //Close keyboard when the search bar is closed
        binding.browserSearchbar.setOnCloseListener {
            hideKeyboard(this)
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
                    (requireActivity() as MainActivity).changeFragment(R.id.nav_single_card, bundle)
                }
            }
    }


    /**
     * Initialize all the Listeners needed for the buttons that filter the cards
     */
    private fun initFilterButtonsListener() {

        binding.browserFilterButton.setOnClickListener {
            val currentState = binding.browserFilterBox.visibility
            if (currentState == View.VISIBLE) {
                binding.browserFilterBox.visibility = View.GONE
            } else {
                binding.browserSortBox.visibility = View.GONE
                binding.browserFilterBox.visibility = View.VISIBLE
            }
            hideKeyboard(this)
        }

        binding.browserFilterBySetButton.setOnClickListener {
            viewModel.setSetFilter(
                binding.browserFilterBySetEdittext.text.toString()
            )
            hideKeyboard(this)
        }

        binding.browserFilterByManaButton.setOnClickListener {
            val value = binding.browserFilterByManaEdittext.text.toString().toIntOrNull()
            if (value == null || value < 0) {
                viewModel.setManaFilter(null)
            } else {
                viewModel.setManaFilter(
                    value
                )
            }
            hideKeyboard(this)
        }

        binding.browserClearFilters.setOnClickListener {
            viewModel.clearFilters()
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                o1.name.compareTo(o2.name)
            }
            hideKeyboard(this)
        }
    }

    /**
     * Initialize all the Listeners needed for the buttons that sort the cards
     */
    private fun initSorterButtonsListener() {
        binding.browserSortButton.setOnClickListener {
            val currentState = binding.browserSortBox.visibility
            if (currentState == View.VISIBLE) {
                binding.browserSortBox.visibility = View.GONE
            } else {
                binding.browserFilterBox.visibility = View.GONE
                binding.browserSortBox.visibility = View.VISIBLE
            }
            hideKeyboard(this)
        }

        binding.browserSortByNameButton.setOnClickListener {
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                o1.name.compareTo(o2.name)
            }
        }

        binding.browserSortByManaButton.setOnClickListener {
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                o1.manaCost.compareTo(o2.manaCost)
            }
        }

        binding.browserSortByRarityButton.setOnClickListener {
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                o1.rarity.ordinal.compareTo(o2.rarity.ordinal)
            }
        }

        binding.browserSortBySetButton.setOnClickListener {
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                val result = o1.set.name.compareTo(o2.set.name)
                if (result == 0) {
                    o1.number.compareTo(o2.number)
                } else {
                    result
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
