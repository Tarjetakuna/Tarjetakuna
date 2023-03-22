package com.github.bjolidon.bootcamp.ui.singlecard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.databinding.FragmentSingleCardBinding
import com.github.bjolidon.bootcamp.model.MagicCard
import com.google.gson.Gson
import com.github.bjolidon.bootcamp.utils.CustomGlide

/**
 * This fragment is used to display a single card with some details.
 * To run this fragment, you need to pass a MagicCard as a json string in the arguments with the key "card".
 * @see MagicCard
 */
class SingleCardFragment: Fragment() {

    private var _binding: FragmentSingleCardBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n") //Because of the concatenation of strings in singleCardTextCardType.text
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
            binding.singleCardTextCardRarity.text = card.rarity.toString()
            binding.singleCardTextCardType.text = card.type.toString() +
                    if (card.subtypes.isNotEmpty()) getString(R.string.single_card_showing_type_subtypes, card.subtypes.joinToString(", ")) else ""
            //The picture from the public API has a certificate problem,
            // so we use a placeholder for now.
            CustomGlide.loadDrawable(this, "https://cards.scryfall.io/large/front/c/f/cfa00c0e-163d-4f59-b8b9-3ee9143d27bb.jpg?1674420138") {
                binding.singleCardImage.setImageDrawable(it)
            }
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
