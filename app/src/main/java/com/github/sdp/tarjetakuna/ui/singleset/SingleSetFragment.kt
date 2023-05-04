package com.github.sdp.tarjetakuna.ui.singleset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.ComponentRegistry
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
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
6
        return binding.root
    }

    /**
     * Loads the set from the json string passed in the arguments.
     */
    private fun loadSetFromJson() {
        try {
            val set = Gson().fromJson(arguments?.getString("set"), MagicSet::class.java)
            binding.singleSetSetName.text = set.name
            binding.singleSetSetCode.text = getString(R.string.single_set_code, set.code)
            binding.singleSetSetType.text = getString(R.string.single_set_type, set.type)
            binding.singleSetSetReleaseDate.text =
                getString(R.string.single_set_release_date, set.releaseDate.toString())

            ImageLoader.Builder(requireContext())
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
                .enqueue(ImageRequest.Builder(requireContext())
                    .data(set.iconUri)
                    .target(binding.singleSetSetImage)
                    .build())


        } catch (e: Exception) {
            binding.singleSetSetName.text = getString(R.string.single_set_error_loading)
            binding.singleSetSetCode.text = ""
            binding.singleSetSetType.text = ""
            binding.singleSetSetReleaseDate.text = ""
        }
    }
}
