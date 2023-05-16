package com.github.sdp.tarjetakuna.ui.singleuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.ui.browser.DisplayCardsAdapter

/**
 * A fragment representing a list of Items.
 */
class CardListFragment : Fragment() {

    private var isOwned = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            isOwned = it.getBoolean(ARG_OWNED_CARDS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = DisplayCardsAdapter(cards)
            }
        }
        return view
    }

    companion object {
        const val ARG_OWNED_CARDS = "isOwned"

        @JvmStatic
        fun newInstance(isOwned: Boolean) =
            CardListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_OWNED_CARDS, isOwned)
                }
            }
    }
}