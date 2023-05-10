package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * This class is used to manage the global collection of cards contained by all users (no duplicates).
 */

class CardsRTDB(val database: FirebaseInterface = FirebaseWrapper(Firebase.database.reference)) {


    /**
     * Add a card to the global card collection.
     */
    fun addCardToGlobalCollection(fbCard: DBMagicCard) {
        database.addCardGlobal(fbCard)
    }

    /**
     * Add a list of cards to the global collection.
     */
    fun addMultipleCardsToGlobalCollection(fbCards: List<DBMagicCard>) {
        for (fbCard in fbCards) {
            addCardToGlobalCollection(fbCard)
        }
    }

    /**
     * Remove a card from the global collection.
     */
    fun removeCardFromGlobalCollection(cardUID: String) {
        database.removeCardGlobal(cardUID)
    }

    /**
     * Retrieves a card asynchronously from the database
     * The card is identified by only its set code and its number
     * @return the card if it exists, null otherwise
     */
    fun getCardFromGlobalCollection(cardUID: String): DBMagicCard? {
        val card = database.getCardGlobal(cardUID)
        return try {
            card.get()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Retrieves a list of cards asynchronously from the database
     * The cards are identified by only their set code and their number
     * @return the cards if they exist, null otherwise
     */
    fun getMultipleCardsFromGlobalCollection(cardUIDs: List<String>): List<DBMagicCard>? {
        val cards = mutableListOf<DBMagicCard>()
        for (cardUID in cardUIDs) {
            val card = getCardFromGlobalCollection(cardUID)
            if (card != null) {
                cards.add(card)
            }
        }
        return cards
    }

    /**
     * Retrieve all the cards asynchronously from the database
     */
    fun getAllCardsFromGlobalCollection(): List<DBMagicCard>? {
        val cards = database.getAllCardsGlobal()
        return try {
            cards.get()
        } catch (e: Exception) {
            null
        }
    }
}
