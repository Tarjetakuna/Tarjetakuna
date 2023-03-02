package com.github.bjolidon.bootcamp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    operator fun get(v: View?) {
        var email = (findViewById<View>(R.id.email) as TextView)
        //.text.toString()
        val phone = (findViewById<View>(R.id.phonenumber) as TextView).text.toString()

        val future = CompletableFuture<String>()
        val db: DatabaseReference = Firebase.database.reference

        db.child(phone).get().addOnSuccessListener {
            if (it.value == null) future.completeExceptionally(NoSuchFieldException())
            else future.complete(it.value as String)
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }

        future.thenAccept {
            email.text = it
        }
    }

    fun set(v: View?) {
        val email = (findViewById<View>(R.id.email) as TextView).text.toString()
        val phone = (findViewById<View>(R.id.phonenumber) as TextView).text.toString()

        val db: DatabaseReference = Firebase.database.reference
        db.child(phone).setValue(email)
    }

}