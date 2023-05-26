package com.github.sdp.tarjetakuna.database

import android.util.Log
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Coordinates
import com.github.sdp.tarjetakuna.model.User
import com.google.firebase.database.*
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * This class is used to manage the user's data in the database.
 */
class UserRTDB(database: Database) { //Firebase.database.reference.child("users") //assumption: we check add ability in User

    companion object {
        private const val TAG = "UserRTDB"
    }

    private var db: DatabaseReference
    private var cardsRTDB: CardsRTDB
    private var chatsRTDB: ChatsRTDB
    private var chatsListener: ValueEventListener? = null

    init {
        this.db = database.usersTable()
        cardsRTDB = CardsRTDB(database)
        chatsRTDB = ChatsRTDB(database)
    }

    /**
     * Adds a card to the user's collection.
     * Returns true if the operation succeeded, false otherwise.
     */
    fun addCard(fbcard: DBMagicCard, userUID: String): CompletableFuture<Boolean> {
        val cardUID = fbcard.getFbKey()
        val fbpossession = fbcard.possession.toString().lowercase()
        val cardNode = db.child(userUID).child(fbpossession).child(cardUID)

        cardsRTDB.addCardToGlobalCollection(fbcard)

        val completableFuture = CompletableFuture<Boolean>()

        try {
            cardNode.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val quantityRef = currentData.child("quantity")
                    val currentValue = quantityRef.getValue(Int::class.java)
                    if (currentValue != null) {
                        // Value exists, increment it
                        quantityRef.value = currentValue + 1
                    } else {
                        // Value doesn't exist, set it to 1
                        quantityRef.value = 1
                    }

                    val lastUpdatedRef = currentData.child("lastUpdated")
                    lastUpdatedRef.value = fbcard.lastUpdate
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (error != null || !committed) {
                        cardNode.setValue(ServerValue.increment(0))  // Rollback the transaction
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
    fun addMultipleCards(cards: List<DBMagicCard>, userUID: String): CompletableFuture<Boolean> {
        val listOfFutures = mutableListOf<CompletableFuture<Boolean>>()
        val completableFuture = CompletableFuture<Boolean>()
        for (card in cards) {
            val future = addCard(card, userUID)
            listOfFutures.add(future)
        }
        CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenRun {
            completableFuture.complete(true)
        }
        return completableFuture
    }

    /**
     * Removes a card from the users collection.
     */
    fun removeCard(userUID: String, fbcard: DBMagicCard): CompletableFuture<Boolean> {
        val cardUID = fbcard.getFbKey()
        val fbpossession = fbcard.possession.toString().lowercase()
        val cardNode = db.child(userUID).child(fbpossession).child(cardUID)

        val completableFuture = CompletableFuture<Boolean>()

        try {
            cardNode.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val quantityRef = currentData.child("quantity")
                    val currentValue = quantityRef.getValue(Int::class.java)
                    if (currentValue != null) {
                        // Value exists, decrement it
                        if (currentValue != 0) {
                            quantityRef.value = currentValue - 1
                        }
                    } else {
                        // Value doesn't exist, set it to 0
                        quantityRef.value = 0
                    }

                    val lastUpdatedRef = currentData.child("lastUpdated")
                    lastUpdatedRef.value = fbcard.lastUpdate
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (error != null || !committed) {
                        cardNode.child("quantity")
                            .setValue(ServerValue.increment(0))  // Rollback the transaction
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
    fun removeAllCopyOfCard(userUID: String, fbcard: DBMagicCard): CompletableFuture<Boolean> {
        val cardUID = fbcard.getFbKey()
        val fbpossession = fbcard.possession.toString().lowercase()
        val cardRef = db.child(userUID).child(fbpossession).child(cardUID)

        val completableFuture = CompletableFuture<Boolean>()

        cardRef.child("quantity").setValue(0).addOnSuccessListener {
            cardRef.child("lastUpdated").setValue(fbcard.lastUpdate).addOnSuccessListener {
                completableFuture.complete(true)
            }.addOnFailureListener {
                completableFuture.complete(false)
            }
        }.addOnFailureListener {
            completableFuture.complete(false)
        }
        return completableFuture
    }

    /**
     * Get the unique card code from the user's collection asynchronously from the database (based on possession category)
     */
    fun getCardQuantityFromUserCollection(
        userUID: String,
        cardUID: String,
        possession: CardPossession
    ): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        db.child(userUID).child(possession.toString().lowercase()).child(cardUID).child("quantity")
            .get()
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
            val cardCodeFuture = getCardQuantityFromUserCollection(userUID, cardUID, possession)
            cardCodeFuture.thenCompose {
                if (it.value == null || it.value == 0L) {
                    throw NoSuchFieldException("card $cardUID is not in user collection")
                } else {
                    cardsRTDB.getCardFromGlobalCollection(cardUID) //only get the card if it exists in the user's collection
                }
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
     * Add a [Chat] to the user's chats collection,
     * in (chats and messages) nodes then user node.
     */
    fun addChat(chat: Chat): CompletableFuture<Chat> {
        val future = CompletableFuture<Chat>()
        val dbChat = DBChat.toDBChat(chat)
        chatsRTDB.addChatToDatabase(chat).thenAccept {
            addChat(dbChat).thenAccept {
                future.complete(chat)
            }.exceptionally {
                future.completeExceptionally(it)
                null
            }
        }.exceptionally {
            future.completeExceptionally(it)
            null
        }
        return future
    }

    /**
     * Get all users from the database
     */
    fun getUsers(): CompletableFuture<List<User>> {
        val future = CompletableFuture<List<User>>()
        val userFutures = mutableListOf<CompletableFuture<Void>>()
        val users = mutableListOf<User>()
        db.get().addOnSuccessListener { snapshot ->
            for (user in snapshot.children) {
                val uid = user.key.toString()
                val usernameValue = user.child("username").value
                var username = usernameValue?.toString()

                if (username == null) {
                    UsernamesRTDB(FirebaseDB()).getUsernameFromUID(uid).thenAccept {
                        username = it.value.toString()
                    }
                }

                val coordinates = user.child("location").run {
                    val latValue = child("lat").value
                    val longValue = child("long").value
                    val lat = latValue?.toString()?.toDouble() ?: 0.0
                    val long = longValue?.toString()?.toDouble() ?: 0.0
                    Coordinates(lat, long)
                }

                val cards = mutableListOf<DBMagicCard>()
                val ownedCardsFuture = getListOfFullCardsInfos(
                    uid,
                    CardPossession.OWNED
                )
                val wantedCardsFuture = getListOfFullCardsInfos(
                    uid,
                    CardPossession.WANTED
                )

                val userFuture =
                    CompletableFuture.allOf(ownedCardsFuture, wantedCardsFuture).thenRun {
                        cards.addAll(ownedCardsFuture.get())
                        cards.addAll(wantedCardsFuture.get())
                        username?.let { User(uid, it, cards, coordinates) }?.let { users.add(it) }
                    }
                userFutures.add(userFuture)
            }
            CompletableFuture.allOf(*userFutures.toTypedArray()).thenRun {
                future.complete(users)
            }

        }
        return future
    }

    /**
     * Get a user from the database by their username
     */
    fun getUserByUsername(username: String): CompletableFuture<User?> {
        val future = CompletableFuture<User?>()
        db.get().addOnSuccessListener { snapshot ->
            for (user in snapshot.children) {
                if (user.child("username").value.toString() == username) {
                    val uid = user.key.toString()
                    val coordinates = user.child("location").run {
                        val lat = child("lat").value?.toString()?.toDouble() ?: 0.0
                        val long = child("long").value?.toString()?.toDouble() ?: 0.0
                        Coordinates(lat, long)
                    }
                    val ownedCardsFuture = cardsFromUser(uid, CardPossession.OWNED)
                    val wantedCardsFuture = cardsFromUser(uid, CardPossession.WANTED)

                    CompletableFuture.allOf(ownedCardsFuture, wantedCardsFuture).thenAccept {
                        val ownedCards = ownedCardsFuture.join()
                        val wantedCards = wantedCardsFuture.join()

                        val cards = mutableListOf<DBMagicCard>()
                        cards.addAll(ownedCards)
                        cards.addAll(wantedCards)

                        future.complete(User(uid, username, cards, coordinates))
                    }
                    return@addOnSuccessListener
                }
            }
            future.complete(null)
        }
        return future
    }

    private fun cardsFromUser(
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

    /**
     * Put a chat reference in both users' chats collection
     * in the users nodes only.
     */
    fun addChat(dbChat: DBChat): CompletableFuture<DBChat> {
        val future = CompletableFuture<DBChat>()
        db.child(dbChat.user1).child("chats").child(dbChat.uid).setValue(dbChat.user1LastRead)
            .addOnSuccessListener {
                db.child(dbChat.user2).child("chats").child(dbChat.uid)
                    .setValue(dbChat.user2LastRead)
                    .addOnSuccessListener { future.complete(dbChat) }
                    .addOnFailureListener { future.completeExceptionally(it) }
            }.addOnFailureListener { future.completeExceptionally(it) }
        return future
    }

    /**
     * Get all chats ids from the user's chats collection
     * in the users and chats nodes.
     */
    fun getChatsFromUser(userUID: String): CompletableFuture<List<DBChat>> {
        val future = CompletableFuture<List<DBChat>>()
        db.child(userUID).child("chats").get().addOnSuccessListener {
            if (it.value == null) {
                Log.d(TAG, "no chats")
                future.complete(mutableListOf())
            } else {
                Log.d(TAG, "chats received $it")
                val chatIds = it.value as HashMap<String, Date>

                val futureDBChats = chatsRTDB.getChats(chatIds.keys.toList())
                futureDBChats.thenAccept { dbChats ->
                    future.complete(dbChats)
                }.exceptionally { it2 ->
                    future.completeExceptionally(it2)
                    null
                }
            }
        }.addOnFailureListener {
            Log.w(TAG, "Error getting chats", it)
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Replace the card in the user's collection asynchronously from the database
     */
    fun replaceCardFromUser(
        userUID: String,
        card: DBMagicCard
    ): CompletableFuture<Boolean> {
        val cardUID = card.getFbKey()
        val future = CompletableFuture<Boolean>()
        val cardRef = db.child(userUID).child(card.possession.toString().lowercase()).child(cardUID)

        cardsRTDB.addCardToGlobalCollection(card)
        cardRef.child("quantity").setValue(card.quantity).addOnSuccessListener {
            cardRef.child("lastUpdated").setValue(card.lastUpdate).addOnSuccessListener {
                future.complete(true)
            }.addOnFailureListener {
                future.completeExceptionally(it)
            }
        }
        return future
    }

    /**
     * Add a listener to the user's chats collection to get updates
     * from the user and chat tables.
     */
    fun addChatsListener(userUID: String, listener: UserChatsListener) {
        if (chatsListener != null) {
            db.child(userUID).child("chats").removeEventListener(chatsListener!!)
        }
        chatsListener =
            db.child(userUID).child("chats").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.w(
                        TAG,
                        "ValueEventListener:onDataChange userUID: $userUID snapshot: $snapshot"
                    )
                    if (snapshot.value == null) {
                        listener.onChatsRemoved()
                    } else {
                        val chatIds = snapshot.value as HashMap<String, Date>
                        val futureDBChats = chatsRTDB.getChats(chatIds.keys.toList())
                        futureDBChats.thenAccept { dbChats ->
                            listener.onChatsChanged(dbChats)
                        }.exceptionally { it2 ->
                            Log.w(TAG, "Error getting chats", it2)
                            null
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
    }

    /**
     * Remove the listener from the user's chats collection.
     */
    fun removeChatsListener(userUID: String) {
        chatsListener?.let { db.child(userUID).child("chats").removeEventListener(it) }
    }

    /**
     * get the quantity and lastUpdate information of a card from the user's collection asynchronously from the database
     * e.g
     * card_key:
     *  quantity: 1
     *  lastUpdated: 123456789
     * It will return the node "card_key"
     */
    private fun getCardAdditionnalInfos(
        userUID: String,
        card: DBMagicCard,
        possession: CardPossession
    ): CompletableFuture<DataSnapshot> {
        val cardUID = card.getFbKey()
        val future = CompletableFuture<DataSnapshot>()
        db.child(userUID).child(possession.toString().lowercase()).child(cardUID)
            .get()
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
     * Get a card from the user's collection asynchronously from the database
     * with all the additionnal informations (quantity, lastUpdate, possession)
     * Since the card from the global collection doesn't have the quantity and lastUpdate information.
     */
    private fun getFullCardInfos(
        userUID: String,
        card: DBMagicCard,
        possession: CardPossession
    ): CompletableFuture<DBMagicCard> {
        val future = CompletableFuture<DBMagicCard>()
        getCardAdditionnalInfos(userUID, card, possession).thenAccept { snapshot ->
            val fbCard = card.copy(
                quantity = snapshot.child("quantity").value.toString().toInt(),
                lastUpdate = snapshot.child("lastUpdated").value.toString().toLong(),
                possession = possession
            )
            future.complete(fbCard)
        }
        return future
    }

    /**
     * Get the list of all cards from the user's collection asynchronously from the database
     * with all the additionnal informations (quantity, lastUpdate, possession)
     */
    fun getListOfFullCardsInfos(
        userUID: String,
        possession: CardPossession
    ): CompletableFuture<List<DBMagicCard>> {
        val future = CompletableFuture<List<DBMagicCard>>()
        db.child(userUID).child(possession.toString().lowercase()).get()
            .addOnSuccessListener {
                if (it.value == null) {
                    future.complete(listOf())
                } else {
                    val listOfCardsUID = (it.value as HashMap<*, *>).keys
                    val listOfFutures = mutableListOf<CompletableFuture<DBMagicCard>>()
                    for (cardName in listOfCardsUID) {
                        val f1 = cardsRTDB.getCardFromGlobalCollection(cardName.toString())
                            .thenCompose { card ->
                                getFullCardInfos(
                                    userUID,
                                    Gson().fromJson(card.value.toString(), DBMagicCard::class.java),
                                    possession
                                )
                            }
                        listOfFutures.add(f1)
                    }

                    CompletableFuture.allOf(*listOfFutures.toTypedArray()).thenRun {
                        val listOfCards = mutableListOf<DBMagicCard>()
                        for (futureCard in listOfFutures) {
                            listOfCards.add(futureCard.get())
                        }
                        future.complete(listOfCards)
                    }
                }
            }.addOnFailureListener {
                future.complete(listOf())
            }
        return future
    }
}
