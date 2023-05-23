package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

/**
 * Represents a database sync.
 */
object DatabaseSync {


    /**
     * Sync the local database with the remote database.
     */
    @JvmStatic
    fun sync(): CompletableFuture<Boolean> {
        Log.i("DatabaseSync", "sync: start")
        val isSyncCompleted = CompletableFuture<Boolean>()
        val userRTDB = UserRTDB(FirebaseDB())
        if (!SignIn.getSignIn().isUserLoggedIn()) {
            Log.i("DatabaseSync", "sync: Not connected to firebase")
            isSyncCompleted.completeExceptionally(Exception("Not connected to firebase"))
            return isSyncCompleted
        }
        val f1 = processCardsByPossession(CardPossession.OWNED, userRTDB)
        val f2 = processCardsByPossession(CardPossession.TRADE, userRTDB)
        val f3 = processCardsByPossession(CardPossession.WANTED, userRTDB)

        CompletableFuture.allOf(f1, f2, f3).thenRun {
            Log.i("DatabaseSync", "sync: all cards processed")
            isSyncCompleted.complete(true)
        }
        return isSyncCompleted
    }

    /**
     * Get the cards from the remote database.
     * @param possession the possession of the cards
     * @param userRTDB the remote database
     * @return the cards from the remote database
     */
    private fun processCardsByPossession(
        possession: CardPossession,
        userRTDB: UserRTDB
    ): CompletableFuture<Boolean> {

        val isSyncDone = CompletableFuture<Boolean>()
        val cards = userRTDB.cardsFromUser(
            SignIn.getSignIn().getUserUID()!!,
            possession
        )

        val listOfFirebaseCards = mutableListOf<DBMagicCard>()
        val listOfFutures1 = mutableListOf<CompletableFuture<DataSnapshot>>()
        cards.thenAccept { c ->
            if (c != null) {
                for (card in c) {
                    Log.i(
                        "DatabaseSync",
                        "processCardsByPossession: card in cards: ${card.code}_${card.number} found on firebase"
                    )
                    val f1 = getCardQuantity(card, possession, userRTDB)
                    f1.whenComplete { _, _ ->
                        Log.e(
                            "DatabaseSync",
                            "processCardsByPossession: F1 IS FINISHED"
                        )
                    }
                    // When the quantity is found,
                    // We search for the last time it has been updated and then update the card
                    f1.thenApply { quantity ->
                        card.quantity = quantity.getValue(Int::class.java)!!
                        // Get the last time the card has been updated
                        val f2 = getLastUpdated(card, possession, userRTDB)
                        f2.thenApply { lastUpdated ->
                            card.lastUpdate = lastUpdated.value as Long
                            val f3 = syncCard(card, listOfFirebaseCards)
                            f3.whenComplete { _, _ ->
                                Log.i(
                                    "DatabaseSync",
                                    "processCardsByPossession: complete: ${card.code}_${card.number} found on firebase"
                                )
                            }
                        }
                    }
                    listOfFutures1.add(f1)
                }
                // When all of them are done, we can sync the local database to the remote database
                CompletableFuture.allOf(*listOfFutures1.toTypedArray())
                    .thenRun {
                        Log.i(
                            "DatabaseSync",
                            "processCardsByPossession: finish: all cards added to list"
                        )
                        val f = syncLocalDBToFirebase(possession, userRTDB, listOfFirebaseCards)
                        f.whenComplete { _, _ ->
                            Log.i(
                                "DatabaseSync",
                                "processCardsByPossession: finish: syncLocalDBToFirebase $possession"
                            )
                            isSyncDone.complete(true)
                        }
                    }
            } else {
                Log.e("DatabaseSync", "processCardsByPossession: AAAAAAAAAAAAAAAAAAAAAAAA")
            }
        }.exceptionally {
            Log.i("DatabaseSync", "no cards found in firebase}")
            addLocalDBToFirebase(possession, userRTDB)
                .whenComplete { _, _ ->
                    Log.i(
                        "DatabaseSync",
                        "processCardsByPossession: complete exceptionally: $possession"
                    )
                    isSyncDone.complete(true)
                }
            null
        }
        return isSyncDone
    }

    /**
     * Get the quantity of a card from the remote database.
     */
    private fun getCardQuantity(
        card: DBMagicCard,
        possession: CardPossession,
        userRTDB: UserRTDB
    ): CompletableFuture<DataSnapshot> {
        val quantity = userRTDB.getCardQuantityFromUserCollection(
            SignIn.getSignIn().getUserUID()!!,
            card.getFbKey(),
            possession
        )
        quantity.thenAccept { q ->
            if (q != null) {
                Log.i(
                    "DatabaseSync",
                    "getQuantity: ${card.code}_${card.number} found on firebase with ${q.value} quantity"
                )
                card.quantity = q.value as Int
            }
        }
        return quantity
    }

    /**
     * Get the last update of a card from the remote database.
     */
    private fun getLastUpdated(
        card: DBMagicCard,
        possession: CardPossession,
        userRTDB: UserRTDB
    ): CompletableFuture<DataSnapshot> {
        val lastUpdate = userRTDB.getCardLastUpdatedFromUserPossession(
            SignIn.getSignIn().getUserUID()!!,
            card.getFbKey(),
            possession
        )
        lastUpdate.thenAccept { l ->
            if (l != null) {
                Log.i("DatabaseSync", "lastUpdate: ${l.value} for card ${card.code}_${card.number}")
                card.lastUpdate = l.value as Long
            }
        }
        return lastUpdate
    }

