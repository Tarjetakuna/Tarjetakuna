package com.github.sdp.tarjetakuna.ui.singlecard

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.sdp.tarjetakuna.MainActivity
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.databinding.FragmentSingleCardBinding
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicCardType
import com.github.sdp.tarjetakuna.utils.CustomGlide
import com.google.android.material.tabs.TabLayoutMediator
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
        _binding = FragmentSingleCardBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SingleCardViewModel::class.java]

        UserRTDB(FirebaseDB()).getUsers().thenApply {
            Log.d("BLABLA", it.toString())
        }

        loadCardFromJson()
        setUsersTabs()
        //Initialize the local database
        viewModel.localDatabase = LocalDatabaseProvider.setDatabase(
            requireContext(),
            LocalDatabaseProvider.CARDS_DATABASE_NAME
        )

        binding.singleCardSetText.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("set", Gson().toJson(viewModel.card?.set))
            //TODO : Should be changed to remove the dependency on MainActivity
            (requireActivity() as MainActivity).changeFragment(R.id.nav_single_set, bundle)
        }

        viewModel.checkUserConnected()
        viewModel.isConnected.observe(viewLifecycleOwner) {
            displayButton(it)
            viewModel.checkCardInCollection()
        }

        binding.singleCardAddCardButton.setOnClickListener {
            viewModel.addCardToOwnedCollection()
        }

        binding.singleCardRemoveCardButton.setOnClickListener {
            viewModel.removeCardFromOwnedCollection()
        }

        binding.singleCardAddWantedButton.setOnClickListener {
            viewModel.manageWantedCollection()
        }

        viewModel.currentQuantity.observe(viewLifecycleOwner) {
            binding.singleCardQuantityText.text = it.toString()
        }

        viewModel.buttonWantedText.observe(viewLifecycleOwner) {
            if (it) {
                binding.singleCardAddWantedButton.text =
                    getString(R.string.single_card_showing_add_to_wanted)
            } else {
                binding.singleCardAddWantedButton.text =
                    getString(R.string.single_card_showing_remove_wanted)
            }
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
            binding.singleCardCardNameText.text = card.name
            val spannableSet = SpannableString(
                getString(
                    R.string.single_card_showing_set,
                    card.set.name,
                    card.set.code
                )
            )
            spannableSet.setSpan(UnderlineSpan(), 0, spannableSet.length, 0)
            binding.singleCardSetText.text = spannableSet
            binding.singleCardCardNumberText.text =
                getString(R.string.single_card_showing_number, card.number)
            binding.singleCardCardTextText.text = card.text
            binding.singleCardRarityText.text = card.rarity.toString()
            binding.singleCardManaCostText.text =
                getString(R.string.single_card_showing_mana_cost, card.convertedManaCost.toString())

            val powerToughnessStr = if (card.type == MagicCardType.CREATURE) " " + getString(
                R.string.single_card_showing_stats,
                card.power,
                card.toughness
            ) else ""
            val subtypesStr = if (card.subtypes.isNotEmpty()) " " + getString(
                R.string.single_card_showing_subtypes,
                card.subtypes.joinToString(", ")
            ) else ""
            binding.singleCardTypeSubtypeStatsText.text = getString(
                R.string.single_card_showing_type_subtypes_stats,
                card.type.toString(),
                powerToughnessStr,
                subtypesStr
            )

            binding.singleCardArtistText.text =
                getString(R.string.single_card_showing_artist, card.artist)

            CustomGlide.loadDrawable(
                this,
                card.imageUrl
            ) {
                binding.singleCardImage.setImageDrawable(it)
            }
        } catch (e: Exception) {
            binding.singleCardCardNameText.text = getString(R.string.error_load_card)
        }
    }

    /**
     * Sets the tabs to display the users that have the card in their collection or wanted cards.
     */
    private fun setUsersTabs() {
        binding.singleCardViewPager.adapter = ItemFragmentAdapter(requireActivity())
        TabLayoutMediator(
            binding.singleCardTabLayout,
            binding.singleCardViewPager
        ) { tab, position ->
            tab.text =
                if (position == 0) getString(R.string.single_card_users_have) else getString(R.string.single_card_users_want)
            tab.id = position
        }.attach()

    }

    /**
     * Displays the buttons to add the card to the deck or to the wanted cards if the user is connected.
     * @param userIsConnected true if the user is connected, false otherwise.
     */
    private fun displayButton(userIsConnected: Boolean) {
        if (userIsConnected) {
            binding.singleCardAddCardButton.visibility = View.VISIBLE
            binding.singleCardRemoveCardButton.visibility = View.VISIBLE
            binding.singleCardAddWantedButton.visibility = View.VISIBLE
            binding.singleCardQuantityText.visibility = View.VISIBLE
            binding.singleCardAskConnectionText.visibility = View.GONE
        } else {
            binding.singleCardAddCardButton.visibility = View.GONE
            binding.singleCardRemoveCardButton.visibility = View.GONE
            binding.singleCardAddWantedButton.visibility = View.GONE
            binding.singleCardQuantityText.visibility = View.GONE
            binding.singleCardAskConnectionText.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ItemFragmentAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return UsersFragment.newInstance(
                position == 0,
                Gson().toJson(viewModel.card?.toDBMagicCard())
            )
        }
    }
}
