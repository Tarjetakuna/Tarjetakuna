package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

/**
 * Represents a user.
 */
data class User(
    var uid: String,
    val username: String,
    var cards: MutableList<DBMagicCard>,
    var location: Coordinates,
    val database: Database = FirebaseDB(Firebase.database.reference)
    //var chats: List<Chat>

) {
    private var db: DatabaseReference
    private var userRTDB: UserRTDB

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

        db = database.returnDatabaseReference()
        userRTDB = UserRTDB(database)
    }

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
