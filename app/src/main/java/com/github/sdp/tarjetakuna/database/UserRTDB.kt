package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.Coordinates
import com.google.firebase.database.*
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
     * Adds a card to the user's collection.
     * Returns true if the operation succeeded, false otherwise.
     */
    fun addCard(fbcard: DBMagicCard, userUID: String): CompletableFuture<Boolean> {
        val cardUID = fbcard.getFbKey()
        val fbpossession = fbcard.possession.toString().lowercase()
        val cardRef = db.child(userUID).child(fbpossession).child(cardUID).child("quantity")

        cardsRTDB.addCardToGlobalCollection(fbcard)

        val completableFuture = CompletableFuture<Boolean>()

        try {
            cardRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val currentValue = currentData.getValue(Int::class.java)
                    if (currentValue != null) {
                        // Value exists, increment it
                        currentData.value = currentValue + 1
                    } else {
                        // Value doesn't exist, set it to 1
                        currentData.value = 1
                    }
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (error != null || !committed) {
                        cardRef.setValue(ServerValue.increment(0))  // Rollback the transaction
                        completableFuture.complete(false) // Card addition failed
                    } else {
                        completableFuture.complete(true) // Card addition succeeded
                    }
                }
            })
        } catch (e: Exception) {
            completableFuture.completeExceptionally(e) // Complete exceptionally if an exception occurs
        }

        return completableFuture
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
     */
    fun removeCard(userUID: String, fbcard: DBMagicCard): CompletableFuture<Boolean> {
        val cardUID = fbcard.getFbKey()
        val fbpossession = fbcard.possession.toString().lowercase()
        val cardRef = db.child(userUID).child(fbpossession).child(cardUID).child("quantity")

        val completableFuture = CompletableFuture<Boolean>()

        try {
            cardRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val currentValue = currentData.getValue(Int::class.java)
                    if (currentValue != null) {
                        // Value exists, decrement it
                        if (currentData.value != 0) {
                            currentData.value = currentValue - 1
                        }
                    } else {
                        // Value doesn't exist, set it to 0
                        currentData.value = 0
                    }
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (error != null || !committed) {
                        cardRef.setValue(ServerValue.increment(0))  // Rollback the transaction
                        completableFuture.complete(false) // Card removal failed
                    } else {
                        completableFuture.complete(true) // Card removal succeeded
                    }
                }
            })
        } catch (e: Exception) {
            completableFuture.completeExceptionally(e) // Complete exceptionally if an exception occurs
        }

        return completableFuture
    }

    /**
     * Removes a list of cards from the users collection.
     */
    fun removeAllCopyOfCard(userUID: String, fbcard: DBMagicCard) {
        val cardUID = fbcard.getFbKey()
        val fbpossession = fbcard.possession.toString().lowercase()
        val cardRef = db.child(userUID).child(fbpossession).child(cardUID)
        cardRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                currentData.value = 0
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null || !committed) {
                    cardRef.setValue(ServerValue.increment(0))  // Rollback the transaction
                }
            }
        })
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
     * Push the location of the user to the database.
     */
    fun pushUserLocation(userUID: String, location: Coordinates) {
        db.child(userUID).child("location").child("lat").setValue(location.latitude)
        db.child(userUID).child("location").child("long").setValue(location.longitude)
    }

    /**
     * Get the location of the user from the database.
     */
    fun getUserLocation(userUID: String): CompletableFuture<Coordinates> {
        val future = CompletableFuture<Coordinates>()
        db.child(userUID).child("location").get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("no location for user"))
            } else {
                val lat = it.child("lat").value.toString().toDouble()
                val long = it.child("long").value.toString().toDouble()
                future.complete(Coordinates(lat, long))
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    fun cardsFromUser(
        userUID: String,
        possession: CardPossession
    ): CompletableFuture<MutableList<DBMagicCard>> {
        val future = CompletableFuture<MutableList<DBMagicCard>>()
        val cards = mutableListOf<DBMagicCard>()

        getAllCardCodesFromUserPossession(userUID, possession).thenApply { cardCode ->
            val cardCodeMap = cardCode.value as HashMap<*, *>
            val cardFutures = mutableListOf<CompletableFuture<DBMagicCard>>()

            for (code in cardCodeMap.keys) {
                val cardFuture =
                    cardsRTDB.getCardFromGlobalCollection(code.toString()).thenApply { card ->
                        val dbCard = Gson().fromJson(card.value.toString(), DBMagicCard::class.java)
                        dbCard.copy(possession = possession)
                    }
                cardFutures.add(cardFuture)
            }

            CompletableFuture.allOf(*cardFutures.toTypedArray()).thenRun {
                for (cardFuture in cardFutures) {
                    cards.add(cardFuture.get())
                }
                future.complete(cards)
            }
        }
        return future
    }

}
