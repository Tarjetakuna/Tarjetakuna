package com.github.sdp.tarjetakuna.database

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
    }
//        val userRTDB = UserCardsRTDB()
//        val data = userRTDB.getAllCardsFromCollection()
//        data.thenAccept {
//            println("Cards from database: ${it.value.toString()}")
//            val firebaseCards =
//                Gson().fromJson(it.value.toString(), Array<FBMagicCard>::class.java)
//                    .toCollection(ArrayList())
//
//            // define coroutine scope to run the database operations
//            val scope = CoroutineScope(Dispatchers.IO)
//
//            scope.launch {
//                val localCards = LocalDatabaseProvider.getDatabase()?.magicCardDao()?.getAllCards()
//
//            }
//        }.exceptionally {
//            println("Error: ${it.message}")
//            null
//        }
//    }
//
//    /**
//     * Check if the local database contains the firebase card.
//     * @param fbCard the card to check
//     * @param localCard the local database
//     * @return true if the card is in the local database, false otherwise
//     */
//    private fun containsCard(fbCard: FBMagicCard, localCard: List<MagicCardEntity>): Boolean {
//        for (card in localCard) {
//            if (card.code == fbCard.card.set.code && card.number == fbCard.card.number) {
//                return true
//            }
//        }
//        return false
//    }
//
//    private fun mergeCards(fbCard: FBMagicCard, localCard: MagicCardEntity): MagicCardEntity {
//        // TODO
//        if (fbCard.lastUpdate > localCard.lastUpdate) {
//            // update local card
//            return fbCard.toMagicCardEntity()
//        }
//        return localCard
//    }
}
