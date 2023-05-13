package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference

class FirebaseDB(db: DatabaseReference) : Database {
    private var dbref: DatabaseReference

    init {
        dbref = db
    }

    override fun userTable(): DatabaseReference {
        return dbref.child("users")
    }

    override fun cardTable(): DatabaseReference {
        return dbref.child("cards")
    }

    override fun returnDatabaseReference(): DatabaseReference {
        return dbref
    }


}
