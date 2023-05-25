package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

/**
 * Represents a database sync.
 */
object DatabaseSync {


    private var isDoingSync: CompletableFuture<Boolean>? = null

    /**
     * Sync the local database with the remote database.
     */
    @JvmStatic
    fun sync(): CompletableFuture<Boolean> {
        val isSyncCompleted = CompletableFuture<Boolean>()
        val userRTDB = UserRTDB(FirebaseDB())
        if (!SignIn.getSignIn().isUserLoggedIn() || LocalDatabaseProvider.getDatabase(
                LocalDatabaseProvider.CARDS_DATABASE_NAME
            ) == null
        ) {
            Log.i("DatabaseSync", "sync: Not connected to database")
            isSyncCompleted.completeExceptionally(Exception("Not connected to database"))
            return isSyncCompleted
        }

        if (isDoingSync != null && !(isDoingSync!!.isDone)) {
            Log.i("DatabaseSync", "sync: Already doing sync")
            isSyncCompleted.completeExceptionally(Exception("Already doing sync"))
            return isSyncCompleted
        }
        Log.i("DatabaseSync", "sync: start")

        isDoingSync = isSyncCompleted
        val f1 = processCardsByPossession(CardPossession.OWNED, userRTDB)
        val f2 = processCardsByPossession(CardPossession.TRADE, userRTDB)
        val f3 = processCardsByPossession(CardPossession.WANTED, userRTDB)

        CompletableFuture.allOf(f1, f2, f3).thenAccept {
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

        val listOfFirebaseCards = mutableListOf<DBMagicCard>()
        val cards = userRTDB.getListOfFullCardsInfos(
            SignIn.getSignIn().getUserUID()!!,
            possession
        )

        cards.thenAccept {
            val listOfFutures = mutableListOf<CompletableFuture<Boolean>>()
            for (card in it) {
                val future = syncCard(card, listOfFirebaseCards)
                listOfFutures.add(future)
            }
            CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenRun {
                syncLocalDBToFirebase(possession, userRTDB, listOfFirebaseCards)
                    .thenAccept(isSyncDone::complete)
            }
        }.exceptionally {
            Log.i("DatabaseSync", "processCardsByPossession: There is no card in the firebase")
            addLocalDBToFirebase(possession, userRTDB)
                .thenAccept(isSyncDone::complete)
            null
        }

        return isSyncDone
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
        LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)?.magicCardDao()
            ?.insertCard(newCard)
        val userRTDB = UserRTDB(FirebaseDB())

        // remove if there is a card in another folder (e.g new folder = owned -> old one = wanted)
        val f1 = userRTDB.removeAllCopyOfCard(SignIn.getSignIn().getUserUID()!!, oldCard)

        val f2 = userRTDB.replaceCardFromUser(SignIn.getSignIn().getUserUID()!!, newCard)
        CompletableFuture.allOf(f1, f2).thenRun {
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

            f1.thenAccept {
                isSyncDone.complete(it)
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

        val scope = CoroutineScope(Dispatchers.Default)
        var cardToBeAdded: DBMagicCard = fbCard
        scope.launch {
            val localCard =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getCard(fbCard.code, fbCard.number.toString())

            if (localCard == null) {
                // Card is not in the local database, so we put it in the local database
                addToLocalDatabase(fbCard)
                isSyncDone.complete(true)
            } else {
                // Card is in the local database, so we compare the last update
                if (isOlderThan(localCard, fbCard)) {
                    pushChanges(fbCard, localCard)
                        .thenAccept(isSyncDone::complete)
                } else {
                    pushChanges(localCard, fbCard)
                        .thenAccept(isSyncDone::complete)
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
        return card1.lastUpdate <= card2.lastUpdate
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

            CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenAccept {
                isSyncDone.complete(true)
            }
        }
        return isSyncDone
    }
}
