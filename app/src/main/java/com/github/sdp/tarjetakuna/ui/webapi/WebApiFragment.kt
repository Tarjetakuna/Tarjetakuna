package com.github.sdp.tarjetakuna.ui.webapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentWebApiBinding
import com.github.sdp.tarjetakuna.ui.browser.DisplayCardsAdapter
import com.github.sdp.tarjetakuna.utils.Utils
import com.google.gson.Gson

class WebApiFragment : Fragment() {

    private var _binding: FragmentWebApiBinding? = null
    private lateinit var viewModel: WebApiViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[WebApiViewModel::class.java]
        _binding = FragmentWebApiBinding.inflate(inflater, container, false)

        binding.webApiListCard.layoutManager = LinearLayoutManager(context)

        initObservers()
        initListener()

        return binding.root
    }

    /**
     * Initialize the different observers
     */
    private fun initObservers() {
        viewModel.cardList.observe(viewLifecycleOwner) {
            binding.webApiListCard.layoutManager = LinearLayoutManager(context)
            val adapter = DisplayCardsAdapter(this, it)
            binding.webApiListCard.adapter = adapter
            initOnCardClickListener(adapter)
        }
    }

    /**
     * Initialize the different listeners
     */
    private fun initListener() {
        binding.apiRandomCardButton.setOnClickListener {
            hideKeyboard()
            viewModel.getRandomCard(this.requireContext())
        }

        binding.apiCardsByNameButton.setOnClickListener {
            hideKeyboard()
            viewModel.getCardsByName(
                this.requireContext(),
                binding.apiCardNameEdittext.text.toString()
            )
        }

        binding.apiCardsBySetButton.setOnClickListener {
            hideKeyboard()
            viewModel.getCardsBySet(this.requireContext(), binding.apiSetIdEdittext.text.toString())
        }
    }

    /**
     * Initialize the on card click listener
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

    private fun hideKeyboard() {
        Utils.hideKeyboard(this)
    }

}
