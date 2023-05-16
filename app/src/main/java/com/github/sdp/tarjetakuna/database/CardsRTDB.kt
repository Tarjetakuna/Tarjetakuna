package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the global collection of cards contained by all users (no duplicates).
 */
class CardsRTDB(database: Database) {

    private var db: DatabaseReference

    init {
        this.db = database.cardsTable() //Firebase.database.reference.child("cards")
    }

    /**
     * Add a card to the global card collection.
     */
    fun addCardToGlobalCollection(fbCard: DBMagicCard) {
        val cardUID = fbCard.getFbKey()
        val newCard =
            fbCard.clearPossession() //clear the possession when storing in global collection
        val data = Gson().toJson(newCard)
        db.child(cardUID).setValue(data)
    }

    /**
     * Add a list of cards to the global collection.
     */
    fun addMultipleCardsToGlobalCollection(fbCards: List<DBMagicCard>) {
        for (fbCard in fbCards) {
            addCardToGlobalCollection(fbCard)
        }
    }

    /**
     * Remove a card from the global collection.
     */
    fun removeCardFromGlobalCollection(cardUID: String) {
        db.child(cardUID).removeValue()
    }

    /**
     * Remove a list of cards from the global collection.
     */
    fun removeMultipleCardsFromGlobalCollection(cardUIDs: List<String>) {
        for (cardUID in cardUIDs) {
            removeCardFromGlobalCollection(cardUID)
        }
    }

    /**
     * Retrieves a card asynchronously from the database
     * The card is identified by only its set code and its number
     */
    fun getCardFromGlobalCollection(
        cardUID: String
    ): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        db.child(cardUID).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("card $cardUID is not in global collection"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Retrieves a list of cards asynchronously from the database
     */
    fun getMultipleCardsFromGlobalCollection(cardUIDs: List<String>): List<CompletableFuture<DataSnapshot>> {
        val cards = mutableListOf(CompletableFuture<DataSnapshot>())
        for (cardUID in cardUIDs) {
            getCardFromGlobalCollection(cardUID).let { cards.add(it) }
        }
        return cards
    }

    /**
     * Retrieve all the cards asynchronously from the database
     */
    fun getAllCardsFromGlobalCollection(): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        db.get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("no cards in global collection"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }
    // }
}
