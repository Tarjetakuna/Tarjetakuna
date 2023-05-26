package com.github.sdp.tarjetakuna.model


import android.util.Log
import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.ChatsRTDB
import com.github.sdp.tarjetakuna.database.DBChat
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.DBMessage
import com.github.sdp.tarjetakuna.database.Database
import com.github.sdp.tarjetakuna.database.FirebaseDB
import com.github.sdp.tarjetakuna.database.UserChatListener
import com.github.sdp.tarjetakuna.database.UserChatsListener
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
    var messages: MutableList<DBMessage> = mutableListOf(),
    var valid: Boolean = true,
    val database: Database = FirebaseDB(),
) : Cloneable {
    private val userRTDB: UserRTDB = UserRTDB(database)
    private val usernamesRTDB: UsernamesRTDB = UsernamesRTDB(database)
    private val chatsRTDB = ChatsRTDB(database)

    constructor(uid: String) : this(
        uid,
        "",
        mutableListOf(),
        Coordinates(),
        valid = false
    )

    public override fun clone(): User {
        return User(
            uid,
            username,
            cards.toMutableList(),
            location,
            chats.toMutableList(),
            messages.toMutableList(),
            valid
        )
    }

    init {
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
    fun removeCard(card: MagicCard, possession: CardPossession): CompletableFuture<Boolean> {
        return userRTDB.removeCard(uid, card.toDBMagicCard(possession))
    }

    /**
     * Creates a new chat with the given user, uploading it to the database
     * in the users, chats and messages nodes.
     */
    fun newChat(chat: Chat): CompletableFuture<Chat> {
        val dbChat = DBChat.toDBChat(chat)
        val chatsFiltered = chatAlreadyExist(dbChat)
        if (chatsFiltered.isNotEmpty()) {
            Log.w("User", "Chat $chat already exist")
            return CompletableFuture.completedFuture(DBChat.fromDBChat(chatsFiltered[0]))
        }
        chats.add(dbChat)
        return userRTDB.addChat(chat)
    }

    /**
     * Creates a new chat with the given user in the user table only and in chats table.
     */
    fun newChat(chat: DBChat): CompletableFuture<DBChat> {
        val chatsFiltered = chatAlreadyExist(chat)
        if (chatsFiltered.isNotEmpty()) {
            Log.w("User", "Chat $chat already exist")
            return CompletableFuture.completedFuture(chatsFiltered[0])
        }
        chats.add(chat)
        return chatsRTDB.addChat(chat).thenCompose { userRTDB.addChat(chat) }
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

    /**
     * Add a listeners on the chats of a user to always have them up to date.
     * From user table and chats table.
     */
    fun addChatsListener(listener: (() -> Unit)? = null) {
        val mListener: UserChatsListener = object : UserChatsListener {
            override fun onChatsRemoved() {
                chats.clear()
                if (listener != null) listener()
            }

            override fun onChatsChanged(chatsChanged: List<DBChat>) {
                chats = chatsChanged.toMutableList()
                if (listener != null) listener()
            }
        }
        userRTDB.addChatsListener(uid, mListener)
    }

    /**
     * Remove the listener on the chats of a user.
     */
    fun removeChatsListener() {
        userRTDB.removeChatsListener(uid)
    }

    /**
     * Add a listener on a single chat to have it up to date at all time.
     * From chats table and message table.
     */
    fun addChatListener(chatUID: String, listener: (() -> Unit)? = null) {
        val mListener: UserChatListener = object : UserChatListener {
            override fun onChatRemoved() {
                messages.clear()
                if (listener != null) listener()
            }

            override fun onChatChanged(messagesChanged: List<DBMessage>) {
                messages = messagesChanged.toMutableList()
                if (listener != null) listener()
            }
        }
        chatsRTDB.addChatListener(chatUID, mListener)
    }

    /**
     * Remove the listener on a single chat.
     */
    fun removeChatListener() {
        chatsRTDB.removeChatListener()
    }

    private fun chatAlreadyExist(chat: DBChat): List<DBChat> {
        Log.d("User", "chatAlreadyExist chats $chats")
        return chats.filter {
            (it.uid == chat.uid) || // same id
                    (it.user1 == chat.user1 && it.user2 == chat.user2) || // same users
                    (it.user1 == chat.user2 && it.user2 == chat.user1) // same users
        }
    }


    /**
     * Removes all copy of a card from the user's collection
     */
    fun removeAllCopyOfCard(card: MagicCard, possession: CardPossession) {
        userRTDB.removeAllCopyOfCard(uid, card.toDBMagicCard(possession))
    }

    /**
     * Send the message to the given user, creating a new chat if it doesn't exist.
     */
    fun sendMessageToUser(
        message: String,
        toUserUID: String
    ): CompletableFuture<Pair<DBChat, DBMessage>> {

        val dbMessage = DBMessage.newMessage(uid, toUserUID, message)
        val dbChat = findChatWithUser(toUserUID)

        val future = CompletableFuture<Pair<DBChat, DBMessage>>()
        if (dbChat != null) {
            sendMessageToUser(dbChat, dbMessage, future)
        } else {
            val chat = DBChat.newChat(uid, toUserUID)
            newChat(chat).thenAccept { mDBChat ->
                sendMessageToUser(mDBChat, dbMessage, future)
            }
        }
        return future
    }

    /**
     * Sends a message to the given chat, updating the messages node, the chats node and the user node.
     */
    private fun sendMessageToUser(
        dbChat: DBChat,
        dbMessage: DBMessage,
        future: CompletableFuture<Pair<DBChat, DBMessage>>
    ) {
        chatsRTDB.addMessageToChat(dbChat.uid, dbMessage).thenApply { (dbChat, dbMessage) ->
            messages.removeIf { it.uid == dbMessage.uid }
            messages.add(dbMessage)
            userRTDB.addChat(dbChat).thenApply {
                chats.removeIf { it.uid == dbChat.uid }
                chats.add(dbChat)
                future.complete(Pair(dbChat, dbMessage))
            }.exceptionally { future.completeExceptionally(it); null }
        }.exceptionally { future.completeExceptionally(it); null }
    }

    /**
     * Finds a chat between this user and the other, if it exists.
     */
    private fun findChatWithUser(otherUserUID: String): DBChat? {
        return chats.find {
            (it.user1 == uid && it.user2 == otherUserUID) ||
                    (it.user1 == otherUserUID && it.user2 == uid)
        }
    }
}
