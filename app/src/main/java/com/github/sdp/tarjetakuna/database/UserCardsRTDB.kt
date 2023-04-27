package com.github.sdp.tarjetakuna.database

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
    fun addCardToCollection(fbCard: DBMagicCard) {
        val cardUID = fbCard.code + fbCard.number
        val data = Gson().toJson(fbCard)
        userCardCollection?.child(cardUID)?.setValue(data)
    }

    /**
     * Adds a list of cards to the user's collection.
     */
    fun addCardsToCollection(fbCards: List<DBMagicCard>) {
        for (fbCard in fbCards) {
            addCardToCollection(fbCard)
        }
    }

    /**
     * Removes a card from the user's collection.
     */
    fun removeCardFromCollection(fbCard: DBMagicCard) {
        val cardUID = fbCard.code + fbCard.number
        userCardCollection?.child(cardUID)?.removeValue()
    }

    /**
     * Retrieves a card asynchronously from the database
     * The card is identified by only its set code and its number
     */
    fun getCardFromCollection(fbCard: DBMagicCard): CompletableFuture<DataSnapshot> {
        val cardUID = fbCard.code + fbCard.number
        val future = CompletableFuture<DataSnapshot>()
        userCardCollection?.child(cardUID)?.get()?.addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("card $cardUID is not in your collection"))
            } else {
                future.complete(it)
            }
        }?.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Retrieves all the cards asynchronously from the database
     */
    fun getAllCardsFromCollection(): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        userCardCollection?.get()?.addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("you don't have any card in your collection"))
            } else {
                future.complete(it)
            }
        }?.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }
}
