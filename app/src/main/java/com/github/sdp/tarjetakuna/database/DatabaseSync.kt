package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.github.sdp.tarjetakuna.ui.authentication.SignIn
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
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
        //TODO assign the right function for cards
        val cards = processCardsByPossession(CardPossession.OWNED, userRTDB)
        cards.thenAccept {
            for (card in it) {
                Log.i("DatabaseSync", "${card.getFbKey()}: $card")
            }
        }
    }

    /**
     * Merge the cards from the local database with the cards from the remote database.
     * @param snapshot the snapshot of the remote database (used to retrieve the remote cards
     */
    private fun processSnapshot(snapshot: DataSnapshot) {
        val map: Map<String, String> = snapshot.value!! as Map<String, String>
        val fbCardsMap: Map<String, DBMagicCard> = map.mapValues { (_, value) ->
            Gson().fromJson(value, DBMagicCard::class.java)
        }

        Log.i("DatabaseSync", "sync: ${fbCardsMap.size} cards found on firebase")

        // retrieve local cards
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val localCards =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getAllCards().associateBy { it.getFbKey() }
            Log.i("DatabaseSync", "sync: ${localCards.size} cards found on local database")
            // merge cards so that we only have the most updated cards
            var updatedCards: MutableMap<String, DBMagicCard> = mutableMapOf()
            updatedCards = mergeCards(localCards, fbCardsMap, updatedCards)
            updatedCards = mergeCards(fbCardsMap, localCards, updatedCards)

//            pushChanges(updatedCards.toList().map { it.second })
        }
    }

    /**
     * Merge two maps of cards so that it contains only the most updated cards.
     * @param map1 the first map
     * @param map2 the second map
     * @param currentCards the current cards
     */
    private fun mergeCards(
        map1: Map<String, DBMagicCard>,
        map2: Map<String, DBMagicCard>,
        currentCards: MutableMap<String, DBMagicCard>
    ): MutableMap<String, DBMagicCard> {
        for ((key, card1) in map1) {
            if (!map2.contains(key) && !currentCards.contains(key)) {
                currentCards[key] = card1
            } else if (map2.contains(key) && !currentCards.contains(key)) {
                val card2 = map2[key]!!

                if (card2.lastUpdate > card1.lastUpdate) {
                    currentCards[key] = card2
                } else {
                    currentCards[key] = card1
                }
            }
        }
        return currentCards
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
        // TODO Change when we can add the cards that we possess
        // TODO cardsSeparated contains the cards separated by possession, it may not be useful depending
        // TODO on how we add the cards to the remote database
        userRTDB.removeCard(SignIn.getSignIn().getUserUID()!!, oldCard)
        userRTDB.addCard(newCard, SignIn.getSignIn().getUserUID()!!)
        Log.i("DatabaseSync", "pushChanges: $newCard cards updated")
    }

    /**
     * Add the local database to the remote database.
     * Used when the user has no cards in the remote database.
     */
    private fun addLocalDBToFirebase() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val localCards =
                LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
                    .magicCardDao().getAllCards()
            val userRTDB = CardsRTDB(
                FirebaseDB()
            )

            // TODO Change when we can add the cards that we possess,
            // TODO cardsSeparated contains the cards separated by possession, it may not be useful depending
            // TODO on how we add the cards to the remote database
            val cardsSeparated = separateCardsByPossession(localCards)
//            userRTDB.addCardsToCollection(localCards)
        }
    }

    /**
     * Separate the cards by possession.
     */
    private fun separateCardsByPossession(cards: List<DBMagicCard>): Map<String, List<DBMagicCard>> {
        val cardsByPossession: MutableMap<String, MutableList<DBMagicCard>> = mutableMapOf()
        cardsByPossession[CardPossession.OWNED.toString()] = mutableListOf()
        cardsByPossession[CardPossession.NONE.toString()] = mutableListOf()
        cardsByPossession[CardPossession.WANTED.toString()] = mutableListOf()
        cardsByPossession[CardPossession.TRADE.toString()] = mutableListOf()

        for (card in cards) {
            when (card.possession) {
                CardPossession.NONE -> cardsByPossession[CardPossession.NONE.toString()]!!.add(card)
                CardPossession.OWNED -> cardsByPossession[CardPossession.OWNED.toString()]!!.add(
                    card
                )
                CardPossession.WANTED -> cardsByPossession[CardPossession.WANTED.toString()]!!.add(
                    card
                )
                CardPossession.TRADE -> cardsByPossession[CardPossession.TRADE.toString()]!!.add(
                    card
                )
            }
        }
        return cardsByPossession
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

        val listOfCards = mutableListOf<DBMagicCard>()
        cards.whenComplete { c, exception ->
            if (c != null) {
                for (card in c) {
                    Log.i(
                        "DatabaseSync",
                        "card in cards: ${card.code}_${card.number} found on firebase"
                    )
                    val future1 = getCardQuantity(card, possession, userRTDB)
                    // When the quantity is found,
                    // We search for the last time it has been updated and then update the card
                    future1.whenComplete { quantity, _ ->
                        card.quantity = quantity.getValue(Int::class.java)!!
                        // Get the last time the card has been updated
                        val future2 = getLastUpdated(card, possession, userRTDB)
                        future2.whenComplete { lastUpdated, _ ->
                            card.lastUpdate = lastUpdated.value as Long
                            listOfCards.add(card)
                            Log.i("DatabaseSync", "finish: card added to list: $card")
                            syncCard(card)
                        }
                    }
                }
            }
        }.exceptionally {
            Log.i("DatabaseSync", "no cards found in database}")
//                addLocalDBToFirebase()
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
    private fun addTOFirebase(card: DBMagicCard) {
    }

    /**
     * Sync a card
     */
    private fun syncCard(fbCard: DBMagicCard) {
        val scope = CoroutineScope(Dispatchers.Default)
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
                }
            }
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
}
