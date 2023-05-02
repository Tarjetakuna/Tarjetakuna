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
    private var localDatabases: HashMap<String, AppDatabase> = hashMapOf()
    const val CARDS_DATABASE_NAME = "cards"
    var debugging = false

    /**
     * Set a local database
     * @param context the context of the application
     * @param name the name of the database
     * @param test if the database is for testing purposes
     */
    fun setDatabase(context: Context, name: String, test: Boolean = false): AppDatabase? {
        if (userIsNotConnected()) {
            return null
        }
        if (!localDatabases.contains(name) && !test) {
            localDatabases[name] =
                Room.databaseBuilder(context, AppDatabase::class.java, name).build()
        } else if (!localDatabases.contains(name)) {
            localDatabases[name] =
                Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        }
        return localDatabases[name]
    }

    /**
     * close the database
     * @param name the name of the database to close
     */
    fun closeDatabase(name: String) {
        localDatabases[name]?.close()
        localDatabases.remove(name)
    }

    /**
     * Get the local database
     * @param name the name of the database to get
     */
    fun getDatabase(name: String): AppDatabase? {
        if (userIsNotConnected()) {
            return null
        }
        return localDatabases[name]
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

    /**
     * Check if the user is connected to the database
     */
    private fun userIsNotConnected(): Boolean {
        if (debugging) {
            return false
        }
        return Firebase.auth.currentUser == null
    }
}
