package com.github.sdp.tarjetakuna.ui.singlecard

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
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.User
import com.google.gson.Gson

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
            dbMagicCard = try {
                Gson().fromJson(it.getString(ARG_CARD), DBMagicCard::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        if (dbMagicCard == null) return view

        val users = generateUsers().filter { user ->
            user.cards.any { card ->
                (card.possession == if (ownedCards) CardPossession.OWNED else CardPossession.WANTED)
                        && (card.code == dbMagicCard!!.code)
                        && (card.number == dbMagicCard!!.number)
            } && user.email != currentUser().email
        }.sortedBy { user ->
            user.location.distanceKmTo(currentUser().location)
        }

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = UserRecyclerViewAdapter(users, currentUser())
            }
        }
        return view
    }

    //TODO: Replace / Remove when database is OK
    private fun currentUser(): User {
        return User(
            "kelvin.kappeler@epfl.ch",
            "keke",
            listOf(DBMagicCard("Aeronaut Tinkerer", CardPossession.OWNED, 3, "M15", 43)),
            Coordinates(0.4f, 3.1f)
        )
    }


    //TODO: Replace when database is OK
    private fun generateUsers(): List<User> {
        val fakeDbMagicCard1 = DBMagicCard("Aeronaut Tinkerer", CardPossession.WANTED, 3, "M15", 43)
        val fakeDbMagicCard2 = DBMagicCard("Aeronaut Tinkerer", CardPossession.OWNED, 3, "M15", 43)
        val fakeDbMagicCard3 = DBMagicCard("Blabla Woaw", CardPossession.OWNED, 3, "M15", 21)
        val fakeUser1 = User(
            "kelvin.kappeler@epfl.ch",
            "keke",
            listOf(fakeDbMagicCard1),
            Coordinates(0.4f, 3.1f)
        )
        val fakeUser2 =
            User("william.kwan@epfl.ch", "willi", listOf(fakeDbMagicCard2), Coordinates(0.4f, 3.6f))

        val fakeUser3 =
            User(
                "bastien.jolidon@epfl.ch",
                "bibi",
                listOf(fakeDbMagicCard2, fakeDbMagicCard3),
                Coordinates(43.4f, -8.6f)
            )

        return listOf(fakeUser1, fakeUser2, fakeUser3)
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