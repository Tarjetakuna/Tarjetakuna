package com.github.sdp.tarjetakuna.ui.singleuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.ui.browser.DisplayCardsAdapter
import com.google.gson.Gson

/**
 * A fragment representing a list of Items.
 */
class CardListFragment : Fragment() {

    private var isOwned = true
    private var cards: List<DBMagicCard> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            isOwned = it.getBoolean(ARG_OWNED_CARDS)
            cards =
                Gson().fromJson(it.getString(ARG_DB_CARDS), Array<DBMagicCard>::class.java).toList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card_list, container, false)

        val cardsFiltered =
            cards.filter {
                if (isOwned)
                    it.possession == CardPossession.OWNED
                else it.possession == CardPossession.WANTED
            }.map { it.toMagicCard() }.toCollection(ArrayList())

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = DisplayCardsAdapter(cardsFiltered)
            }
        }
        return view
    }

    companion object {
        const val ARG_OWNED_CARDS = "isOwned"
        const val ARG_DB_CARDS = "dbCards"

        @JvmStatic
        fun newInstance(isOwned: Boolean, dbCards: ArrayList<DBMagicCard>) =
            CardListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_OWNED_CARDS, isOwned)
                    putString(ARG_DB_CARDS, Gson().toJson(dbCards))
                }
            }
    }
}