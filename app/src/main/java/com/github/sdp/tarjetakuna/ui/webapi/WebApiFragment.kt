package com.github.sdp.tarjetakuna.ui.webapi

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentWebApiBinding
import com.github.sdp.tarjetakuna.utils.Utils

class WebApiFragment : Fragment() {

    private var _binding: FragmentWebApiBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WebApiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[WebApiViewModel::class.java]
        _binding = FragmentWebApiBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        // set scrolling for the results textview
        binding.apiResults.movementMethod = ScrollingMovementMethod()

        // set the buttons listeners for the webapi
        binding.apiRandomCardButton.setOnClickListener {
            hideKeyboard()
            binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getRandomCard(this.requireContext())
        }

        binding.apiCardsByNameButton.setOnClickListener {
            hideKeyboard()
            binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getCardsByName(
                this.requireContext(),
                binding.apiCardNameEdittext.text.toString()
            )
        }

        binding.apiCardsBySetButton.setOnClickListener {
            hideKeyboard()
            binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getCardsBySet(this.requireContext(), binding.apiSetIdEdittext.text.toString())
        }

        binding.apiCardByIdButton.setOnClickListener {
            hideKeyboard()
            binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getCardById(this.requireContext(), binding.apiCardIdEdittext.text.toString())
        }

        binding.apiSetsButton.setOnClickListener {
            hideKeyboard()
            binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getSets(this.requireContext())
        }

        binding.apiSetByCodeButton.setOnClickListener {
            hideKeyboard()
            binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getSetByCode(
                this.requireContext(),
                binding.apiSetIdCodeEdittext.text.toString()
            )
        }

        return root
    }

    private fun hideKeyboard() {
        Utils.hideKeyboard(this)
    }


}
