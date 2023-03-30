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
    private val userCardCollection = if (user != null) db.child(user.uid) else null


    /**
     * Checks if the user is connected.
     */
    fun isConnected(): Boolean {
        return user != null
    }

    /**
     * Adds a card to the user's collection.
     */
    fun addCardToFirebase(card: MagicCard, nodeName: String) {
        val cardUID = card.set.code + card.number
        val data = Gson().toJson(card)
        userCardCollection?.child(nodeName)?.child(cardUID)
            ?.setValue(data) //or child(cardUID).child(owned/wanted).setValue(data)
    }

    /**
     * Removes a card from the user's collection.
     */
    fun removeCardFromFirebase(card: MagicCard, nodeName: String) {
        val cardUID = card.set.code + card.number
        userCardCollection?.child(nodeName)?.child(cardUID)?.removeValue()
    }

    /**
     * Retrieves a card asynchronously from the database
     */
    fun getCardFromFirebase(card: MagicCard, nodeName: String): CompletableFuture<DataSnapshot> {
        val cardUID = card.set.code + card.number
        val future = CompletableFuture<DataSnapshot>()
        userCardCollection?.child(nodeName)?.child(cardUID)?.get()?.addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("card $cardUID not found in collection"))
            } else {
                future.complete(it)
            }
        }?.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }
}
