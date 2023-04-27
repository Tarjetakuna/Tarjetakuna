package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.database.local.LocalDatabaseProvider
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Represents a database sync.
 */
object DatabaseSync {


    /**
     * Sync the local database with the remote database.
     */
    @JvmStatic
    fun sync() {
        // TODO
        val userRTDB = UserCardsRTDB()
        if (!userRTDB.isConnected()) {
            Log.i("DatabaseSync", "sync: Not connected to firebase")
            return
        }
        val cards = userRTDB.getAllCardsFromCollection()
        cards.thenAccept {
            processSnapshot(it)
        }.exceptionally {
            Log.i("DatabaseSync", "no cards found in database}")
            null
        }
    }

    private fun processSnapshot(snapshot: DataSnapshot) {
        // TODO
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
                    .magicCardDao().getAllCards().associateBy { it.code + it.number.toString() }
            Log.i("DatabaseSync", "sync: ${localCards.size} cards found on local database")
            var updatedCards: MutableMap<String, DBMagicCard> = mutableMapOf()
            updatedCards = mergeCards(localCards, fbCardsMap, updatedCards)
            updatedCards = mergeCards(fbCardsMap, localCards, updatedCards)
            Log.i("DatabaseSync", "sync: $updatedCards cards updated")
            pushChanges(updatedCards.toList().map { it.second })
        }
    }

    /**
     * Merge two lists of cards so that it contains only the most updated cards.
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
     * @param cards the cards to push
     */
    private suspend fun pushChanges(cards: List<DBMagicCard>) {
        LocalDatabaseProvider.getDatabase(LocalDatabaseProvider.CARDS_DATABASE_NAME)!!
            .magicCardDao().insertCards(cards)
        val userRTDB = UserCardsRTDB()
        userRTDB.addCardsToCollection(cards)
        Log.i("DatabaseSync", "pushChanges: ${cards.size} cards updated")
    }

}
