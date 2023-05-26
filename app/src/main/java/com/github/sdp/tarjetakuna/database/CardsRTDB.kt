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
    fun addCardToGlobalCollection(fbCard: DBMagicCard): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val cardUID = fbCard.getFbKey()
        val newCard =
            fbCard.clearPossession() //clear the possession when storing in global collection
        val data = Gson().toJson(newCard)
        db.child(cardUID).setValue(data).addOnSuccessListener {
            future.complete(true)
        }.addOnFailureListener {
            future.complete(false)
        }
        return future
    }

    /**
     * Add a list of cards to the global collection.
     */
    fun addMultipleCardsToGlobalCollection(fbCards: List<DBMagicCard>): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val listOfFutures = mutableListOf<CompletableFuture<Boolean>>()
        for (fbCard in fbCards) {
            val isCardAdded = addCardToGlobalCollection(fbCard)
            listOfFutures.add(isCardAdded)
        }
        CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenAccept {
            future.complete(true)
        }.exceptionally {
            future.complete(false)
            null
        }
        return future
    }

    /**
     * Remove a card from the global collection.
     */
    fun removeCardFromGlobalCollection(cardUID: String): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        db.child(cardUID).removeValue().addOnSuccessListener {
            future.complete(true)
        }.addOnFailureListener {
            future.complete(false)
        }
        return future
    }

    /**
     * Remove a list of cards from the global collection.
     */
    fun removeMultipleCardsFromGlobalCollection(cardUIDs: List<String>): CompletableFuture<Boolean> {
        val future = CompletableFuture<Boolean>()
        val listOfFutures = mutableListOf<CompletableFuture<Boolean>>()
        for (cardUID in cardUIDs) {
            val isRemoved = removeCardFromGlobalCollection(cardUID)
            listOfFutures.add(isRemoved)
        }

        CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenAccept {
            future.complete(true)
        }.exceptionally {
            future.complete(false)
            null
        }
        return future
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
}
