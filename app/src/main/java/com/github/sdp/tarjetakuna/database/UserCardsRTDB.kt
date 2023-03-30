package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
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
        userCardCollection.child(cardUID).setValue(data)
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
                future.completeExceptionally(NoSuchFieldException("card $cardUID is not in your collection"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }
}
