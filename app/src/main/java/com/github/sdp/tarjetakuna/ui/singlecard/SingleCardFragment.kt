package com.github.sdp.tarjetakuna.ui.singlecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentSingleCardBinding
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicType
import com.github.sdp.tarjetakuna.utils.CustomGlide
import com.google.gson.Gson

/**
 * This fragment is used to display a single card with some details.
 * To run this fragment, you need to pass a MagicCard as a json string in the arguments with the key "card".
 * @see MagicCard
 */
class SingleCardFragment : Fragment() {

    private var _binding: FragmentSingleCardBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SingleCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SingleCardViewModel::class.java]
        _binding = FragmentSingleCardBinding.inflate(inflater, container, false)

        loadCardFromJson()

        viewModel.checkUserConnected()
        viewModel.isConnected.observe(viewLifecycleOwner) {
            displayButton(it)
            viewModel.checkCardInCollection()
            viewModel.checkCardInWanted()
        }

        binding.singleCardButtonAddToDeck.setOnClickListener {
            viewModel.manageOwnedCollection()
        }

        viewModel.buttonText.observe(viewLifecycleOwner) {
            binding.singleCardButtonAddToDeck.text = it
        }

        binding.singleCardButtonAddToWanted.setOnClickListener {
            //TODO : Add the card to the wanted cards
            viewModel.addCardToWanted()
        }

        return binding.root
    }

    /**
     * Loads the card from the json string passed in the arguments.
     */
    private fun loadCardFromJson() {
        try {
            val card = Gson().fromJson(arguments?.getString("card"), MagicCard::class.java)
            // give the card to the viewModel
            viewModel.card = card

            // display the card
            binding.singleCardTextCardName.text = card.name
            binding.singleCardTextCardSet.text =
                getString(R.string.single_card_showing_set, card.set.name, card.set.code)
            binding.singleCardTextCardNumber.text =
                getString(R.string.single_card_showing_number, card.number)
            binding.singleCardTextCardText.text = card.text
            binding.singleCardTextCardRarity.text = card.rarity.toString()
            binding.singleCardTextCardManaCost.text =
                getString(R.string.single_card_showing_mana_cost, card.convertedManaCost.toString())

            val powerToughnessStr = if (card.type == MagicType.Creature) " " + getString(
                R.string.single_card_showing_stats,
                card.power,
                card.toughness
            ) else ""
            val subtypesStr = if (card.subtypes.isNotEmpty()) " " + getString(
                R.string.single_card_showing_subtypes,
                card.subtypes.joinToString(", ")
            ) else ""
            binding.singleCardTextCardTypeSubtypesStats.text = getString(
                R.string.single_card_showing_type_subtypes_stats,
                card.type.toString(),
                powerToughnessStr,
                subtypesStr
            )

            binding.singleCardTextCardArtist.text =
                getString(R.string.single_card_showing_artist, card.artist)

            //The picture from the public API has a certificate problem,
            // so we use a placeholder for now.
            CustomGlide.loadDrawable(
                this,
                "https://cards.scryfall.io/large/front/c/f/cfa00c0e-163d-4f59-b8b9-3ee9143d27bb.jpg?1674420138"
            ) {
                binding.singleCardImage.setImageDrawable(it)
            }
        } catch (e: Exception) {
            binding.singleCardTextCardName.text = getString(R.string.error_load_card)
        }
    }

    /**
     * Displays the buttons to add the card to the deck or to the wanted cards if the user is connected.
     * @param boolean true if the user is connected, false otherwise.
     */
    private fun displayButton(boolean: Boolean) {
        if (boolean) {
            binding.singleCardButtonAddToDeck.visibility = View.VISIBLE
            binding.singleCardButtonAddToWanted.visibility = View.VISIBLE
            binding.singleCardTextAskConnection.visibility = View.GONE
        } else {
            binding.singleCardButtonAddToDeck.visibility = View.GONE
            binding.singleCardButtonAddToWanted.visibility = View.GONE
            binding.singleCardTextAskConnection.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
