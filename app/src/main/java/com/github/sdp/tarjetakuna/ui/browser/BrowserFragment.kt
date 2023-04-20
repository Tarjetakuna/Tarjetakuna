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
        val adapter = DisplayCardsAdapter(browserViewModel.initialCards)
        binding.browserListCards.adapter = adapter

        initSearchBar(browserViewModel)
        initOnCardClickListener(adapter)

        return root
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
                val adapter = DisplayCardsAdapter(
                    browserViewModel.initialCards.filter { card ->
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
                    (requireActivity() as MainActivity).changeFragment(
                        R.id.nav_single_card,
                        bundle
                    )
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
