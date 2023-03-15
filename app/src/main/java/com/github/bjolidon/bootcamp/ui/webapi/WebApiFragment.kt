package com.github.bjolidon.bootcamp.ui.webapi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.databinding.FragmentScannerBinding
import com.github.bjolidon.bootcamp.databinding.FragmentWebApiBinding

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

        viewModel.apiResults.observe(viewLifecycleOwner) {
            if (it != null){
                binding.apiResults.text = it
            }
        }
        viewModel.apiError.observe(viewLifecycleOwner) {
            if (it != null){
                binding.apiResults.text = getString(it.first, it.second)
            }
        }

        binding.apiCards.setOnClickListener {
            binding.apiResults.text = getString(R.string.api_waiting_results)
            viewModel.getCards(this.requireContext())
        }

        return root
    }
}