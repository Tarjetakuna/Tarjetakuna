package com.github.sdp.tarjetakuna.model


import android.util.Log
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBChat
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.Database
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.github.sdp.tarjetakuna.database.UsernamesRTDB
import com.google.firebase.database.DataSnapshot
import java.util.concurrent.CompletableFuture

/**
 * Represents a user.
 */
data class User(
    val uid: String,
    val username: String,
    var cards: MutableList<DBMagicCard> = mutableListOf(),
    var location: Coordinates = Coordinates(),
    var chats: MutableList<DBChat> = mutableListOf(),
    var valid: Boolean = true,
    val database: Database = FirebaseDB(),
) {
    private var userRTDB: UserRTDB
    private var usernamesRTDB: UsernamesRTDB

    constructor(uid: String) : this(
        uid,
        "",
        mutableListOf(),
        Coordinates(),
        valid = false
    )

    init {
        userRTDB = UserRTDB(database)
        usernamesRTDB = UsernamesRTDB(database)

        if (valid) {
            require(
                username.matches(
                    Regex(
                        "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*\$"
                    )
                )
            ) { "username (email) is not valid" }
            require(
                uid.isNotBlank()
            ) { "UID is not valid" }
            usernamesRTDB.addUsernameUID(
                uid,
                username
            ) //to be able to search a user by username so you can then find their collection
        }
    }

    /**
     * Retrieves a card under a given possession asynchronously from the database
     */
    fun getCard(
        setCode: String,
        setNumber: Int,
        possession: CardPossession
    ): CompletableFuture<DataSnapshot> {
        return userRTDB.getCardFromUserPossession(uid, setCode, setNumber, possession)
    }

    /**
     * Retrieves all cards under a given possession asynchronously from the database
     */
    fun getAllCards(possession: CardPossession): List<CompletableFuture<DataSnapshot>> { //you call these functions with the User object (of other user)
        return userRTDB.getAllCardsFromUserPossession(uid, possession)
    }


    /**
     * Adds a card to the user's collection with the given possession.
     * Returns true if the operation succeeded, false otherwise.
     */
    fun addCard(card: MagicCard, possession: CardPossession): CompletableFuture<Boolean> {
        val fbCard = card.toDBMagicCard(possession)
        cards.add(fbCard)
        return userRTDB.addCard(fbCard, uid)
    }

    /**
     * Adds a list of cards to the user's collection with the given possessions.
     */
    fun addMultipleCards(
        cards: List<MagicCard>,
        possession: List<CardPossession>
    ): CompletableFuture<Boolean> {
        val cardsWithPossession = cards.zip(possession)
        val completableFutures = cardsWithPossession.map { (card, pos) ->
            CompletableFuture.supplyAsync { addCard(card, pos) }
        }.toTypedArray()

        return CompletableFuture.allOf(*completableFutures)
            .thenApply { true }
            .exceptionally { false }
    }

    /**
     * Removes a card from the user's collection with the given possession.
     */
    fun removeCard(card: MagicCard, possession: CardPossession) {
        userRTDB.removeCard(uid, card.toDBMagicCard(possession))
    }

    /**
     * Creates a new chat with the given user, uploading it to the database
     * in the users, chats and messages nodes.
     */
    fun newChat(chat: Chat): CompletableFuture<Chat> {
        val dbChat = DBChat.toDBChat(chat)
        if (chatAlreadyExist(dbChat)) {
            Log.w("User", "Chat $chat already exist")
            return CompletableFuture.completedFuture(chat)
        }
        chats.add(dbChat)
        return userRTDB.addChat(chat)
    }

    /**
     * Creates a new chat with the given user in the user table only.
     */
    fun newChat(chat: DBChat): CompletableFuture<DBChat> {
        if (chatAlreadyExist(chat)) {
            Log.w("User", "Chat $chat already exist")
            return CompletableFuture.completedFuture(chat)
        }
        chats.add(chat)
        return userRTDB.addChat(chat)
    }

    /**
     * Retrieves all chats asynchronously from the database (only the user table).
     */
    fun getChats(): CompletableFuture<List<DBChat>> {
        return userRTDB.getChatsFromUser(uid).thenApply { chats ->
            this.chats = chats.toMutableList()
            chats
        }
    }

    private fun chatAlreadyExist(chat: DBChat): Boolean {
        return (chats.any {
            (it.uid == chat.uid) || // same id
                    (it.user1 == chat.user1 && it.user2 == chat.user2) || // same users
                    (it.user1 == chat.user2 && it.user2 == chat.user1) // same users
        })
    }


    /**
     * Removes all copy of a card from the user's collection
     */
    fun removeAllCopyOfCard(card: MagicCard, possession: CardPossession) {
        userRTDB.removeAllCopyOfCard(uid, card.toDBMagicCard(possession))
    }
}
