package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Class used to assign the database, making sure it is only assigned once.
 */
class AppFirebaseDatabase() {
    companion object DB {
        var db: FirebaseDatabase? = null
        var dbref: DatabaseReference? = null
    }

    fun getDB(): DB {
        if (db == null) {
            db = Firebase.database
            dbref = db!!.reference
        }
        return DB
    }

}
