package com.github.sdp.tarjetakuna.ui.singlecard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicCardType
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicRarity
import com.github.sdp.tarjetakuna.model.MagicSet
import com.github.sdp.tarjetakuna.model.User
import com.github.sdp.tarjetakuna.ui.authentication.GoogleAuthAdapter
import com.google.gson.Gson
import java.time.LocalDate

/**
 * A fragment representing a list of [User].
 */
class UsersFragment : Fragment() {

    private var ownedCards = true
    private var dbMagicCard: DBMagicCard? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            ownedCards = it.getBoolean(ARG_IS_SHOWING_OWNED_CARDS)
            dbMagicCard = Gson().fromJson(it.getString(ARG_CARD), DBMagicCard::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        if (dbMagicCard == null) return view

        val userRTDB = UserRTDB(FirebaseDB())
        var currentUser: User? = null
        val firebaseUser = GoogleAuthAdapter.currentUser
        userRTDB.getUserByUsername(firebaseUser?.email ?: "").thenAccept {
            currentUser = it
        }

        userRTDB.getUsers().thenApply {
            val users = it.filter { user ->
                user.cards.any { card ->
                    (card.possession == if (ownedCards) CardPossession.OWNED else CardPossession.WANTED)
                            && (card.code == dbMagicCard!!.code)
                            && (card.number == dbMagicCard!!.number)
                } && user.username != (currentUser?.username ?: "")
            }.sortedBy { user -> if (currentUser == null) 0.0 else user.location.distanceKmTo(
                currentUser!!.location) }
            with(view as RecyclerView) {
                layoutManager = LinearLayoutManager(context)
                adapter = UserRecyclerViewAdapter(users, currentUser)
            }
        }

        return view
    }

    companion object {
        const val ARG_IS_SHOWING_OWNED_CARDS = "owned_cards"
        const val ARG_CARD = "card"

        @JvmStatic
        fun newInstance(ownedCards: Boolean, dbMagicCardJson: String) =
            UsersFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_SHOWING_OWNED_CARDS, ownedCards)
                    putString(ARG_CARD, dbMagicCardJson)
                }
            }
    }
}
