package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the global collection of cards contained by all users (no duplicates).
 */

class CardsRTDB {
    private val db = Firebase.database.reference
    private val cards = db.child("cards")

    /**
     * Add a card to the global card collection.
     */
    fun addCardToGlobalCollection(fbCard: DBMagicCard) {
        val cardUID = fbCard.code + fbCard.number
        val data = Gson().toJson(fbCard)
        cards.child(cardUID).setValue(data)
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
        cards.child(cardUID).removeValue()
    }

    /**
     * Retrieves a card asynchronously from the database
     * The card is identified by only its set code and its number
     */
    fun getCardFromGlobalCollection(
        cardUID: String
    ): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        cards.child(cardUID).get().addOnSuccessListener {
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
     * Retrieve all the cards asynchronously from the database
     */
    fun getAllCardsFromCollection(): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        cards.get().addOnSuccessListener {
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
