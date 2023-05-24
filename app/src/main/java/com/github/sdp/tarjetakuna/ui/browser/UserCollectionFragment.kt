package com.github.sdp.tarjetakuna.ui.browser

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.databinding.FragmentUserCollectionBinding
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.utils.Utils.Companion.hideKeyboard
import com.google.gson.Gson


/**
 * Fragment that displays the cards in a recycler view
 */
class UserCollectionFragment(val possession: CardPossession = CardPossession.OWNED) : Fragment() {

    private var _binding: FragmentUserCollectionBinding? = null
    private lateinit var viewModel: UserCollectionViewModel
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(
            this,
            UserCollectionViewModelFactory(possession)
        )[UserCollectionViewModel::class.java]

        _binding = FragmentUserCollectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set the local database
        viewModel.localDatabase =
            LocalDatabaseProvider.setDatabase(
                requireContext(),
                LocalDatabaseProvider.CARDS_DATABASE_NAME
            )

        binding.userCollectionListCards.layoutManager = LinearLayoutManager(context)
        initObservers()
        initSearchBar(viewModel)
        initFilterButtonsListener()
        initSorterButtonsListener()

        // update the recycler view when the cards are retrieved from the database
        viewModel.getCardsFromDatabase()

        return root
    }

    override fun onResume() {
        Log.i("UserCollectionFragment", "onResume")
        super.onResume()
        viewModel.getCardsFromDatabase()
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
            binding.userCollectionListCards.layoutManager = LinearLayoutManager(context)
            val adapter = DisplayCardsAdapter(this, it)
            binding.userCollectionListCards.adapter = adapter
            initOnCardClickListener(adapter)
        }

        viewModel.cards.observe(viewLifecycleOwner) {
            val adapter = DisplayCardsAdapter(this, it)
            binding.userCollectionListCards.adapter = adapter
            initOnCardClickListener(adapter)
        }

    }

    /**
     * Initialize the search bar
     */
    private fun initSearchBar(userCollectionViewModel: UserCollectionViewModel) {
        binding.userCollectionSearchbar.isIconified = false
        binding.userCollectionSearchbar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userCollectionViewModel.setSearchState(newText!!)
                return true
            }
        })
        //Close keyboard when the search bar is closed
        binding.userCollectionSearchbar.setOnCloseListener {
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
                    bundle.putString(
                        "card",
                        Gson().toJson(adapter.cardsWithQuantities[position].first)
                    )
                    (requireActivity() as MainActivity).changeFragment(R.id.nav_single_card, bundle)
                }
            }
    }


    /**
     * Initialize all the Listeners needed for the buttons that filter the cards
     */
    private fun initFilterButtonsListener() {

        binding.userCollectionFilterButton.setOnClickListener {
            val currentState = binding.userCollectionFilterBox.visibility
            if (currentState == View.VISIBLE) {
                binding.userCollectionFilterBox.visibility = View.GONE
            } else {
                binding.userCollectionSortBox.visibility = View.GONE
                binding.userCollectionFilterBox.visibility = View.VISIBLE
            }
            hideKeyboard(this)
        }

        binding.userCollectionFilterBySetButton.setOnClickListener {
            viewModel.setSetFilter(
                binding.userCollectionFilterBySetEdittext.text.toString()
            )
            hideKeyboard(this)
        }

        binding.userCollectionFilterByManaButton.setOnClickListener {
            val value = binding.userCollectionFilterByManaEdittext.text.toString().toIntOrNull()
            if (value == null || value < 0) {
                viewModel.setManaFilter(null)
            } else {
                viewModel.setManaFilter(
                    value
                )
            }
            hideKeyboard(this)
        }

        binding.userCollectionClearFilters.setOnClickListener {
            viewModel.clearFilters()
            viewModel.setSorterState { o1: Pair<MagicCard, Int>, o2: Pair<MagicCard, Int> ->
                o1.first.name.compareTo(o2.first.name)
            }
            hideKeyboard(this)
        }
    }

    /**
     * Initialize all the Listeners needed for the buttons that sort the cards
     */
    private fun initSorterButtonsListener() {
        binding.userCollectionSortButton.setOnClickListener {
            val currentState = binding.userCollectionSortBox.visibility
            if (currentState == View.VISIBLE) {
                binding.userCollectionSortBox.visibility = View.GONE
            } else {
                binding.userCollectionFilterBox.visibility = View.GONE
                binding.userCollectionSortBox.visibility = View.VISIBLE
            }
            hideKeyboard(this)
        }

        binding.userCollectionSortByNameButton.setOnClickListener {
            viewModel.setSorterState { o1: Pair<MagicCard, Int>, o2: Pair<MagicCard, Int> ->
                o1.first.name.compareTo(o2.first.name)
            }
        }

        binding.userCollectionSortByManaButton.setOnClickListener {
            viewModel.setSorterState { o1: Pair<MagicCard, Int>, o2: Pair<MagicCard, Int> ->
                o1.first.convertedManaCost.compareTo(o2.first.convertedManaCost)
            }
        }

        binding.userCollectionSortByRarityButton.setOnClickListener {
            viewModel.setSorterState { o1: Pair<MagicCard, Int>, o2: Pair<MagicCard, Int> ->
                o1.first.rarity.ordinal.compareTo(o2.first.rarity.ordinal)
            }
        }

        binding.userCollectionSortBySetButton.setOnClickListener {
            viewModel.setSorterState { o1: Pair<MagicCard, Int>, o2: Pair<MagicCard, Int> ->
                val result = o1.first.set.name.compareTo(o2.first.set.name)
                if (result == 0) {
                    o1.first.number.compareTo(o2.first.number)
                } else {
                    result
                }
            }
        }
    }

    override fun onDestroyView() {
        Log.i("UserCollectionFragment", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

}

/**
 * Factory for the ViewModel
 */
class UserCollectionViewModelFactory(possession: CardPossession) :
    ViewModelProvider.Factory {
    private val viewModelPossession: CardPossession

    init {
        viewModelPossession = possession
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserCollectionViewModel(viewModelPossession) as T
    }

}
