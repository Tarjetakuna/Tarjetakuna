package com.github.sdp.tarjetakuna.database

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

interface Database {
    fun userTable(): DatabaseReference
    fun cardTable(): DatabaseReference
    fun returnDatabaseReference(): DatabaseReference
    fun provideEmulator(): FirebaseDatabase
}
