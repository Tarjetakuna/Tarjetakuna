package com.github.sdp.tarjetakuna.ui.singleset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentSingleSetBinding
import com.github.sdp.tarjetakuna.model.MagicSet
import com.google.gson.Gson

/**
 * Fragment which displays a single set
 */
class SingleSetFragment : Fragment() {

    private var _binding: FragmentSingleSetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleSetBinding.inflate(inflater, container, false)

        loadSetFromJson()

        return binding.root
    }

    /**
     * Loads the set from the json string passed in the arguments.
     */
    private fun loadSetFromJson() {
        try {
            val set = Gson().fromJson(arguments?.getString("set"), MagicSet::class.java)
            binding.singleSetSetNameText.text = set.name
            binding.singleSetSetCodeText.text = getString(R.string.single_set_code, set.code)
            binding.singleSetSetTypeText.text = getString(R.string.single_set_type, set.type)
            binding.singleSetSetBlockText.text = getString(R.string.single_set_block, set.block)
            binding.singleSetSetReleaseDateText.text =
                getString(R.string.single_set_release_date, set.releaseDate.toString())

        } catch (e: Exception) {
            binding.singleSetSetNameText.text = getString(R.string.single_set_error_loading)
            binding.singleSetSetCodeText.text = ""
            binding.singleSetSetTypeText.text = ""
            binding.singleSetSetBlockText.text = ""
            binding.singleSetSetReleaseDateText.text = ""
        }
    }
}
