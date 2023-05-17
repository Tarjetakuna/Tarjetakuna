package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the user's data in the database.
 */
class UserRTDB(database: Database) { //Firebase.database.reference.child("users") //assumption: we check add ability in User

    private var db: DatabaseReference
    private var cardsRTDB: CardsRTDB

    init {
        this.db = database.usersTable()
        cardsRTDB = CardsRTDB(database)
    }

    /**
     * Adds a card to the users collection.
     */
    fun addCard(fbcard: DBMagicCard, userUID: String) {
        val cardUID = fbcard.getFbKey()
        val fbpossession = fbcard.possession.toString().lowercase()
        db.child(userUID).child(fbpossession).child(cardUID).setValue(1)
        cardsRTDB.addCardToGlobalCollection(fbcard)
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
     */ //TODO handle multiple cards and how this interacts with global collection
    fun removeCard(userUID: String, fbcard: DBMagicCard) {
        val fbPosession = fbcard.possession.toString().lowercase()
        val cardUID = fbcard.getFbKey()
        db.child(userUID).child(fbPosession).child(cardUID)
            .removeValue()
    }

    /**
     * Get the unique card code from the user's collection asynchronously from the database (based on possession category)
     */
    private fun getCardCodeFromUserCollection(
        userUID: String,
        cardUID: String,
        possession: CardPossession
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
    fun getCardFromUserPossession(
        userUID: String,
        setCode: String,
        setNumber: Int,
        possession: CardPossession
    ): CompletableFuture<DataSnapshot> {
        val cardUID = setCode + "_" + setNumber.toString()
        val future = CompletableFuture<DataSnapshot>()
        try {
            val cardCodeFuture = getCardCodeFromUserCollection(userUID, cardUID, possession)
            cardCodeFuture.thenCompose {
                cardsRTDB.getCardFromGlobalCollection(cardUID) //only get the card if it exists in the user's collection
            }.whenComplete { snapshot, exception ->
                if (exception != null) {
                    future.completeExceptionally(exception)
                } else {
                    future.complete(snapshot)
                }
            }
        } catch (ex: Exception) {
            future.completeExceptionally(ex)
        }
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

    /**
     * Get all cards from the user's collection asynchronously from the database
     */

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

    /**
     * Get all users from the database
     */
    fun getUsers(): List<User> {
        val users = mutableListOf<User>()
        db.get().addOnSuccessListener {
            for (user in it.children) {
                val uid = user.key.toString()
                val username = user.child("username").value.toString()

                val coordinates = if (user.child("location").child("lat").value == null || user.child("location").child("long").value == null) {
                    Coordinates(0.0, 0.0)
                } else {
                    Coordinates(
                        user.child("location").child("lat").value.toString().toDouble(),
                        user.child("location").child("long").value.toString().toDouble()
                    )
                }

                val cards = mutableListOf<DBMagicCard>()
                cards.addAll(cardsFromUser(uid, CardPossession.OWNED))
                cards.addAll(cardsFromUser(uid, CardPossession.WANTED))
                users.add(User(uid, username, cards, coordinates))
            }
        }
        return users
    }

    private fun cardsFromUser(userUID: String, possession: CardPossession): MutableList<DBMagicCard> {
        val future = CompletableFuture<DataSnapshot>()
        val cards = mutableListOf<DBMagicCard>()
        getAllCardCodesFromUserPossession(userUID, possession).thenApply { cardCode ->
            for (code in (cardCode.value as HashMap<*, *>).keys) {
                cardsRTDB.getCardFromGlobalCollection(code.toString()).thenApply { card ->
                    val dbCard = Gson().fromJson(card.value.toString(), DBMagicCard::class.java)
                    cards.add(dbCard.copy(possession = possession))
                    Log.d("cardsFromUser", "added card ${dbCard.copy(possession = possession)} to user $userUID")
                }
            }
        }
        Log.d("taille", cards.size.toString())
        return cards
    }


}
