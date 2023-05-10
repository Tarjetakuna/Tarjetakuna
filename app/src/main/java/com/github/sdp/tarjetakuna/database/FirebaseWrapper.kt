package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import java.util.concurrent.CompletableFuture

class FirebaseWrapper(db: DatabaseReference) : FirebaseInterface {
    private val cards = db.child("cards")

    override fun getCardGlobal(cardUID: String): CompletableFuture<DBMagicCard> {
        val future = CompletableFuture<DBMagicCard>()
        cards.child(cardUID).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("card $cardUID is not in global collection"))
            } else {
                future.complete(Gson().fromJson(it.value.toString(), DBMagicCard::class.java))
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    override fun addCardGlobal(card: DBMagicCard) {
        val cardUID = card.code + card.number
        val data = Gson().toJson(card)
        cards.child(cardUID).setValue(data)
    }

    override fun removeCardGlobal(cardUID: String) {
        cards.child(cardUID).removeValue()
    }

    override fun getAllCardsGlobal(): CompletableFuture<List<DBMagicCard>> {
        val future = CompletableFuture<List<DBMagicCard>>()
        cards.get().addOnSuccessListener {
            val cards = mutableListOf<DBMagicCard>()
            for (card in it.children) {
                cards.add(Gson().fromJson(card.value.toString(), DBMagicCard::class.java))
            }
            future.complete(cards)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }
}
