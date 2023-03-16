package com.github.bjolidon.bootcamp.database

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.github.bjolidon.bootcamp.MainActivity
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.model.MagicCard
import com.github.bjolidon.bootcamp.model.MagicLayout
import com.github.bjolidon.bootcamp.model.MagicSet
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.github.bjolidon.bootcamp.database.UserCardsRTDB
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import java.util.concurrent.CompletableFuture

/**
 * Activity to manage cards in the user's collection.
 */
class UserCardsActivity : AppCompatActivity() {
    private val db = Firebase.database.reference
    private val user = Firebase.auth.currentUser
    private val userCardCollection = db.child(user!!.uid)

    private val card1 = MagicCard("Angel of Mercy", "Flying",
        MagicLayout.Normal, 7, "{5}{W}{W}",
        MagicSet("MT15", "Magic 2015"), 56,
        "https://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=149935&type=card")

    private val card1UID = card1.set.code+card1.number

    private val card2 = MagicCard("Meandering Towershell", "Islandwalk",
        MagicLayout.DoubleFaced, 5, "{3}{G}{G}",
        MagicSet("MT15", "Magic 2015"), 141,
        "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=386602")
    private val usc = UserCardsRTDB()

    /**
     * Configures the UI and buttons for managing the current user's card collection
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_cards)

        val setMessage: TextView = findViewById(R.id.text_set_card)
        setMessage.text = ""
        val getMessage: TextView = findViewById(R.id.text_get_card)
        getMessage.text = ""
        var card : MagicCard

        val backButton = findViewById<Button>(R.id.button_back_home2)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        /**
         * Adding a card to the user's collection (database)
         */
        val setButton = findViewById<Button>(R.id.set_card_button)
        setButton.setOnClickListener {
            val msg = usc.addCardToCollection(card2) //this is for now hardcoded but will be replaced by a function call
            setMessage.text = msg

        }

        val getButton = findViewById<Button>(R.id.get_card_button)

        getButton.setOnClickListener{
            //this code should be in a separate function in UserCardsRTDB so it can be separated from this activitiy's UI

            val cardUID = card1UID
            val future = CompletableFuture<DataSnapshot>()
            userCardCollection.child(cardUID).get().addOnSuccessListener {
                if (it.value == null) {
                    putMessage( "Card $cardUID was not found in your collection")
                    future.completeExceptionally(NoSuchFieldException())
                }
                else {
                    future.complete(it)
                }
            }.addOnFailureListener {
                putMessage( "Error getting $cardUID")
                future.completeExceptionally(it)
            }
            //the actual value gotten from the database
            future.thenAccept {
                card = usc.transformData(it) //turn the retrieved data into a MagicCard object
                putMessage("${card.name} was succesfully retrieved from your collection")
            }
        }

    }/**
     * This function is used to display a message in the UI. Should be written UserCardsRTDB
     */
    fun putMessage(msg: String) {
        val getMessage: TextView = findViewById(R.id.text_get_card)
        getMessage.text = msg
    }

}