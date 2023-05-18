package com.github.sdp.tarjetakuna.database

import com.google.android.gms.tasks.Task
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

    /**
     * Clear all data from the database.
     */
    fun clearDatabase(): Task<Void> {
        return dbref.removeValue()
    }


}
