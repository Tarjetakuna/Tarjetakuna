package com.github.sdp.tarjetakuna.database.local

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Provides a local database.
 * TODO modify so that we can add more than one database
 */
object LocalDatabaseProvider {
    private var localDatabase: ArrayList<AppDatabase> = arrayListOf()
    private var databaseNames: ArrayList<String> = arrayListOf()
    const val CARDS_DATABASE_NAME = "cards"

    /**
     * Set a local database
     * @param context the context of the application
     * @param name the name of the database
     * @param test if the database is for testing purposes
     */
    fun setDatabase(context: Context, name: String, test: Boolean = false): AppDatabase? {
        if (Firebase.auth.currentUser == null && !test) {
            return null
        }
        if (!databaseNames.contains(name) && !test) {
            localDatabase.add(Room.databaseBuilder(context, AppDatabase::class.java, name).build())
            databaseNames.add(name)
        } else if (!databaseNames.contains(name)) {
            localDatabase.add(
                Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
            )
            databaseNames.add(name)
        }
        return localDatabase[databaseNames.indexOf(name)]
    }

    /**
     * close the database
     * @param name the name of the database to close
     */
    fun closeDatabase(name: String) {
        localDatabase[databaseNames.indexOf(name)].close()
        localDatabase.remove(localDatabase[databaseNames.indexOf(name)])
        databaseNames.remove(name)
    }

    /**
     * Get the local database
     * @param name the name of the database to get
     */
    fun getDatabase(name: String): AppDatabase? {
        if (Firebase.auth.currentUser == null) {
            return null
        }
        return localDatabase[databaseNames.indexOf(name)]
    }

    /**
     * Delete the databases from the device
     * @param context the context of the application
     * @param names the names of the databases to delete
     */
    fun deleteDatabases(context: Context, names: ArrayList<String>) {
        for (name in names) {
            context.deleteDatabase(name)
            Log.i("Database", "Deleted database $name")
        }
    }
}
