package com.github.sdp.tarjetakuna.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the user's data in the database.
 */
class UserRTDB {
    //todo: switch to use auth adapter?
    private val db = Firebase.database.reference
    private val currentUser = Firebase.auth.currentUser
    private val cardsRTDB = CardsRTDB()

    private val user = if (currentUser != null) db.child("users").child(currentUser.uid) else null
    private val userCards = if (currentUser != null) user?.child("cards") else null
    private val usernames = db.child("usernames")

    private val userUsername = if (currentUser != null) user?.child("username") else null
    private val userOwnedCardCollection =
        if (currentUser != null) userCards?.child("owned") else null
    private val userWantedCardCollection =
        if (currentUser != null) userCards?.child("wanted") else null
    private val userTradeCardCollection =
        if (currentUser != null) userCards?.child("trade") else null
    private val userLocation = if (currentUser != null) user?.child("location") else null
    private val userChats = if (currentUser != null) user?.child("chats") else null

    /**
     * Checks if the user is connected.
     */
    fun isConnected(): Boolean {
        return currentUser != null
    }

    /**
     * Retrieves a user UID from their username asynchronously from the database.
     */
    private fun getUserUIDFromUserName(username: String): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        usernames.child(username).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("no username"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Retrieves a user from their username asynchronously from the database.
     */
    fun getUserFromUsername(username: String): CompletableFuture<DataSnapshot> {
        var uid = ""
        getUserUIDFromUserName(username).thenAccept {
            uid = it.value.toString() //string of other user's uid
        }.exceptionally { null }

        val future = CompletableFuture<DataSnapshot>()
        usernames.child(uid).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("no user"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Adds a card to the users "owned" collection.
     */
    fun addOwnedCard(card: DBMagicCard) {
        userOwnedCardCollection?.child(card.code + card.number)?.setValue(1) //todo:quantity
        cardsRTDB.addCardToGlobalCollection(card)
    }

    /**
     * Adds a card to the users "wanted" collection.
     */
    fun addWantedCard(card: DBMagicCard) {
        userWantedCardCollection?.child(card.code + card.number)?.setValue(1) //todo:quantity
        cardsRTDB.addCardToGlobalCollection(card)
    }

    /**
     * Adds a card to the users "trade" collection.
     */
    fun addTradeCard(card: DBMagicCard) {
        userTradeCardCollection?.child(card.code + card.number)?.setValue(1) //todo:quantity
        cardsRTDB.addCardToGlobalCollection(card)
    }

    //todo:remove method: verify the card doesn't exist in another user's collection, or user doesn't have multiple cards before removing from global collection

    /**
     * Get the unique card code from the user's collection asynchronously from the database (based on possession category)
     */
    private fun getCardCodeFromUserCollection(
        card: DBMagicCard,
        possession: CardPossession,
        userUID: String? = currentUser?.uid
    ): CompletableFuture<DataSnapshot> {
        //todo
        return CompletableFuture<DataSnapshot>()
    }

    /**
     * Get the card (json) from the user's collection asynchronously from the database (based on possession category)
     */
    fun getCardFromUserCollection(
        card: DBMagicCard,
        possession: CardPossession
    ): String {
        //todo
        return ""
    }

    /**
     * Get all cards (json) from the user's collection asynchronously from the database (based on possession category)
     */
    fun getAllCardsFromUserCollection(): CompletableFuture<DataSnapshot> {
        //todo
        //get each category, need to parse string to get card code
        //get each card from global collection from card code and add to list

        return CompletableFuture<DataSnapshot>()

    }

}
