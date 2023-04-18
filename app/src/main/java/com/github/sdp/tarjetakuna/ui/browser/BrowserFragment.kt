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
import com.github.sdp.tarjetakuna.databinding.FragmentBrowserBinding
import com.github.sdp.tarjetakuna.model.MagicCard
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
            hideKeyboard()
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


    /**
     * Initialize all the Listeners needed for the buttons that filter the cards
     */
    private fun initFilterButtonsListener() {

        binding.browserFilterButton.setOnClickListener {
            val currentState = binding.filterBox.visibility
            if (currentState == View.VISIBLE) {
                binding.filterBox.visibility = View.GONE
            } else {
                binding.sortBox.visibility = View.GONE
                binding.filterBox.visibility = View.VISIBLE
            }
            hideKeyboard()
        }

        binding.filterBySetButton.setOnClickListener {
            viewModel.setFilterState(
                FilterState(
                    FilterType.SET,
                    binding.filterBySetEdittext.text.toString()
                )
            )
            hideKeyboard()
        }

        binding.filterByManaButton.setOnClickListener {
            val value = binding.filterByManaEdittext.text.toString().toIntOrNull()
            if (value == null || value < 0) {
                viewModel.setFilterState(FilterState(FilterType.NONE, ""))
            } else {
                viewModel.setFilterState(
                    FilterState(
                        FilterType.MANA,
                        binding.filterByManaEdittext.text.toString()
                    )
                )
            }
            hideKeyboard()
        }

        binding.clearFilters.setOnClickListener {
            viewModel.setFilterState(FilterState(FilterType.NONE, ""))
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                o1.name.compareTo(o2.name)
            }
            hideKeyboard()
        }
    }

    /**
     * Initialize all the Listeners needed for the buttons that sort the cards
     */
    private fun initSorterButtonsListener() {
        binding.browserSortButton.setOnClickListener {
            val currentState = binding.sortBox.visibility
            if (currentState == View.VISIBLE) {
                binding.sortBox.visibility = View.GONE
            } else {
                binding.filterBox.visibility = View.GONE
                binding.sortBox.visibility = View.VISIBLE
            }
            hideKeyboard()
        }

        binding.sortByNameButton.setOnClickListener {
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                o1.name.compareTo(o2.name)
            }
        }

        binding.sortByManaButton.setOnClickListener {
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                o1.manaCost.compareTo(o2.manaCost)
            }
        }

        binding.sortByRarityButton.setOnClickListener {
            viewModel.setSorterState { o1: MagicCard, o2: MagicCard ->
                o1.rarity.ordinal.compareTo(o2.rarity.ordinal)
            }
        }

        binding.sortBySetButton.setOnClickListener {
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

    private fun hideKeyboard() {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(binding.browserSearchbar.windowToken, 0)
    }

}
