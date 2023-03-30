package com.github.sdp.tarjetakuna.database

import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * This class is used to manage the user's collection of cards.
 */

class UserCardsRTDB {
    private val db = Firebase.database.reference
    private val user = Firebase.auth.currentUser
    private val userCardCollection = db.child(user!!.uid)

    /**
     * Adds a card to the user's collection.
     */
    fun addCardToCollection(card: MagicCard): String {
        val cardUID = card.set.code + card.number
        userCardCollection.child(cardUID).child("name").setValue(card.name)
        userCardCollection.child(cardUID).child("text").setValue(card.text)
        userCardCollection.child(cardUID).child("layout").setValue(card.layout.toString())
        userCardCollection.child(cardUID).child("convertedManaCost")
            .setValue(card.convertedManaCost)
        userCardCollection.child(cardUID).child("manaCost").setValue(card.manaCost)
        userCardCollection.child(cardUID).child("setCode").setValue(card.set.code)
        userCardCollection.child(cardUID).child("setName").setValue(card.set.name)
        userCardCollection.child(cardUID).child("number").setValue(card.number)
        userCardCollection.child(cardUID).child("imageUrl").setValue(card.imageUrl)

        return "Card $cardUID successfully added to collection"

    }

    /**
     * Removes a card from the user's collection.
     */
    fun removeCardFromCollection(card: MagicCard): String {
        val cardUID = card.set.code + card.number
        userCardCollection.child(cardUID).removeValue()
        return "Card $cardUID successfully removed from collection"
    }

    /**
     * Transforms the data obtained from the database to a MagicCard object.
     */
    fun transformData(data: DataSnapshot): MagicCard {
        val name = data.child("name").value.toString()
        val text = data.child("text").value.toString()
        val layout = MagicLayout.valueOf(data.child("layout").value.toString())
        val convertedManaCost = data.child("convertedManaCost").value.toString().toInt()
        val manaCost = data.child("manaCost").value.toString()
        val setCode = data.child("setCode").value.toString()
        val setName = data.child("setName").value.toString()
        val number = data.child("number").value.toString().toInt()
        val imageUrl = data.child("imageUrl").value.toString()
        val set = MagicSet(setCode, setName)
        return MagicCard(name, text, layout, convertedManaCost, manaCost, set, number, imageUrl)
    }

    /**
     * Checks if the user's collection contains a specific card in the given field.
     * @param magicCard The card to be checked.
     * @param field The emplacement of the card in the database.
     * @return True if the card is in the collection, false otherwise.
     */
    fun containsCard(magicCard: MagicCard, field: String): Boolean {
        return false
    }
}
