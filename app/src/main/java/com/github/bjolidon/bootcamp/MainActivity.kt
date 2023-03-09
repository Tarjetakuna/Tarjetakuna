package com.github.bjolidon.bootcamp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {
    //private lateinit var db: DatabaseReference

    private var db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    operator fun get(v: View?) {
        val email = (findViewById<View>(R.id.email) as TextView)
        //.text.toString()
        val phone = (findViewById<View>(R.id.phonenumber) as TextView).text.toString()
        //if (!:: db.isInitialized) db = Firebase.database.reference
        email.text = getFromDatabase(phone)

    }
    fun getFromDatabase(phone : String) : String {
        val future = CompletableFuture<String>()
        db.child(phone).get().addOnSuccessListener {
            if (it.value == null) future.completeExceptionally(NoSuchFieldException())
            else future.complete(it.value as String)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future.get()
    }

    fun set(v: View?) {
        val email = (findViewById<View>(R.id.email) as TextView).text.toString()
        val phone = (findViewById<View>(R.id.phonenumber) as TextView).text.toString()
        //if (!:: db.isInitialized) db = Firebase.database.reference
        sendToDatabase(phone, email)
    }
    fun sendToDatabase(phone : String, email : String) {
        db.child(phone).setValue(email)
    }

}