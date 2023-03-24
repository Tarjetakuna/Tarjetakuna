package com.github.sdp.tarjetakuna.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.model.MagicLayout
import com.github.sdp.tarjetakuna.model.MagicSet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

class UserCardsViewModel : ViewModel() {

    private val db = Firebase.database.reference
    private val user = Firebase.auth.currentUser
    private val userCardCollection = db.child(user!!.uid)

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    //TODO Remove these hardcoded values and replace them with the web API callss
    val card1 = MagicCard(
        "Angel of Mercy", "Flying",
        MagicLayout.Normal, 7, "{5}{W}{W}",
        MagicSet("MT15", "Magic 2015"), 56,
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card"
    )

    private val card1UID = card1.set.code + card1.number

    val card2 = MagicCard(
        "Meandering Towershell", "Islandwalk",
        MagicLayout.DoubleFaced, 5, "{3}{G}{G}",
        MagicSet("MT15", "Magic 2015"), 141,
        "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602"
    )
    val usc = UserCardsRTDB()

    fun onCardButtonClick() {
        //this code should be in a separate function in UserCardsRTDB so it can be separated from this activitiy's UI

        val cardUID = card1UID
        val future = CompletableFuture<DataSnapshot>()
        userCardCollection.child(cardUID).get().addOnSuccessListener {
            if (it.value == null) {
                _message.value = "Card $cardUID was not found in your collection"
                future.completeExceptionally(NoSuchFieldException())
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            _message.value = "Error getting $cardUID"
            future.completeExceptionally(it)
        }
        //the actual value gotten from the database
        future.thenAccept {
            val card = usc.transformData(it) //turn the retrieved data into a MagicCard object
            _message.value = "Card $cardUID was succesfully retrieved from your collection"
        }
    }


}
