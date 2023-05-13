package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference

interface Database {
    fun userTable(): DatabaseReference
    fun cardTable(): DatabaseReference
    fun returnDatabaseReference(): DatabaseReference
}
