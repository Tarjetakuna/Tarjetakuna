package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseDB : Database {
    val userDB = Firebase.database.reference.child("users")
    val cardDB = Firebase.database.reference.child("cards")


    override fun userTable(): DatabaseReference {
        return userDB
    }

    override fun cardTable(): DatabaseReference {
        return cardDB
    }

    override fun returnDatabaseReference(): DatabaseReference {
        return Firebase.database.reference
    }

    override fun provideEmulator(): FirebaseDatabase {
        val fb = Firebase.database
        fb.useEmulator("10.0.2.2", 9000)
        return fb

    }


}
