package com.github.bjolidon.bootcamp.ui.singlecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.request.RequestOptions
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.databinding.FragmentSingleCardBinding
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.utils.GlideApp
import com.google.gson.Gson

/**
 * This fragment is used to display a single card with some details.
 * To run this fragment, you need to pass a MagicCard as a json string in the arguments with the key "card".
 * @see MagicCard
 */
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
            binding.singleCardTextCardText.text = card.text

            //The picture from the public API has a certificate problem,
            // so we use a placeholder for now.
            GlideApp.with(this)
                .asBitmap()
                .load("https://cards.scryfall.io/large/front/c/f/cfa00c0e-163d-4f59-b8b9-3ee9143d27bb.jpg?1674420138")
                .apply(RequestOptions().override(1100))
                .into(binding.singleCardImage)
        } catch (e: Exception) {
            binding.singleCardTextCardName.text = getString(R.string.error_load_card)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
