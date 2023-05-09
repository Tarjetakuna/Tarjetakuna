package com.github.sdp.tarjetakuna.model

import com.github.sdp.tarjetakuna.database.CardPossession
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.database.UserRTDB
import com.google.firebase.database.DataSnapshot
import java.util.concurrent.CompletableFuture

/**
 * Represents a user.
 */
data class User(
    val username: String,
    val cards: List<DBMagicCard>,
    val location: Coordinates
    //val chats: List<Chat>

) {
    init {
        require(
            username.matches(
                Regex(
                    "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*\$"
                )
            )
        ) { "Email is not valid" }
    }

    private val userRTDB = UserRTDB()
    fun getAllCards(username: String = this.username): CompletableFuture<DataSnapshot> {
        //todo
        return userRTDB.getAllCardsFromUserCollection()
    }

    fun getAllTradeCards(username: String = this.username) {
        //todo
    }

    fun getCard(username: String = this.username) {
        //todo
    }

    fun addCard(card: MagicCard, possession: CardPossession) {
        //todo
    }

    fun addCards() {
        //todo
    }


}
