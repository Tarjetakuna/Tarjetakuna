package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.google.firebase.database.DataSnapshot
import java.util.concurrent.CompletableFuture

/**
 * Represents a user.
 */
data class User(
    var uid: String,
    val username: String,
    var cards: MutableList<DBMagicCard>,
    var location: Coordinates
    //var chats: List<Chat>

) {
    init {
        require(
            username.matches(
                Regex(
                    "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*\$"
                )
            )
        ) { "username (email) is not valid" }
        require(
            uid.isNotBlank()
        ) { "UID is not valid" }
    }

    private val db = FirebaseDB.returnDatabaseReference()
    private val userRTDB = UserRTDB(FirebaseDB.userTable())

    /**
     * Retrieves a card under a given possession asynchronously from the database
     */
    fun getCard(card: MagicCard, possession: CardPossession): CompletableFuture<DataSnapshot> {
        return userRTDB.getCardFromUserPossession(uid, card.toDBMagicCard(possession))
    }

    /**
     * Retrieves all cards under a given possession asynchronously from the database
     */
    fun getAllCards(possession: CardPossession): List<CompletableFuture<DataSnapshot>> { //you call these functions with the User object (of other user)
        return userRTDB.getAllCardsFromUserPossession(uid, possession)
    }


    /**
     * Adds a card to the user's collection with the given possession.
     */
    fun addCard(card: MagicCard, possession: CardPossession) {
        val fbCard = card.toDBMagicCard(possession)
        cards.add(fbCard)
        userRTDB.addCard(fbCard, uid)
    }

    /**
     * Adds a list of cards to the user's collection with the given possessions.
     */
    fun addMultipleCards(cards: List<MagicCard>, possession: List<CardPossession>) {
        val cardsWithPossession = cards.zip(possession)
        for ((card, pos) in cardsWithPossession) {
            addCard(card, pos)
        }
    }

    /**
     * Removes a card from the user's collection with the given possession.
     */
    fun removeCard(card: MagicCard, possession: CardPossession) {
        userRTDB.removeCard(uid, card.toDBMagicCard(possession))
    }

}
