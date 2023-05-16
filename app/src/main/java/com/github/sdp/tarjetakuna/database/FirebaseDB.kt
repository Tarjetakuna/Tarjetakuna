package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference

/**
 * Database for the app using Firebase.
 */
class FirebaseDB(db: DatabaseReference = AppFirebaseDatabase().getDB().dbref!!) : Database {

    private var dbref: DatabaseReference = db


    override fun usersTable(): DatabaseReference {
        return dbref.child("users")
    }

    override fun cardsTable(): DatabaseReference {
        return dbref.child("cards")
    }

    override fun usernamesTable(): DatabaseReference {
        return dbref.child("usernames")
    }

    override fun chatsTable(): DatabaseReference {
        return dbref.child("chats")
    }

    override fun messagesTable(): DatabaseReference {
        return dbref.child("messages")
    }

    override fun returnDatabaseReference(): DatabaseReference {
        return dbref
    }

    fun useEmulator() {
        dbref.database.useEmulator("10.0.2.2", 9000)
    }


}
