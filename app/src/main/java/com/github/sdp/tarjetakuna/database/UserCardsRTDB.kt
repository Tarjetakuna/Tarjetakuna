package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.MagicCard
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture


/**
 * This class is used to manage the user's collection of cards.
 */

class UserCardsRTDB {
    private val db = Firebase.database.reference
    private val user = Firebase.auth.currentUser
    private val userCardCollection = db.child(user!!.uid)

    /**
     * Adds a card to the user's collection.
     */
    fun addCardToCollection(card: MagicCard) {
        val cardUID = card.set.code + card.number
        val data = Gson().toJson(card)
        userCardCollection.child(cardUID)
            .setValue(data) //or child(cardUID).child(owned/wanted).setValue(data)
    }

    /**
     * Removes a card from the user's collection.
     */
    fun removeCardFromCollection(card: MagicCard) {
        val cardUID = card.set.code + card.number
        userCardCollection.child(cardUID).removeValue()
    }

    /**
     * Retrieves a card asynchronously from the database
     */
    fun getCardFromCollection(card: MagicCard): CompletableFuture<DataSnapshot> {
        val cardUID = card.set.code + card.number
        val future = CompletableFuture<DataSnapshot>()
        userCardCollection.child(cardUID).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("card $cardUID not found in collection"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Checks if the user's collection contains a specific card in the given field.
     * @param magicCard The card to be checked.
     * @param field The emplacement of the card in the database.
     * @return True if the card is in the collection, false otherwise.
     */
    fun containsCard(magicCard: MagicCard, field: String): Boolean {
        return false
    }
}
