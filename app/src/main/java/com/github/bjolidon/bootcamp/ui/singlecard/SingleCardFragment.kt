package com.github.bjolidon.bootcamp.ui.singlecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.databinding.FragmentSingleCardBinding
import com.github.bjolidon.bootcamp.model.MagicCard
import com.google.gson.Gson

class SingleCardFragment: Fragment() {

    private var _binding: FragmentSingleCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleCardBinding.inflate(inflater, container, false)

        try {
            val card = Gson().fromJson(arguments?.getString("card"), MagicCard::class.java)
            binding.singleCardTextCardName.text = card.name
            binding.singleCardTextCardSet.text = getString(R.string.single_card_showing_set, card.set.name, card.set.code)
            binding.singleCardTextCardNumber.text = getString(R.string.single_card_showing_number, card.number)
        } catch (e: Exception) {
            binding.singleCardTextCardName.text = getString(R.string.error_load_card)
        }

        Glide.with(this)
            .load("https://cards.scryfall.io/large/front/c/f/cfa00c0e-163d-4f59-b8b9-3ee9143d27bb.jpg?1674420138")
            .into(binding.singleCardImage)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