    /**
     * Push the changes to the local database and to the remote database.
     * @param newCard the card to push
     * @param oldCard the old card
     */
    private suspend fun pushChanges(
        newCard: DBMagicCard,
        oldCard: DBMagicCard
    ): CompletableFuture<Boolean> {
        val isSyncDone = CompletableFuture<Boolean>()
        Log.e("DatabaseSync", "pushChanges: I come here")
        LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
            .magicCardDao().insertCard(newCard)
        val userRTDB = UserRTDB(FirebaseDB())

        val f1 = userRTDB.removeAllCopyOfCard(SignIn.getSignIn().getUserUID()!!, oldCard)
        val f2 = userRTDB.addCard(newCard, SignIn.getSignIn().getUserUID()!!)
        Log.i("DatabaseSync", "pushChanges: $newCard cards updated")
        CompletableFuture.allOf(f1, f2).thenRun {
            Log.e("DatabaseSync", "pushChanges: $newCard cards updated")
            isSyncDone.complete(true)
        }
        return isSyncDone
    }

    /**
     * Add the local database to the remote database.
     * Used when the user has no cards in the remote database.
     * @param possession the possession of the cards
     * @param userRTDB the remote database
     * @param isSyncDone the future to complete when the sync is done
     */
    private fun addLocalDBToFirebase(
        possession: CardPossession,
        userRTDB: UserRTDB,
    ): CompletableFuture<Boolean> {
        val isSyncDone = CompletableFuture<Boolean>()

        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val localCards =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getAllCardsByPossession(possession.toString())
            if (localCards.isEmpty()) {
                isSyncDone.complete(true)
                scope.cancel()
            }
            val f1 = userRTDB.addMultipleCards(
                localCards,
                SignIn.getSignIn().getUserUID()!!
            )

            f1.whenComplete { _, _ ->
                isSyncDone.complete(true)
            }
        }
        return isSyncDone
    }


    /**
     * Add a card to the local database.
     */
    private suspend fun addToLocalDatabase(card: DBMagicCard) {
        LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
            .magicCardDao().insertCard(card)
    }

    /**
     * Add a card to the remote database.
     */
    private fun addToFirebase(card: DBMagicCard, userRTDB: UserRTDB): CompletableFuture<Boolean> {
        return userRTDB.addCard(card, SignIn.getSignIn().getUserUID()!!)
    }

    /**
     * Sync a card
     */
    private fun syncCard(
        fbCard: DBMagicCard,
        listOfFirebaseCards: MutableList<DBMagicCard>
    ): CompletableFuture<Boolean> {
        val isSyncDone = CompletableFuture<Boolean>()
        // TODO REMOVE THIS
        isSyncDone.whenComplete { _, _ ->
            Log.i("DatabaseSync", "syncCard: isSyncDone completed")
        }

        val scope = CoroutineScope(Dispatchers.Default)
        var cardToBeAdded: DBMagicCard = fbCard
        scope.launch {
            val localCard =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getCard(fbCard.code, fbCard.number.toString())

            Log.i("DatabaseSync", "syncCard: $localCard")
            if (localCard == null) {
                // Card is not in the local database, so we put it in the local database
                addToLocalDatabase(fbCard)
                isSyncDone.complete(true)
                Log.e("DatabaseSync", "syncCard: IM HERE KSJDLADJSLKJD")
            } else {
                // Card is in the local database, so we compare the last update
                if (isOlderThan(localCard, fbCard)) {
                    pushChanges(fbCard, localCard)
                        .whenComplete { _, _ ->
                            isSyncDone.complete(true)
                        }
                } else {
                    pushChanges(localCard, fbCard)
                        .whenComplete { _, _ ->
                            isSyncDone.complete(true)
                        }
                    cardToBeAdded = localCard
                }
            }
        }.invokeOnCompletion {
            listOfFirebaseCards.add(cardToBeAdded)
        }
        return isSyncDone
    }

    /**
     * Compare the last update of two cards.
     */
    private fun isOlderThan(card1: DBMagicCard, card2: DBMagicCard): Boolean {
        return if (card1.lastUpdate < card2.lastUpdate) {
            Log.i("DatabaseSync", "card1 is older than card2")
            true
        } else {
            Log.i("DatabaseSync", "card2 is older than card1")
            false
        }
    }

    /**
     * Sync all cards from the local database to the remote database.
     * @param possession the possession of the cards
     * @param userRTDB the remote database
     * @param listOfFirebaseCards the list of cards from the remote database
     */
    private fun syncLocalDBToFirebase(
        possession: CardPossession,
        userRTDB: UserRTDB,
        listOfFirebaseCards: MutableList<DBMagicCard>
    ): CompletableFuture<Boolean> {
        val isSyncDone = CompletableFuture<Boolean>()

        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val localCards =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getAllCardsByPossession(possession.toString())

            // a list of futures to wait for before completing the sync
            val listOfFutures: MutableList<CompletableFuture<Boolean>> = mutableListOf()
            for (localCard in localCards) {
                val cardIndex = listOfFirebaseCards.indexOfFirst {
                    it.code == localCard.code && it.number == localCard.number
                }
                if (cardIndex == -1) {
                    // Card is not in the remote database, so we put it in the remote database
                    val f1 = addToFirebase(localCard, userRTDB)
                    listOfFutures.add(f1)
                    continue
                }
                val fbCard = listOfFirebaseCards[cardIndex]
                // Card is in the remote database, so we compare the last update
                if (isOlderThan(localCard, fbCard)) {
                    val f1 = pushChanges(fbCard, localCard)
                    listOfFutures.add(f1)
                } else {
                    val f1 = pushChanges(localCard, fbCard)
                    listOfFutures.add(f1)
                }
            }

            CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenRun {
                isSyncDone.complete(true)
            }
        }
        return isSyncDone
    }
}
