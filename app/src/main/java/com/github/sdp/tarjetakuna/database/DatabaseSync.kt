package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    fun sync() {
        Log.i("DatabaseSync", "sync: start")
        val userRTDB = UserRTDB(FirebaseDB())
        if (!SignIn.getSignIn().isUserLoggedIn()) {
            Log.i("DatabaseSync", "sync: Not connected to firebase")
            return
        }
        processCardsByPossession(CardPossession.OWNED, userRTDB)
        processCardsByPossession(CardPossession.TRADE, userRTDB)
        processCardsByPossession(CardPossession.WANTED, userRTDB)

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
    ): CompletableFuture<MutableList<DBMagicCard>> {
        val cards = userRTDB.cardsFromUser(
            SignIn.getSignIn().getUserUID()!!,
            possession
        )

        val listOfFirebaseCards = mutableListOf<DBMagicCard>()
        val listOfFutures1 = mutableListOf<CompletableFuture<DataSnapshot>>()
        cards.thenApply { c ->
            if (c != null) {
                for (card in c) {
                    Log.i(
                        "DatabaseSync",
                        "card in cards: ${card.code}_${card.number} found on firebase"
                    )
                    val future1 = getCardQuantity(card, possession, userRTDB)
                    // When the quantity is found,
                    // We search for the last time it has been updated and then update the card
                    future1.thenApply { quantity ->
                        card.quantity = quantity.getValue(Int::class.java)!!
                        // Get the last time the card has been updated
                        val future2 = getLastUpdated(card, possession, userRTDB)
                        future2.thenApply { lastUpdated ->
                            card.lastUpdate = lastUpdated.value as Long
                            syncCard(card, listOfFirebaseCards)
                        }
                    }
                    listOfFutures1.add(future1)
                }
                // When all of them are done, we can sync the local database to the remote database
                CompletableFuture.allOf(*listOfFutures1.toTypedArray())
                    .thenRun {
                        Log.i("DatabaseSync", "finish: all cards added to list")
                        syncLocalDBToFirebase(possession, userRTDB, listOfFirebaseCards)

                    }
            }
        }.exceptionally {
            Log.i("DatabaseSync", "no cards found in database}")
            addLocalDBToFirebase(possession, userRTDB)
            null
        }
        return cards
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
    private suspend fun pushChanges(newCard: DBMagicCard, oldCard: DBMagicCard) {
        LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
            .magicCardDao().insertCard(newCard)
        val userRTDB = UserRTDB(FirebaseDB())
        userRTDB.removeAllCopyOfCard(SignIn.getSignIn().getUserUID()!!, oldCard)
        userRTDB.addCard(newCard, SignIn.getSignIn().getUserUID()!!)
        Log.i("DatabaseSync", "pushChanges: $newCard cards updated")
    }

    /**
     * Add the local database to the remote database.
     * Used when the user has no cards in the remote database.
     */
    private fun addLocalDBToFirebase(possession: CardPossession, userRTDB: UserRTDB) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val localCards =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getAllCardsByPossession(possession.toString())

            userRTDB.addMultipleCards(
                localCards,
                SignIn.getSignIn().getUserUID()!!
            )
        }
    }


    /**
     * Add a card to the local database.
     */
    private fun addToLocalDatabase(card: DBMagicCard) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                .magicCardDao().insertCard(card)
        }
    }

    /**
     * Add a card to the remote database.
     */
    private fun addToFirebase(card: DBMagicCard, userRTDB: UserRTDB) {
        userRTDB.addCard(card, SignIn.getSignIn().getUserUID()!!)
    }

    /**
     * Sync a card
     */
    private fun syncCard(fbCard: DBMagicCard, listOfFirebaseCards: MutableList<DBMagicCard>) {
        val scope = CoroutineScope(Dispatchers.Default)
        var cardToBeAdded: DBMagicCard = fbCard
        scope.launch {
            val localCard =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getCard(fbCard.code, fbCard.number.toString())

            Log.i("DatabaseSync", "Local databse card: $localCard")
            if (localCard == null) {
                // Card is not in the local database, so we put it in the local database
                addToLocalDatabase(fbCard)
            } else {
                // Card is in the local database, so we compare the last update
                if (isOlderThan(localCard, fbCard)) {
                    pushChanges(fbCard, localCard)
                } else {
                    pushChanges(localCard, fbCard)
                    cardToBeAdded = localCard
                }
            }
        }.invokeOnCompletion {
            listOfFirebaseCards.add(cardToBeAdded)
        }
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
     */
    private fun syncLocalDBToFirebase(
        possession: CardPossession,
        userRTDB: UserRTDB,
        listOfFirebaseCards: MutableList<DBMagicCard>
    ) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val localCards =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getAllCardsByPossession(possession.toString())
            for (localCard in localCards) {
                val cardIndex = listOfFirebaseCards.indexOfFirst {
                    it.code == localCard.code && it.number == localCard.number
                }
                if (cardIndex == -1) {
                    // Card is not in the remote database, so we put it in the remote database
                    addToFirebase(localCard, userRTDB)
                    continue
                }
                val fbCard = listOfFirebaseCards[cardIndex]
                // Card is in the remote database, so we compare the last update
                if (isOlderThan(localCard, fbCard)) {
                    pushChanges(fbCard, localCard)
                } else {
                    pushChanges(localCard, fbCard)
                }

            }
        }
    }
}
