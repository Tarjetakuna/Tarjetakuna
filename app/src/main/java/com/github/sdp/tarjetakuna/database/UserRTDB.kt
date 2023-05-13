package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the user's data in the database.
 */
class UserRTDB(db: DatabaseReference) { //Firebase.database.reference.child("users") //assumption: we check add ability in User

    private var db: DatabaseReference
    private val cardsRTDB = CardsRTDB(db.child("cards"))

    init {
        this.db = db
    }

    /**
     * Adds a card to the users collection.
     */
    fun addCard(card: DBMagicCard, userUID: String) {
        val cardUID = card.code + card.number
        val fbpossession = card.possession.toString().lowercase()
        db.child(userUID).child(fbpossession).child(cardUID).setValue(1)
        cardsRTDB.addCardToGlobalCollection(card)
    }

    /**
     * Adds a list of cards to the users collection.
     */
    fun addMultipleCards(cards: List<DBMagicCard>, userUID: String, possession: CardPossession) {
        for (card in cards) {
            addCard(card, userUID)
        }
    }

    /**
     * Removes a card from the users collection.
     */ //todo: handle multiple cards and how this interacts with global collection
    fun removeCard(card: DBMagicCard, userUID: String) {
        val fbPosession = card.possession.toString().lowercase()
        val cardUID = card.code + card.number
        db.child(userUID).child(fbPosession).child(cardUID)
            .removeValue()
    }

    /**
     * Get the unique card code from the user's collection asynchronously from the database (based on possession category)
     */
    private fun getCardCodeFromUserCollection(
        possession: CardPossession,
        userUID: String,
        cardUID: String
    ): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        db.child(userUID).child(possession.toString().lowercase()).child(cardUID).get()
            .addOnSuccessListener {
                if (it.value == null) {
                    future.completeExceptionally(NoSuchFieldException("card $cardUID is not in user collection"))
                } else {
                    future.complete(it)
                }
            }.addOnFailureListener {
                future.completeExceptionally(it)
            }
        return future
    }

    /**
     * Get the card (json) from the user's collection asynchronously from the database (based on possession category)
     */
    //@RequiresApi(Build.VERSION_CODES.S)//for exceptionally
    fun getCardFromUserPossession(
        fbcard: DBMagicCard,
        userUID: String
    ): CompletableFuture<DataSnapshot> {
        val cardUID = fbcard.code + fbcard.number
        var future = CompletableFuture<DataSnapshot>()
        getCardCodeFromUserCollection(fbcard.possession, userUID, cardUID).thenAccept {
            future =
                cardsRTDB.getCardFromGlobalCollection(cardUID) //only get the card if the user has it in their collection
        }//todo: not sure how to handle exception case (needs certain api level), is it ok to just not handle this ?
//            .exceptionally { exp = CompletableFuture.failedFuture<DataSnapshot>(it.cause)
//            null}
        return future
    }

    /**
     * Get all card codes from the user's collection asynchronously from the database (based on possession category)
     */
    private fun getAllCardCodesFromUserPossession(
        userUID: String,
        possession: CardPossession
    ): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        db.child(userUID).child(possession.toString().lowercase()).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("no cards in collection"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Get all cards from the user's collection asynchronously from the database (based on possession category)
     */
    fun getAllCardsFromUserPossession(
        userUID: String,
        possession: CardPossession
    ): List<CompletableFuture<DataSnapshot>> {
        val cards = mutableListOf<CompletableFuture<DataSnapshot>>()
        getAllCardCodesFromUserPossession(userUID, possession).thenAccept { ds ->
            for (card in ds.children) {
                val cardUID = card.key.toString().uppercase()
                cardsRTDB.getCardFromGlobalCollection(cardUID).let { cards.add(it) }
            }
        }
        return cards
    }

    fun getAllCardsFromUser(userUID: String): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        db.child(userUID).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("no cards in user collection"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }


}
