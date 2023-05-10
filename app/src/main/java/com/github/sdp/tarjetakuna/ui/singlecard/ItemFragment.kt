package com.github.sdp.tarjetakuna.ui.singlecard

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.User

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private var ownedCards = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            ownedCards = it.getBoolean(ARG_OWNED_CARDS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        val users = generateUsers().filter {
            user -> user.cards.any {
                card -> card.possession == if (ownedCards) CardPossession.OWNED else CardPossession.WANTED
            }
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = UserRecyclerViewAdapter(users)
            }
        }
        return view
    }

    //TODO: Replace when database is OK
    private fun generateUsers(): List<User> {
        val fakeDbMagicCard1 = DBMagicCard("Aeronaut Tinkerer", CardPossession.OWNED, 3, "M15", 43)
        val fakeDbMagicCard2 = DBMagicCard("Aeronaut Tinkerer", CardPossession.WANTED, 3, "M15", 43)
        val fakeUser1 = User("kelvin.kappeler@epfl.ch", "keke", listOf(fakeDbMagicCard1), Coordinates(0.4f, 3.1f))
        val fakeUser2 = User("william.kwan@epfl.ch", "willi", listOf(fakeDbMagicCard2), Coordinates(0.4f, 3.6f))

        return listOf(fakeUser1, fakeUser2)
    }

    companion object {
        const val ARG_OWNED_CARDS = "owned_cards"

        @JvmStatic
        fun newInstance(ownedCards: Boolean) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_OWNED_CARDS, ownedCards)
                }
            }
    }
}
