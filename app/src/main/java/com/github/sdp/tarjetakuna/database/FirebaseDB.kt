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

    override fun provideEmulator(): DatabaseReference {
        dbref.database.useEmulator("10.0.2.2", 9000)
        return dbref

    }


}
