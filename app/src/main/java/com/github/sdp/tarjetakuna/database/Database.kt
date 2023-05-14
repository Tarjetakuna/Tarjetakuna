package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference

/**
 * Interface for the database of the app.
 */
interface Database {
    fun userTable(): DatabaseReference
    fun cardTable(): DatabaseReference
    fun returnDatabaseReference(): DatabaseReference
}
