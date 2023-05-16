package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference

/**
 * Interface for the database of the app.
 */
interface Database {
    /**
     * Returns the reference to the user table.
     */
    fun usersTable(): DatabaseReference

    /**
     * Returns the reference to the card table.
     */
    fun cardsTable(): DatabaseReference

    /**
     * Returns the reference to the username table.
     */
    fun usernamesTable(): DatabaseReference

    /**
     * Returns the reference to the database.
     */
    fun returnDatabaseReference(): DatabaseReference
}
