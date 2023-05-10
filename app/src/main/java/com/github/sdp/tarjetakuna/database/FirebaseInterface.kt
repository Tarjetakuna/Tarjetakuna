package com.github.sdp.tarjetakuna.database

import java.util.concurrent.CompletableFuture

interface FirebaseInterface {
    fun getCardGlobal(cardUID: String): CompletableFuture<DBMagicCard>
    fun addCardGlobal(card: DBMagicCard)
    fun removeCardGlobal(cardUID: String)
    fun getAllCardsGlobal(): CompletableFuture<List<DBMagicCard>>
}
