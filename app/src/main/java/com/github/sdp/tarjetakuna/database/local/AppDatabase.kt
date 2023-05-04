package com.github.sdp.tarjetakuna.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.sdp.tarjetakuna.database.DBMagicCard
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.ApiMagicCardDao
import com.github.sdp.tarjetakuna.ui.webapi.magicApi.MagicCard

@Database(entities = [DBMagicCard::class, MagicCard::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Contains the methods to access the database for the MagicCard
    abstract fun magicCardDao(): MagicCardDao

    // Contains the methods to access the database for the APIMagicCard
    abstract fun apiMagicCardDao(): ApiMagicCardDao
}
