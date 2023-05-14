package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference

/**
 * Database for the app using Firebase.
 */
class FirebaseDB(db: DatabaseReference = AppFirebaseDatabase().getDB().dbref!!) : Database {

    private var dbref: DatabaseReference = db


    override fun userTable(): DatabaseReference {
        return dbref.child("users")
    }

    override fun cardTable(): DatabaseReference {
        return dbref.child("cards")
    }

    override fun returnDatabaseReference(): DatabaseReference {
        return dbref
    }

    fun useEmulator() {
        dbref.database.useEmulator("10.0.2.2", 9000)
    }


}
