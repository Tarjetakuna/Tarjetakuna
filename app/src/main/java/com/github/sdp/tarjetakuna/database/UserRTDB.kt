package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the user's data in the database.
 */
class UserRTDB(database: Database) { //Firebase.database.reference.child("users") //assumption: we check add ability in User

    private var db: DatabaseReference
    private var cardsRTDB: CardsRTDB
    private var chatsRTDB: ChatsRTDB

    init {
        this.db = database.usersTable()
        cardsRTDB = CardsRTDB(database)
        chatsRTDB = ChatsRTDB(database)
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
     * Put a chat reference in both users' chats collection
     */
    fun addChat(dbChat: DBChat): CompletableFuture<DBChat> {
        val future = CompletableFuture<DBChat>()
        db.child(dbChat.user1).child("chats").push().setValue(dbChat.uid)
            .addOnSuccessListener {
                db.child(dbChat.user2).child("chats").push().setValue(dbChat.uid)
                    .addOnSuccessListener { future.complete(dbChat) }
                    .addOnFailureListener { future.completeExceptionally(it) }
            }.addOnFailureListener { future.completeExceptionally(it) }

        return future
    }

    /**
     * Get all chats ids from the user's chats collection
     */
    fun getChatsFromUser(userUID: String): CompletableFuture<List<DBChat>> {
        val future = CompletableFuture<List<DBChat>>()
        db.child(userUID).child("chats").get().addOnSuccessListener {
            if (it.value == null) {
                future.complete(mutableListOf())
            } else {
                val chatIds = it as List<String>
                val chats = mutableListOf<DBChat>()
                for (chatId in chatIds) {
                    chatsRTDB.getChat(chatId).thenAccept { chat ->
                        chats.add(chat)
                    }
                }
                future.complete(chats)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }
}
