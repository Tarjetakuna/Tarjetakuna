package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import java.util.concurrent.CompletableFuture

/**
 * Class to represent a username - uid pair in the Firebase database
 */
class UsernamesRTDB(database: Database) {
    private var db: DatabaseReference

    init {
        this.db = database.usernamesTable()
    }

    /**
     * Add a username - uid pair to the database
     */
    fun addUsernameUID(uid: String, username: String) {
        db.child(uid).setValue(username)
    }

    /**
     * Get a UID from the database, given a username
     */
    fun getUIDFromUsername(username: String): CompletableFuture<DataSnapshot> {
        val future = CompletableFuture<DataSnapshot>()
        db.child(username).get().addOnSuccessListener {
            if (it.value == null) {
                future.completeExceptionally(NoSuchFieldException("username $username is not in database"))
            } else {
                future.complete(it)
            }
        }.addOnFailureListener {
            future.completeExceptionally(it)
        }
        return future
    }

    /**
     * Remove a username - uid pair from the database
     */
    fun removeUsernameUID(username: String) {
        db.child(username).removeValue()
    }

}
