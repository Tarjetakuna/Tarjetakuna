package com.github.sdp.tarjetakuna.database.local

import android.content.Context
import androidx.room.Room

object LocalDatabaseProvider {
    private var localDatabase: AppDatabase? = null

    /**
     * Set a local database
     */
    fun setDatabase(context: Context, name: String): AppDatabase {
        if (localDatabase == null) {
            localDatabase = Room.databaseBuilder(context, AppDatabase::class.java, name).build()
        }
        return localDatabase!!
    }

    /**
     * Set a database for testing purposes, this database is destroyed when the process is finished
     */
    fun setTestDatabase(context: Context): AppDatabase {
        if (localDatabase == null) {
            localDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        }
        return localDatabase!!
    }

    /**
     * close the database
     */
    fun closeDatabase() {
        localDatabase?.close()
        localDatabase = null
    }
}
