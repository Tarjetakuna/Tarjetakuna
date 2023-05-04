package com.github.sdp.tarjetakuna.ui.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.databinding.FragmentBrowserApiBinding
import com.github.sdp.tarjetakuna.utils.Utils

class BrowserApiFragment : Fragment() {
    private var _binding: FragmentBrowserApiBinding? = null
    private lateinit var viewModel: BrowserApiViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[BrowserApiViewModel::class.java]
        _binding = FragmentBrowserApiBinding.inflate(inflater, container, false)


        // observe the apiResults and apiError LiveData
        viewModel.apiResults.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.apiResults.text = it
            }
        }
        viewModel.apiError.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.apiResults.text = getString(it.first, it.second)
            }
        }

        binding.apiCards.setOnClickListener {
            hideKeyboard()
            //binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getRandomCard(this.requireContext())
        }

        binding.apiCardsByName.setOnClickListener {
            hideKeyboard()
            //binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getCardsByName(this.requireContext(), binding.cardName.text.toString())
        }

        binding.apiCardsBySet.setOnClickListener {
            hideKeyboard()
            //binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getCardsBySet(this.requireContext(), binding.setIdForCards.text.toString())
        }

        return binding.root
    }

    private fun hideKeyboard() {
        Utils.hideKeyboard(this)
    }

}
